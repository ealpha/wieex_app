package com.wieex.modules.chip.controller;

import com.wieex.common.api.CommonResult;
import com.wieex.common.api.ResultCode;
import com.wieex.modules.chip.dto.ChipKeyInfo;
import com.wieex.modules.chip.dto.UvoiceDeviceInfoParam;
import com.wieex.modules.chip.model.ChipKey;
import com.wieex.modules.chip.service.ChipKeyIssuanceLogService;
import com.wieex.modules.chip.service.ChipKeyService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * Apenman  前端控制器
 * </p>
 *
 * @author txshi
 * @since 2022-09-26
 */
@RestController
@Api(tags = "声瀚授权管理")
@Tag(name = "声瀚授权管理", description = "声瀚授权管理")
@RequestMapping("/uvoice")
public class UvoicetechKeyController {

    String KEY_1 = "339b57d75b55b713bff5cccbc9d7de21";
    String KEY_2 = "50a956778107a4272aae83c86ece77cb";

    @Autowired
    private ChipKeyService chipKeyService;

    @Autowired
    private ChipKeyIssuanceLogService chipKeyIssuanceLogService;


    // @ApiOperation("声瀚获取授权密钥")
    @RequestMapping(value = "/key", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ChipKeyInfo> getItem(@Validated @RequestBody UvoiceDeviceInfoParam uvoiceDeviceInfoParam) {

        //判断info的长度
        String[] chipInfo = uvoiceDeviceInfoParam.getFactory().split("-");
        if (chipInfo.length != 4) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED);
        }

        String chipFactory = chipInfo[0];
        String chip = chipInfo[1];
        String channel = chipInfo[2];
        String burnFactory = chipInfo[3];

        String modelVersion =  uvoiceDeviceInfoParam.getModelVersion();
        String deviceId =  uvoiceDeviceInfoParam.getDeviceId();
        String libRandom = uvoiceDeviceInfoParam.getLibRandom();
        String checkDigit= uvoiceDeviceInfoParam.getCheckDigit();


        //先校验，校验码
        String in_str = deviceId+KEY_1+libRandom;
        System.out.println("==============");
        System.out.println(in_str);
        String checkDigitServer = DigestUtils.md5Hex(in_str);
        System.out.println(checkDigitServer);
        System.out.println("==============");

        if(!checkDigit.equalsIgnoreCase(checkDigitServer)){
            return CommonResult.failed(ResultCode.DIGITERROR);
        }


        //查找对应的key
        ChipKey chipKey = chipKeyService.getKey(chipFactory, chip, channel, burnFactory, modelVersion);

        if (null == chipKey) {
            return CommonResult.failed(ResultCode.NOCHIP);
        }

        //判断是否还有剩余的烧录数量
        if (chipKey.getAvailableSn() <= 0) {
            return CommonResult.failed(ResultCode.NOLICENSE);
        }

        if (!chipKeyService.reduceAvailableSn(chipKey.getId())) {
            return CommonResult.failed(ResultCode.NOLICENSE);
        }

        //进行算法处理
        System.out.println(chipKey.getKeyInfo());
        System.out.println(uvoiceDeviceInfoParam.getDeviceId());

        String sn = DigestUtils.md5Hex(deviceId+KEY_1+libRandom+chipKey.getKeyInfo());

        ChipKeyInfo cki = new ChipKeyInfo();
        cki.setChipId(deviceId+"_"+libRandom+"_"+checkDigit);
        cki.setFactory(chipFactory);
        cki.setModelVersion(modelVersion);
        cki.setSn(sn);

        chipKeyIssuanceLogService.insertIssuanceUvoiceLog(uvoiceDeviceInfoParam, chipKey, cki);

        return CommonResult.success(cki);

    }
}

