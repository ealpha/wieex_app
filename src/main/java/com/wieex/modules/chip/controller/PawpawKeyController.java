package com.wieex.modules.chip.controller;

import com.wieex.common.api.CommonResult;
import com.wieex.common.api.ResultCode;
import com.wieex.modules.chip.dto.ChipKeyInfo;
import com.wieex.modules.chip.dto.PawpawDeviceInfoParam;
import com.wieex.modules.chip.model.ChipKey;
import com.wieex.modules.chip.service.ChipKeyIssuanceLogService;
import com.wieex.modules.chip.service.ChipKeyService;
import com.wieex.utils.AizipStringUtils;
import com.wieex.utils.PawpawSNPayload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * PAWPAW 授权管理
 * </p>
 *
 * @author txshi
 * @since 2025-04-23
 */

// @ApiIgnore
@RestController
@Api(tags = "PAWPAW ATS 芯片授权")
@Tag(name = "PAWPAW ATS 芯片授权管理", description = "PAWPAW ATS 芯片授权管理接口，换取MSS算法的 SN")
@RequestMapping("/pawpaw-ats")
public class PawpawKeyController {

    String KEY_1 = "c68fe7b1da0243f2894d3105cefb993f";
    String KEY_2 = "7d32b8a905b47c6e0e9d186f7c68bc84"; // 这个是从服务器获取的

    int[] k1_idx = {15, 7, 23, 11, 9, 5, 17, 20, 8, 14, 25, 3, 12, 19, 6, 22};
    int[] k2_idx = {21, 18, 4, 16, 10, 24, 7, 13, 22, 9, 5, 1, 26, 8, 12, 20};
    int[] chip_idx = {19, 2, 14, 6, 8, 21, 4, 11, 23, 7, 13, 17, 9, 26, 3, 5};
    int[] flash_idx = {12, 25, 6, 10, 3, 14, 9, 22, 16, 1, 19, 5, 18, 11, 7, 4};


    @Autowired
    private ChipKeyService chipKeyService;

    @Autowired
    private ChipKeyIssuanceLogService chipKeyIssuanceLogService;


    // @ApiIgnore
    @ApiOperation("Coros授权管理")
    @RequestMapping(value = "/key", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ChipKeyInfo> getItem(@Validated @RequestBody PawpawDeviceInfoParam  pawpawDeviceInfoParam) {

        //判断info的长度
        String[] chipInfo = pawpawDeviceInfoParam.getFactory().split("-");
        if (chipInfo.length != 4) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED);
        }

        String chipFactory = chipInfo[0];
        String chip = chipInfo[1];
        String channel = chipInfo[2];
        String burnFactory = chipInfo[3];

        String modelVersion =  pawpawDeviceInfoParam.getModelVersion();
        String chipId =  pawpawDeviceInfoParam.getChipId();
        String flashId = pawpawDeviceInfoParam.getFlashId();
        String checkDigit= pawpawDeviceInfoParam.getCheckDigit();

        //先校验，校验码
        String in_str = AizipStringUtils.idxString(chipId,chip_idx) +AizipStringUtils.idxString(KEY_1,k1_idx)   + AizipStringUtils.idxString(flashId,flash_idx);
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
        System.out.println(pawpawDeviceInfoParam.getChipId());

        String sn = DigestUtils.md5Hex(
                 AizipStringUtils.idxString(chipId,chip_idx)
                +AizipStringUtils.idxString(KEY_1,k1_idx)
                +AizipStringUtils.idxString(flashId,flash_idx)
                +AizipStringUtils.idxString(chipKey.getKeyInfo(),k2_idx)
        );

        byte[] payload = PawpawSNPayload.generatePayload(sn, chipId, flashId);

        // 将 payload 转换为十六进制字符串
        String payloadHex = PawpawSNPayload.bytesToHexString(payload);


        ChipKeyInfo cki = new ChipKeyInfo();
        cki.setChipId(chipId+"_"+flashId+"_"+checkDigit);
        cki.setFactory(chipFactory);
        cki.setModelVersion(modelVersion);
        cki.setSn(payloadHex);

        chipKeyIssuanceLogService.insertIssuancePawpawLog(pawpawDeviceInfoParam, chipKey, cki);

        return CommonResult.success(cki);

    }
}

