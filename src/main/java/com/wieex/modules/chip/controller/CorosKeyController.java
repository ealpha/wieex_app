package com.wieex.modules.chip.controller;

import com.wieex.common.api.CommonResult;
import com.wieex.common.api.ResultCode;
import com.wieex.modules.chip.dto.ChipKeyInfo;
import com.wieex.modules.chip.dto.CorosDeviceInfoParam;
import com.wieex.modules.chip.model.ChipKey;
import com.wieex.modules.chip.service.ChipKeyIssuanceLogService;
import com.wieex.modules.chip.service.ChipKeyService;
import com.wieex.utils.AizipStringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
@Api(tags = "Coros授权管理")
@Tag(name = "Coros授权管理", description = "Coros授权管理")
@RequestMapping("/coros-del")
public class CorosKeyController {

    String KEY_1 = "bfa574687c1e8295f709475f585ce5a6";
    String KEY_2 = "175febcfd4a0350e3dad297060b5618e"; // 这个是从服务器获取的

    int[] k1_idx = {15, 7, 23, 11, 9, 5, 17, 20, 8, 14, 25, 3, 12, 19, 6, 22};
    int[] k2_idx = {21, 18, 4, 16, 10, 24, 7, 13, 22, 9, 5, 1, 26, 8, 12, 20};
    int[] ran_idx = {19, 2, 14, 6, 8, 21, 4, 11, 23, 7, 13, 17, 9, 26, 3, 5};
    int[] mac_idx = {12, 25, 6, 10, 3, 14, 9, 22, 16, 1, 19, 5, 18, 11, 7, 4};


    @Autowired
    private ChipKeyService chipKeyService;

    @Autowired
    private ChipKeyIssuanceLogService chipKeyIssuanceLogService;


    @ApiIgnore
    @ApiOperation("Coros授权管理")
    @RequestMapping(value = "/key", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ChipKeyInfo> getItem(@Validated @RequestBody CorosDeviceInfoParam corosDeviceInfoParam) {

        //判断info的长度
        String[] chipInfo = corosDeviceInfoParam.getFactory().split("-");
        if (chipInfo.length != 4) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED);
        }

        String chipFactory = chipInfo[0];
        String chip = chipInfo[1];
        String channel = chipInfo[2];
        String burnFactory = chipInfo[3];

        String modelVersion =  corosDeviceInfoParam.getModelVersion();
        String deviceId =  corosDeviceInfoParam.getDeviceId();
        String libRandom = corosDeviceInfoParam.getLibRandom(); // COROS 不是随机数，是手表上看到的手表ID，经过coros算法算出的
        String checkDigit= corosDeviceInfoParam.getCheckDigit();




        //先校验，校验码
        String in_str = AizipStringUtils.idxString(deviceId,mac_idx) +AizipStringUtils.idxString(KEY_1,k1_idx)   + AizipStringUtils.idxString(libRandom,ran_idx);
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
        System.out.println(corosDeviceInfoParam.getDeviceId());

        String sn = DigestUtils.md5Hex(
                 AizipStringUtils.idxString(deviceId,mac_idx)
                +AizipStringUtils.idxString(KEY_1,k1_idx)
                +AizipStringUtils.idxString(libRandom,ran_idx)
                +AizipStringUtils.idxString(chipKey.getKeyInfo(),k2_idx)
        );

        ChipKeyInfo cki = new ChipKeyInfo();
        cki.setChipId(deviceId+"_"+libRandom+"_"+checkDigit);
        cki.setFactory(chipFactory);
        cki.setModelVersion(modelVersion);
        cki.setSn(sn);

        chipKeyIssuanceLogService.insertIssuanceCorosLog(corosDeviceInfoParam, chipKey, cki);

        return CommonResult.success(cki);

    }
}

