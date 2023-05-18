package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gao.happying_shop_system.entity.Employee;
import com.gao.happying_shop_system.mapper.EmployeeMapper;
import com.gao.happying_shop_system.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author GaoWenQiang
 * @since 2022-09-11
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public R<Employee> login(HttpServletRequest request,Employee employee) {
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeMapper.selectOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("用户名不存在，登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误，登录失败");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employeeId",emp.getId());
        return R.success(emp);
    }

    @Override
    public R<String> logout(HttpServletRequest request) {
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employeeId");
        return R.success("退出成功");
    }

    @Override
    public R<String> save(HttpServletRequest request, Employee employee) {
        //设置默认密码123456，md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employeeId");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        int insert = employeeMapper.insert(employee);
        if (insert == 0) {
            return R.error("新增员工失败");
        }
        return R.success("新增员工成功");
    }

    @Override
    public R<String> checkUsername(String username) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,username);
        Employee emp = employeeMapper.selectOne(queryWrapper);
        if(emp == null){
            return R.success("用户名可以使用");
        }
        return R.error("用户名已存在");
    }

    @Override
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page pageInfo = new Page<>(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        //添加过滤条件
        queryWrapper.like(StringUtils.hasText(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeMapper.selectPage(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    @Override
    public R<String> updateEmployee(HttpServletRequest request,Employee employee) {
        log.info(employee.toString());
//        Long empId = (Long) request.getSession().getAttribute("employeeId");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeMapper.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @Override
    public R<Employee> getById(Long id) {
        Employee employee =employeeMapper.selectById(id);
        if ( employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
