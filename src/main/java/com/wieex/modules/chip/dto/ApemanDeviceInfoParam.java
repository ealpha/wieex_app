package com.wieex.modules.chip.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;


@Getter
@Setter
public class ApemanDeviceInfoParam {
    @NotEmpty
    @ApiModelProperty(value = "「厂家信息」由Aizip分配，格式：A-B-C-D", required = true, example = "apeman-camera-wifi-apeman")
    private String factory;

    @NotEmpty
    @ApiModelProperty(value = "模型版本，格式：BC-A1.0.0,从烧录的模型中读取或双方约定", required = true, example = "BC-1.0.0")
    private String modelVersion;

    @NotEmpty
    @ApiModelProperty(value = "设备DeviceID，本设备为MAC，小写", required = true, example = "36:e7:f4:35:40:08")
    private String deviceId;

    @ApiModelProperty(value = "由设备算法库给出，例如：94de14e7070371271c654e1f7c5bbe07", required = true , example = "1695906654943")
    private String libRandom;

    @ApiModelProperty(value = "由设备算法库给出", required = true)
    private String checkDigit;
}