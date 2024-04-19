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
 * 接口签发SN日志表
 * </p>
 *
 * @author txshi
 * @since 2022-09-27
 */
@Getter
@Setter
@TableName("chip_key_issuance_log")
@ApiModel(value = "ChipKeyIssuanceLog对象", description = "接口签发SN日志表")
public class ChipKeyIssuanceLog implements Serializable {

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

    @ApiModelProperty("密钥")
    private String keyInfo;

    @ApiModelProperty("芯片ID，一般是CPUID")
    private String chipId;

    @ApiModelProperty("模型版本")
    private String modelVersion;

    @ApiModelProperty("签发的SN")
    private String sn;

    @ApiModelProperty("请求时候携带的时间戳")
    private Date requestTimestamp;

    @ApiModelProperty("请求时候IP")
    private String ip;

    @ApiModelProperty("浏览器类型")
    private String userAgent;

    private Date createTime;


}
