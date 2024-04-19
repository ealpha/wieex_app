package com.wieex.modules.chip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wieex.modules.chip.mapper.ChipKeyMapper;
import com.wieex.modules.chip.model.ChipKey;
import com.wieex.modules.chip.service.ChipKeyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 芯片密钥表 服务实现类
 * </p>
 *
 * @author txshi
 * @since 2022-09-26
 */
@Service
public class ChipKeyServiceImpl extends ServiceImpl<ChipKeyMapper, ChipKey> implements ChipKeyService {

    @Override
    public ChipKey getKey(String chipFactory, String chip, String channel, String burnFactory, String modelVersion) {
        //查询符合条件的芯片
        QueryWrapper<ChipKey> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ChipKey::getChipFactory, chipFactory)
                .eq(ChipKey::getChip, chip)
                .eq(ChipKey::getChannel, channel)
                .eq(ChipKey::getBurnFactory, burnFactory)
                .eq(ChipKey::getModelVersion, modelVersion);

        ChipKey ck = getOne(wrapper);

        if (ck != null) {
            return ck;
        }
        return null;
    }

    @Override
    public boolean reduceAvailableSn(Long id) {

        ChipKey ck = getById(id);

        System.out.println(ck.toString());

        ck.setAvailableSn(ck.getAvailableSn()-1);

        System.out.println(ck.toString());

        return updateById(ck);
    }

}
