package com.wieex.modules.chip.controller;

import com.wieex.common.api.CommonResult;
import com.wieex.common.api.ResultCode;
import com.wieex.modules.chip.service.CorosLicenseService;
import com.wieex.modules.chip.service.ChipKeyIssuanceLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/coros/license")
@Api(tags = "Coros许可证管理")
@Tag(name = "Coros许可证管理", description = "用于生成和验证Coros应用的许可证")
public class CorosLicenseController {

    private final CorosLicenseService corosLicenseService;
    private final ChipKeyIssuanceLogService chipKeyIssuanceLogService;

    public CorosLicenseController(CorosLicenseService corosLicenseService, ChipKeyIssuanceLogService chipKeyIssuanceLogService) {
        this.corosLicenseService = corosLicenseService;
        this.chipKeyIssuanceLogService = chipKeyIssuanceLogService;
    }

    @PostMapping("/generate")
    @ApiOperation(value = "生成许可证", notes = "根据应用ID和平台生成加密的许可证")
    public CommonResult<Map<String, Object>> generateLicense(
            @ApiParam(value = "应用ID", required = true, example = "com.yf.smart.coros.dist 或 com.coros.coros")
            @RequestParam String appId,
            @ApiParam(value = "平台类型", required = true, example = "android", allowableValues = "android,ios")
            @RequestParam String platform) throws Exception {
        try {
            String result = corosLicenseService.generateLicense(appId, platform);
            JSONObject json = new JSONObject(result);
            Map<String, Object> map = new HashMap<>();
            String license = json.getString("license");
            String expiry = json.getString("expiry");
            
            // 记录生成许可证的日志
            chipKeyIssuanceLogService.insertLicenseGenerateLog(appId, platform, license, expiry);
            
            map.put("license", license);
            map.put("app_id", json.getString("app_id"));
            map.put("platform", json.getString("platform"));
            map.put("expiry", expiry);
            return CommonResult.success(map);
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message.contains("Invalid platform")) {
                return CommonResult.failed(ResultCode.VALIDATE_FAILED, "无效的平台类型");
            } else if (message.contains("Unauthorized Android package name")) {
                return CommonResult.failed(ResultCode.VALIDATE_FAILED, "无效的Android应用ID");
            } else if (message.contains("Unauthorized iOS bundle ID")) {
                return CommonResult.failed(ResultCode.VALIDATE_FAILED, "无效的iOS应用ID");
            }
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, message);
        }
    }

    @PostMapping("/verify")
    @ApiOperation(value = "验证许可证", notes = "验证许可证的有效性")
    public CommonResult<Map<String, Object>> verifyLicense(
            @ApiParam(value = "加密的许可证", required = true)
            @RequestParam String encryptedLicense) throws Exception {
        String result = corosLicenseService.verifyLicense(encryptedLicense);
        if (result.startsWith("{")) {
            // 如果是 JSON 格式，说明验证通过
            JSONObject json = new JSONObject(result);
            Map<String, Object> map = new HashMap<>();
            map.put("status", json.getString("status"));
            map.put("app_id", json.getString("app_id"));
            map.put("platform", json.getString("platform"));
            map.put("expiry", json.getString("expiry"));
            return CommonResult.success(map);
        }
        switch (result) {
            case "Invalid platform":
                return CommonResult.failed(ResultCode.VALIDATE_FAILED, "无效的平台类型");
            case "Invalid app ID for Android":
                return CommonResult.failed(ResultCode.VALIDATE_FAILED, "无效的Android应用ID");
            case "Invalid app ID for iOS":
                return CommonResult.failed(ResultCode.VALIDATE_FAILED, "无效的iOS应用ID");
            case "License expired":
                return CommonResult.failed(ResultCode.VALIDATE_FAILED, "许可证已过期");
            default:
                return CommonResult.failed(ResultCode.VALIDATE_FAILED, "许可证验证失败");
        }
    }
}

