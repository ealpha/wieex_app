package com.wieex.modules.chip.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Getter
@Setter
public class PawpawDeviceInfoParam {
    @NotEmpty
    @ApiModelProperty(value = "「厂家信息」由Aizip分配，格式：A-B-C-D", required = true, example = "Actions-ATS3625N-HARMAN-PAWPAW")
    private String factory;

    @NotEmpty
    @ApiModelProperty(value = "模型版本，从烧录的模型中读取或双方约定", required = true, example = "MSS")
    private String modelVersion;

    @NotEmpty
    @Size(min = 16, max = 16, message = "chipId must be exactly 16 characters long")
    @ApiModelProperty(value = "ATS3625N芯片ChipId，长度必须为16个字符", required = true, example = "90465834153CE90A")
    private String chipId;

    @NotEmpty
    @Size(min = 32, max = 32, message = "flashId must be exactly 32 characters long")
    @ApiModelProperty(value = "ATS3625N芯片FlashId，长度必须为32个字符", required = true , example = "000000005136363633159d4c45ffffff")
    // 检查flashId 长度为 16。
    private String flashId;

    @NotEmpty
    @Size(min = 32, max = 32, message = "checkDigit 32 characters long")
    @ApiModelProperty(value = "由设备算法库给出，例如：1271c654e1f94de14e7070377c5bbe07，长度必须为32个字符", required = true)
    private String checkDigit;
}