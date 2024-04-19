package com.wieex.modules.chip.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 芯片密钥表
 * </p>
 *
 * @author txshi
 * @since 2022-09-26
 */
@Getter
@Setter
@TableName("chip_key")
@ApiModel(value = "ChipKey对象", description = "芯片密钥表")
public class ChipKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("芯片厂家")
    private String chipFactory;

    @ApiModelProperty("芯片型号")
    private String chip;

    @ApiModelProperty("渠道代号")
    private String channel;

    @ApiModelProperty("渠道代号")
    private String burnFactory;

    @ApiModelProperty("模型版本")
    private String modelVersion;

    @ApiModelProperty("密钥")
    private String keyInfo;

    @ApiModelProperty("可用SN剩余数")
    private Integer availableSn;

    @ApiModelProperty("数据启用状态：0->禁用；1->启用")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createTime;


}
