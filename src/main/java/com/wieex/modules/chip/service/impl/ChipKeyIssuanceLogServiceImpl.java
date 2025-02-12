package com.wieex.modules.chip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wieex.modules.chip.dto.*;
import com.wieex.modules.chip.mapper.ChipKeyIssuanceLogMapper;
import com.wieex.modules.chip.model.ChipKey;
import com.wieex.modules.chip.model.ChipKeyIssuanceLog;
import com.wieex.modules.chip.service.ChipKeyIssuanceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static cn.hutool.core.date.DateTime.now;

/**
 * <p>
 * 接口签发SN日志表 服务实现类
 * </p>
 *
 * @author txshi
 * @since 2022-09-27
 */
@Service
public class ChipKeyIssuanceLogServiceImpl extends ServiceImpl<ChipKeyIssuanceLogMapper, ChipKeyIssuanceLog> implements ChipKeyIssuanceLogService {


    @Autowired
    private ChipKeyIssuanceLogMapper chipKeyIssuanceLogMapper;

    /**
     * 添加签发记录
     *
     * @param chipInfoParam
     * @param chipKey
     * @param chipKeyInfo
     */

    public void insertIssuanceLog(ChipInfoParam chipInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo) {

        ChipKeyIssuanceLog issuanceLog = new ChipKeyIssuanceLog();

        issuanceLog.setChipFactory(chipKey.getChipFactory());
        issuanceLog.setChip(chipKey.getChip());
        issuanceLog.setChannel(chipKey.getChannel());
        issuanceLog.setBurnFactory(chipKey.getBurnFactory());

        issuanceLog.setChipId(chipKeyInfo.getChipId());
        issuanceLog.setModelVersion(chipKeyInfo.getModelVersion());
        issuanceLog.setSn(chipKeyInfo.getSn());
        issuanceLog.setRequestTimestamp(chipInfoParam.getReqTimestamp());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        issuanceLog.setIp(request.getRemoteAddr() + "," + request.getParameter("X-Real-IP"));
        issuanceLog.setCreateTime(new Date());

        chipKeyIssuanceLogMapper.insert(issuanceLog);
    }


    /**
     * 添加签发记录
     *
     * @param apemanDeviceInfoParam
     * @param chipKey
     * @param chipKeyInfo
     */

    public void insertIssuanceApemanLog(ApemanDeviceInfoParam apemanDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo) {

        ChipKeyIssuanceLog issuanceLog = new ChipKeyIssuanceLog();

        issuanceLog.setChipFactory(chipKey.getChipFactory());
        issuanceLog.setChip(chipKey.getChip());
        issuanceLog.setChannel(chipKey.getChannel());
        issuanceLog.setBurnFactory(chipKey.getBurnFactory());

        issuanceLog.setChipId(chipKeyInfo.getChipId());
        issuanceLog.setModelVersion(chipKeyInfo.getModelVersion());
        issuanceLog.setSn(chipKeyInfo.getSn());
        issuanceLog.setRequestTimestamp(now());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        issuanceLog.setIp(request.getRemoteAddr() + "," + request.getParameter("X-Real-IP"));
        issuanceLog.setCreateTime(new Date());

        chipKeyIssuanceLogMapper.insert(issuanceLog);
    }


    /**
     * 添加签发记录
     *
     * @param uvoiceDeviceInfoParam
     * @param chipKey
     * @param chipKeyInfo
     */

    public void insertIssuanceUvoiceLog(UvoiceDeviceInfoParam uvoiceDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo) {

        ChipKeyIssuanceLog issuanceLog = new ChipKeyIssuanceLog();

        issuanceLog.setChipFactory(chipKey.getChipFactory());
        issuanceLog.setChip(chipKey.getChip());
        issuanceLog.setChannel(chipKey.getChannel());
        issuanceLog.setBurnFactory(chipKey.getBurnFactory());

        issuanceLog.setChipId(chipKeyInfo.getChipId());
        issuanceLog.setModelVersion(chipKeyInfo.getModelVersion());
        issuanceLog.setSn(chipKeyInfo.getSn());
        issuanceLog.setRequestTimestamp(now());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        issuanceLog.setIp(request.getRemoteAddr() + "," + request.getParameter("X-Real-IP"));
        issuanceLog.setCreateTime(new Date());

        chipKeyIssuanceLogMapper.insert(issuanceLog);
    }

    /**
     * 添加签发记录
     *
     * @param glazeroDeviceInfoParam
     * @param chipKey
     * @param chipKeyInfo
     */

    public void insertIssuanceGlazeroLog(GlazeroDeviceInfoParam glazeroDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo) {

        ChipKeyIssuanceLog issuanceLog = new ChipKeyIssuanceLog();

        issuanceLog.setChipFactory(chipKey.getChipFactory());
        issuanceLog.setChip(chipKey.getChip());
        issuanceLog.setChannel(chipKey.getChannel());
        issuanceLog.setBurnFactory(chipKey.getBurnFactory());

        issuanceLog.setChipId(chipKeyInfo.getChipId());
        issuanceLog.setModelVersion(chipKeyInfo.getModelVersion());
        issuanceLog.setSn(chipKeyInfo.getSn());
        issuanceLog.setRequestTimestamp(now());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        issuanceLog.setIp(request.getRemoteAddr() + "," + request.getParameter("X-Real-IP"));
        issuanceLog.setCreateTime(new Date());

        chipKeyIssuanceLogMapper.insert(issuanceLog);
    }

    public void insertIssuanceWjaLog(WjaDeviceInfoParam wjaDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo) {

        ChipKeyIssuanceLog issuanceLog = new ChipKeyIssuanceLog();

        issuanceLog.setChipFactory(chipKey.getChipFactory());
        issuanceLog.setChip(chipKey.getChip());
        issuanceLog.setChannel(chipKey.getChannel());
        issuanceLog.setBurnFactory(chipKey.getBurnFactory());

        issuanceLog.setChipId(chipKeyInfo.getChipId());
        issuanceLog.setModelVersion(chipKeyInfo.getModelVersion());
        issuanceLog.setSn(chipKeyInfo.getSn());
        issuanceLog.setRequestTimestamp(now());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        issuanceLog.setIp(request.getRemoteAddr() + "," + request.getParameter("X-Real-IP"));
        issuanceLog.setCreateTime(new Date());

        chipKeyIssuanceLogMapper.insert(issuanceLog);
    }


    public void insertIssuanceCorosLog(CorosDeviceInfoParam corosDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo) {

        ChipKeyIssuanceLog issuanceLog = new ChipKeyIssuanceLog();

        issuanceLog.setChipFactory(chipKey.getChipFactory());
        issuanceLog.setChip(chipKey.getChip());
        issuanceLog.setChannel(chipKey.getChannel());
        issuanceLog.setBurnFactory(chipKey.getBurnFactory());

        issuanceLog.setChipId(chipKeyInfo.getChipId());
        issuanceLog.setModelVersion(chipKeyInfo.getModelVersion());
        issuanceLog.setSn(chipKeyInfo.getSn());
        issuanceLog.setRequestTimestamp(now());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        issuanceLog.setIp(request.getRemoteAddr() + "," + request.getParameter("X-Real-IP"));
        issuanceLog.setCreateTime(new Date());

        chipKeyIssuanceLogMapper.insert(issuanceLog);
    }
}
