package com.gao.happying_shop_system.config;

import com.gao.happying_shop_system.entity.AccessLog;
import com.gao.happying_shop_system.service.IAccessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2023/03/18 17:26
 * @Description:
 */
@Slf4j
@Aspect
@Component
public class AccessLogAspect {

    @Autowired
    private IAccessService accessService;

    @Autowired
    private HttpServletRequest request;

    @Pointcut("execution(public * com.gao.happying_shop_system.controller.*.*(..))")
    public void webLog() {
    }
    //Q: 为什么要用@Around?
    //A: @Around是在方法执行前后都会执行的方法，而@Before是在方法执行前执行的方法，@After是在方法执行后执行的方法。
    //   @Around可以用来做一些日志记录，如记录请求的URL、请求的IP、请求的方法、请求的参数等信息，也可以用来做一些权限校验。
    @Around("webLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //Q:下面三行代码是什么意思？
        //A:获取请求的开始时间，执行目标方法，获取请求的结束时间，计算请求的耗时。
        //Q：那我这里的环绕通具体应用是在哪？
        //A：在这里，我们可以在目标方法执行前后做一些事情，比如记录请求的日志、记录请求的耗时、记录请求的参数等。
        //Q:@Aspect、@Pointcut("execution(public * com.gao.happying_shop_system.controller.*.*(..))")、@Around("webLog()")的作用分别是什么，有什么关联？
        //A:@Aspect是用来标注这是一个切面类，@Pointcut是用来定义切点的，@Around是用来定义环绕通知的，@Around("webLog()")中的webLog()就是切点。
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        AccessLog accessLog = new AccessLog();
        accessLog.setRequestTime(endTime - startTime);
        accessLog.setRequestIp(getIpAddress());
        accessLog.setRequestUrl(getRequestUrl());
        accessLog.setRequestMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        accessLog.setRequestType(request.getMethod());
        accessLog.setCreateTime(LocalDateTime.now());
        accessService.save(accessLog);

        return result;
    }

    /***
    * @description: 获取请求的URL
    * @param: []
    * @return: java.lang.String
    * @author: GaoWenQiang
    * @date: 2023/3/18 17:47
    */
    private String getRequestUrl() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String queryStr = request.getQueryString();
        if (StringUtils.isNotEmpty(queryStr)) {
            url = url + "?" + queryStr;
        }
        return url;
    }

    /***
    * @description: 获取请求的IP地址
    * @param: []
    * @return: java.lang.String
    * @author: GaoWenQiang
    * @date: 2023/3/18 17:47
    */
    private String getIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多IP地址情况
        if (StringUtils.isNotEmpty(ip) && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

}