package com.wieex.modules.chip.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
public class CorosDeviceInfoParam {
    @NotEmpty
    @ApiModelProperty(value = "「厂家信息」由Aizip分配，格式：A-B-C-D", required = true, example = "Ambiq-Apollo-COROS-WATCH")
    private String factory;

    @NotEmpty
    @ApiModelProperty(value = "模型版本，从烧录的模型中读取或双方约定", required = true, example = "KWS-DNR")
    private String modelVersion;

    @NotEmpty
    @ApiModelProperty(value = "设备芯片ChipID,为芯片的唯一ID，跑在主芯片唯一ID为 主芯片片ChipID，跑在IA8201为8201的唯一ID", required = true, example = "ab88ab7b-11d180d0")
    private String deviceId;

    @ApiModelProperty(value = "由设备算法库给出,COROS手表为，COROS计算后的手表SN，可以在手表上看到的值，例如：c6f89d", required = true , example = "c6f89d")
    private String libRandom;

    @ApiModelProperty(value = "由设备算法库给出，例如：1271c654e1f94de14e7070377c5bbe07", required = true)
    private String checkDigit;
}