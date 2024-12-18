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

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.ZoneId;

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
@RequestMapping("/phaten")
public class PhatenKeyController {

    @Autowired
    private ChipKeyService chipKeyService;

    @Autowired
    private ChipKeyIssuanceLogService chipKeyIssuanceLogService;

    String phaten_secret = "8e1e308ed6c50ebe9660a86382d9839b";

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


        // md5(chipId+factory+modelVersion+reqTimestamp+Secret),「Secret」由Aizip分配
        String sign = chipInfoParam.getSign().toLowerCase();

        System.out.println("===== sign =====");
        System.out.println(sign);

        // 将 Date 转换为 ZonedDateTime
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(chipInfoParam.getReqTimestamp().getTime())
                .atZone(ZoneId.of("UTC"));

        // 定义格式化模式（ISO-8601 格式）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // 将 ZonedDateTime 格式化为字符串
        String formattedDate_ReqTimestamp = zonedDateTime.format(formatter);

        System.out.println("Formatted Date: " + formattedDate_ReqTimestamp);

        //chipId+factory+modelVersion+reqTimestamp+Secret
        String sign_in =  chipInfoParam.getChipId() +chipInfoParam.getFactory()+ chipInfoParam.getModelVersion() + formattedDate_ReqTimestamp + phaten_secret;

        System.out.println("===== sign_in =====");
        System.out.println(sign_in);

        System.out.println("===== md5Hex(sign_in).toLowerCase() =====");
        System.out.println(md5Hex(sign_in).toLowerCase());

        if (!sign.equals(md5Hex(sign_in).toLowerCase())) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED);
        }

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

