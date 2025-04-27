package com.wieex.modules.chip.controller;

import com.wieex.modules.chip.service.CorosLicenseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coros/license")
@Api(tags = "Coros许可证管理")
@Tag(name = "Coros许可证管理", description = "用于生成和验证Coros应用的许可证")
public class CorosLicenseController {

    private final CorosLicenseService corosLicenseService;

    public CorosLicenseController(CorosLicenseService corosLicenseService) {
        this.corosLicenseService = corosLicenseService;
    }

    @PostMapping("/generate")
    @ApiOperation(value = "生成许可证", notes = "根据应用ID和平台生成加密的许可证")
    public String generateLicense(
            @ApiParam(value = "应用ID", required = true, example = "com.yf.smart.coros.dist 或 com.coros.coros")
            @RequestParam String appId,
            @ApiParam(value = "平台类型", required = true, example = "android", allowableValues = "android,ios")
            @RequestParam String platform) throws Exception {
        return corosLicenseService.generateLicense(appId, platform);
    }

    @PostMapping("/verify")
    @ApiOperation(value = "验证许可证", notes = "验证许可证的有效性")
    public String verifyLicense(
            @ApiParam(value = "加密的许可证", required = true)
            @RequestParam String encryptedLicense) throws Exception {
        return corosLicenseService.verifyLicense(encryptedLicense);
    }
}

