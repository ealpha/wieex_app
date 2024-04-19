package com.wieex.modules.chip.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;


@Getter
@Setter
public class ChipInfoParam {
    @NotEmpty
    @ApiModelProperty(value = "「厂家信息」由Aizip分配，格式：A-B-C-D", required = true, example = "XXXX-19x-XXXX-XXXX")
    private String factory;

    @NotEmpty
    @ApiModelProperty(value = "芯片ID，一般是CHIPID：00000000983055218826E6FFFFFFFFFF", required = true, example = "00000000983055218826E6FFFFFFFFFF")
    private String chipId;

    @NotEmpty
    @ApiModelProperty(value = "模型版本，格式：KWS-A1.0.0,从烧录的模型中读取", required = true, example = "KWS-1.0.0P")
    private String modelVersion;

    @ApiModelProperty(value = "请求时间戳，例如：2022-09-27T11:22:48.273Z", required = true)
    private Date reqTimestamp;

    @ApiModelProperty(value = "md5(chipId+factory+modelVersion+reqTimestamp+Secret),「Secret」由Aizip分配", required = true)
    private String sign;

}