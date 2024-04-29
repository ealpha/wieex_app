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
    @ApiModelProperty(value = "芯片ID", required = true, example = "0000552188F")
    private String chipId;

    @NotEmpty
    @ApiModelProperty(value = "模型版本，从烧录的模型中读取或双方约定", required = true, example = "xx-1.0.0P")
    private String modelVersion;

    @ApiModelProperty(value = "服务器返回的授权码", required = true)
    private String sn;

}