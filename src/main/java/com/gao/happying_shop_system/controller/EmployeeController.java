package com.gao.happying_shop_system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gao.happying_shop_system.entity.Employee;
import com.gao.happying_shop_system.service.IEmployeeService;
import com.gao.happying_shop_system.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author GaoWenQiang
 * @since 2022-09-11
 */
@RestController
@RequestMapping("/happying_shop_system/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;


    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        return employeeService.login(request,employee);
    }


    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        return employeeService.logout(request);
    }


    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        return employeeService.save(request,employee);
    }

    @GetMapping("/checkUsername")
    public R<String> checkUsername(@RequestParam String username){
        return  employeeService.checkUsername(username);
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize, String name){
        return employeeService.page(page,pageSize,name);
    }

    @PutMapping
    public R<String> updateEmployee(HttpServletRequest request,@RequestBody Employee employee) {
        return employeeService.updateEmployee(request, employee);
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        return employeeService.getById(id);
    }
}
