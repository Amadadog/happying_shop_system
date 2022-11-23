package com.gao.happying_shop_system.filter;

import com.alibaba.fastjson.JSON;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/09/12 13:16
 * @Description:检查用户是否已经完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取此次请求的url
        String requestUrl = request.getRequestURI();
        log.info("此次的请求为" + requestUrl);

        //不需要拦截的请求路径
        String[] urls = new String[]{
                "/happying_shop_system/employee/login",
                "/happying_shop_system/employee/logout",
                "/backend/**",
                "/front/**",
                "/universal/**",
                "/user/sendMailCode",
                "/user/loginByCode",
                "/user/loginByUser"
        };

        //2.判断此次请求是否需要处理
        boolean check = check(urls, requestUrl);
        log.info("" + check);
        //3.如果不需要处理，放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }

        //4-1.判断登录状态，如果已经登录，则放行
        if(request.getSession().getAttribute("employeeId") != null) {
            log.info("用户已登录，用户id为:{}",request.getSession().getAttribute("employeeId"));
            Long empId = (Long) request.getSession().getAttribute("employeeId");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }
        //4-2.判断登录状态，如果已经登录，则放行
        if(request.getSession().getAttribute("userId") != null) {
            log.info("用户已登录，用户id为:{}",request.getSession().getAttribute("userId"));
            Long userId = (Long) request.getSession().getAttribute("userId");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        //5.如果没有登录，返回界面，并通过输出流响应未登录结果的响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * @Description: 检查此次请求是否需要放行
     * @Param: [urls, requestURI]
     * @return: boolean
     * @Author: GaoWenQiang
     * @Date: 2022/9/12 13:33
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
