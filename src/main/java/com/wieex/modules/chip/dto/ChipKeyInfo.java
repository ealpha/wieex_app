package com.wieex.modules.chip.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
public class ChipKeyInfo {
    @NotEmpty
    @ApiModelProperty(value = "「厂家信息」由Aizip分配，格式：A-B-C-D", required = true, example = "A-B-C-D")
    private String factory;

    @NotEmpty
    @ApiModelProperty(value = "芯片ID，一般是CPUID：00000000983055218826E6FFFFFFFFFF", required = true, example = "00000000983055218826E6FFFFFFFFFF")
    private String chipId;

    @NotEmpty
    @ApiModelProperty(value = "模型版本，格式：KWS-A1.0.0,从烧录的模型中读取", required = true, example = "KWS-1.0.0P")
    private String modelVersion;

    @ApiModelProperty(value = "服务器返回的授权码", required = true)
    private String sn;

}