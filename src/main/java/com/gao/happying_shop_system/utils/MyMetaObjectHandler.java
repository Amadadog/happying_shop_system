package com.gao.happying_shop_system.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.gao.happying_shop_system.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/09/14 15:37
 * @Description:自定义元数据对象处理器：公共字段自动填充
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
    * @Description: 插入操作自动填充
    * @Param: [metaObject]
    * @return: void
    * @Author: GaoWenQiang
    * @Date: 2022/9/14 16:20
    */
    @Override
    public void insertFill(MetaObject metaObject) {
         metaObject.setValue("createTime", LocalDateTime.now());
         metaObject.setValue("updateTime", LocalDateTime.now());
         metaObject.setValue("createUser", BaseContext.getCurrentId());
         metaObject.setValue("updateUser", BaseContext.getCurrentId());

    }
    /**
    * @Description: 更新操作自动填充
    * @Param: [metaObject]
    * @return: void
    * @Author: GaoWenQiang
    * @Date: 2022/9/14 16:21
    */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
