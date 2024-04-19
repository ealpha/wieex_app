package com.wieex.modules.chip.controller;

import com.wieex.common.api.CommonResult;
import com.wieex.common.api.ResultCode;
import com.wieex.modules.chip.dto.ChipInfoParam;
import com.wieex.modules.chip.dto.ChipKeyInfo;
import com.wieex.modules.chip.model.ChipKey;
import com.wieex.modules.chip.service.ChipKeyIssuanceLogService;
import com.wieex.modules.chip.service.ChipKeyService;
import com.wieex.utils.AES128;
import com.wieex.utils.AesEcbCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

/**
 * <p>
 * 芯片密钥表 前端控制器
 * </p>
 *
 * @author txshi
 * @since 2022-09-26
 */

@ApiIgnore
@RestController
// @Api(tags = "授权管理")
// @Tag(name = "Chip", description = "授权管理")
@RequestMapping("/chip")
public class ChipKeyController {

    @Autowired
    private ChipKeyService chipKeyService;

    @Autowired
    private ChipKeyIssuanceLogService chipKeyIssuanceLogService;


    // @ApiOperation("获取授权密钥")
    @RequestMapping(value = "/key", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ChipKeyInfo> getItem(@Validated @RequestBody ChipInfoParam chipInfoParam) {

        //TODO：验证加密密钥

        //判断info的长度
        String[] chipInfo = chipInfoParam.getFactory().split("-");
        if (chipInfo.length != 4) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED);
        }

        String chipFactory = chipInfo[0];
        String chip = chipInfo[1];
        String channel = chipInfo[2];
        String burnFactory = chipInfo[3];

        //查找对应的key
        ChipKey chipKey = chipKeyService.getKey(chipFactory, chip, channel, burnFactory, chipInfoParam.getModelVersion());

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
        try {

            System.out.println(chipKey.getKeyInfo());
            System.out.println(chipInfoParam.getChipId());

            byte[] roundKey = AesEcbCodec.decodeHex(chipKey.getKeyInfo());
            byte[] chipIds = AesEcbCodec.decodeHex(chipInfoParam.getChipId());
            AES128.AES_ECB_encrypt(roundKey, chipIds);
            String sn = AesEcbCodec.encodeHexString(chipIds);


            ChipKeyInfo cki = new ChipKeyInfo();
            cki.setChipId(chipInfoParam.getChipId());
            cki.setFactory(chipInfoParam.getFactory());
            cki.setModelVersion(chipInfoParam.getModelVersion());
            cki.setSn(sn);

            chipKeyIssuanceLogService.insertIssuanceLog(chipInfoParam, chipKey, cki);

            return CommonResult.success(cki);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return CommonResult.failed(ResultCode.FAILED);

    }
}

