package com.gao.happying_shop_system.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/10/12 15:35
 * @Description:自定义异常类
 */
public class ServiceException extends RuntimeException{
    public ServiceException(String message){
        super(message);
    }
}
