package com.wieex.modules.chip.controller;

import com.wieex.common.api.CommonResult;
import com.wieex.common.api.ResultCode;
import com.wieex.modules.chip.dto.ApemanDeviceInfoParam;
import com.wieex.modules.chip.dto.ChipKeyInfo;
import com.wieex.modules.chip.model.ChipKey;
import com.wieex.modules.chip.service.ChipKeyIssuanceLogService;
import com.wieex.modules.chip.service.ChipKeyService;
//import com.wieex.utils.AES128;
//import com.wieex.utils.AesEcbCodec;
import com.wieex.utils.AizipStringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.digest.DigestUtils;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

/**
 * <p>
 * Apenman  前端控制器
 * </p>
 *
 * @author txshi
 * @since 2022-09-26
 */
@ApiIgnore
@RestController
@Api(tags = "授权管理")
@Tag(name = "授权管理", description = "授权管理")
@RequestMapping("/apeman")
public class ApemanKeyController {

    String KEY_1 = "499c89ff1b59fa8be89d7d0cfd9434d1";
    String KEY_2 = "4bd9cc40c2d82aafe08fb0362a57fa0e";

    int[] k1_idx = {30, 8, 11, 18, 12, 14, 26, 11, 16, 18, 22, 8, 16, 6, 20, 13};
    int[] k2_idx = {24, 22, 16, 21, 5, 12, 19, 15, 7, 10, 2, 3, 20, 9, 26, 18};
    int[] ran_idx = {26, 3, 9, 1, 7, 28, 13, 20, 16, 22, 18, 8, 11, 10, 5, 4};
    int[] mac_idx = {8, 28, 2, 23, 19, 11, 5, 17, 1, 13, 8, 11, 1, 9, 4, 14};


    @Autowired
    private ChipKeyService chipKeyService;

    @Autowired
    private ChipKeyIssuanceLogService chipKeyIssuanceLogService;


    @ApiIgnore
    @ApiOperation("Apeman获取授权密钥")
    @RequestMapping(value = "/key", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ChipKeyInfo> getItem(@Validated @RequestBody ApemanDeviceInfoParam apemanDeviceInfoParam) {

        //判断info的长度
        String[] chipInfo = apemanDeviceInfoParam.getFactory().split("-");
        if (chipInfo.length != 4) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED);
        }

        String chipFactory = chipInfo[0];
        String chip = chipInfo[1];
        String channel = chipInfo[2];
        String burnFactory = chipInfo[3];

        String modelVersion =  apemanDeviceInfoParam.getModelVersion();
        String deviceId =  apemanDeviceInfoParam.getDeviceId();
        String libRandom = apemanDeviceInfoParam.getLibRandom();
        String checkDigit= apemanDeviceInfoParam.getCheckDigit();


        //先校验，校验码

        String in_str = deviceId+KEY_1+libRandom;

        if(modelVersion.startsWith("babycry_m") || modelVersion.indexOf("apeman_JZT23")>0){
             in_str = AizipStringUtils.idxString(deviceId,mac_idx) +AizipStringUtils.idxString(KEY_1,k1_idx)   + AizipStringUtils.idxString(libRandom,ran_idx);
        }

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
        System.out.println(apemanDeviceInfoParam.getDeviceId());

        String sn = DigestUtils.md5Hex(deviceId+KEY_1+libRandom+chipKey.getKeyInfo());


        if(modelVersion.startsWith("babycry_m") || modelVersion.indexOf("apeman_JZT23")>0){
            sn = DigestUtils.md5Hex(
                    AizipStringUtils.idxString(deviceId,mac_idx)
                            +AizipStringUtils.idxString(KEY_1,k1_idx)
                            +AizipStringUtils.idxString(libRandom,ran_idx)
                            +AizipStringUtils.idxString(chipKey.getKeyInfo(),k2_idx)
            );
        }


        ChipKeyInfo cki = new ChipKeyInfo();
        cki.setChipId(deviceId+"_"+libRandom+"_"+checkDigit);
        cki.setFactory(chipFactory);
        cki.setModelVersion(modelVersion);
        cki.setSn(sn);

        chipKeyIssuanceLogService.insertIssuanceApemanLog(apemanDeviceInfoParam, chipKey, cki);

        return CommonResult.success(cki);

    }
}

