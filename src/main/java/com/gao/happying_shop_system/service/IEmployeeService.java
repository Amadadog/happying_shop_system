package com.gao.happying_shop_system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gao.happying_shop_system.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gao.happying_shop_system.utils.R;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 员工信息 服务类
 * </p>
 *
 * @author GaoWenQiang
 * @since 2022-09-11
 */
public interface IEmployeeService extends IService<Employee> {
    public R<Employee> login(HttpServletRequest request, Employee employee);
    public R<String> logout(HttpServletRequest request);
    public R<String> save(HttpServletRequest request,Employee employee);
    public R<String> checkUsername(String username);
    public R<Page> page(int page, int pageSize, String name);
    public R<String> updateEmployee(HttpServletRequest request,Employee employee);
    public R<Employee> getById(@PathVariable Long id);
}
