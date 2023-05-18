package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.entity.AccessLog;
import com.gao.happying_shop_system.entity.User;
import com.gao.happying_shop_system.mapper.AccessLogMapper;
import com.gao.happying_shop_system.service.IAccessService;
import com.gao.happying_shop_system.utils.R;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2023/03/18 17:20
 * @Description:
 */
@Service
public class AccessLogServiceImpl extends ServiceImpl<AccessLogMapper, AccessLog> implements IAccessService {
    @Override
    public R<Integer> getVisits(String beginTime, String endTime) {
        LambdaQueryWrapper<AccessLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(AccessLog::getCreateTime, beginTime, endTime);
        // 查询接口访问量
        int count = this.count(wrapper);
        return R.success(count);
    }
}
