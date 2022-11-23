package com.gao.happying_shop_system.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/09/14 17:02
 * @Description:基于ThreadLocal封装工具类。用户保存和获取当前登录用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    /**
    * @Description: 同一线程内设置值
    * @Param: [id]
    * @return: void
    * @Author: GaoWenQiang
    * @Date: 2022/9/14 17:15
    */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
    /**
    * @Description: 同一线程内获取值
    * @Param: []
    * @return: java.lang.Long
    * @Author: GaoWenQiang
    * @Date: 2022/9/14 17:23
    */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
