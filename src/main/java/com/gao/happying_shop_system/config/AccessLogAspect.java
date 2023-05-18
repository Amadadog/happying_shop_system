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

    @Around("webLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
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