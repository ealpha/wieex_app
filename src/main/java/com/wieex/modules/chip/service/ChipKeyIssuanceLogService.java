package com.wieex.modules.chip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wieex.modules.chip.dto.ApemanDeviceInfoParam;
import com.wieex.modules.chip.dto.ChipInfoParam;
import com.wieex.modules.chip.dto.ChipKeyInfo;
import com.wieex.modules.chip.dto.UvoiceDeviceInfoParam;
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

}
