package com.wieex.modules.chip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wieex.modules.chip.dto.*;
import com.wieex.modules.chip.model.ChipKey;
import com.wieex.modules.chip.model.ChipKeyIssuanceLog;

/**
 * <p>
 * 接口签发SN日志表 服务类
 * </p>
 *
 * @author txshi
 * @since 2022-09-27
 */
public interface ChipKeyIssuanceLogService extends IService<ChipKeyIssuanceLog> {

    void insertIssuanceLog(ChipInfoParam chipInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo);
    void insertIssuanceApemanLog(ApemanDeviceInfoParam apemanDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo);
    void insertIssuanceUvoiceLog(UvoiceDeviceInfoParam uvoiceDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo);
    void insertIssuanceGlazeroLog(GlazeroDeviceInfoParam glazeroDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo);
    void insertIssuanceWjaLog(WjaDeviceInfoParam wjaDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo);
    void insertIssuanceCorosLog(CorosDeviceInfoParam corosDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo);
    void insertIssuancePawpawLog(PawpawDeviceInfoParam pawpawDeviceInfoParam, ChipKey chipKey, ChipKeyInfo chipKeyInfo);
}
