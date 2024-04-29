package com.wieex.modules.chip.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
public class GlazeroDeviceInfoParam {
    @NotEmpty
    @ApiModelProperty(value = "「厂家信息」由Aizip分配，格式：A-B-C-D", required = true, example = "ingenic-T41-glazero-glazero")
    private String factory;

    @NotEmpty
    @ApiModelProperty(value = "模型版本，从烧录的模型中读取或双方约定", required = true, example = "VwwPackage_m1.0.0_Doorbell_d1.0.0_Glazero")
    private String modelVersion;

    @NotEmpty
    @ApiModelProperty(value = "设备DeviceID,为芯片的唯一ID", required = true, example = "36e7f4354008")
    private String deviceId;

    @ApiModelProperty(value = "由设备算法库给出，例如：94395906654943", required = true , example = "94395906654943")
    private String libRandom;

    @ApiModelProperty(value = "由设备算法库给出，例如：1271c654e1f94de14e7070377c5bbe07", required = true)
    private String checkDigit;
}