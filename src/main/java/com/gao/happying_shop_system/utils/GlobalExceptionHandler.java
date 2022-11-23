package com.gao.happying_shop_system.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/09/12 16:00
 * @Description:
 */
//annotaions指定注解，哪些类加了指定注解，都会被拦截
@RestControllerAdvice(annotations = {RestController.class})
@Slf4j
public class GlobalExceptionHandler {
    /**
    * @Description: sql异常处理方法
    * @Param: []
    * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
    * @Author: GaoWenQiang
    * @Date: 2022/9/12 16:06
    */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error(exception.getMessage());
        if (exception.getMessage().contains("Duplicate entry")){
            String[] spit = exception.getMessage().split(" ");
            String msg = spit[2] + "已存在";
            return R.error(msg);
        }
        return  R.error("未知异常");
    }
    /**
    * @Description:
    * @Param: [exception]
    * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
    * @Author: GaoWenQiang
    * @Date: 2022/10/12 15:40
    */
    @ExceptionHandler(ServiceException.class)
    public R<String> exceptionHandler(ServiceException exception) {
        log.error(exception.getMessage());
        return  R.error(exception.getMessage());
    }
}
