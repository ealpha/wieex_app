package com.wieex.modules.chip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wieex.modules.chip.model.ChipKey;

/**
 * <p>
 * 芯片密钥表 服务类
 * </p>
 *
 * @author txshi
 * @since 2022-09-26
 */
public interface ChipKeyService extends IService<ChipKey> {
    ChipKey getKey(String chipFactory, String chip, String channel, String burnFactory, String modelVersion);
    boolean reduceAvailableSn(Long id);
}
