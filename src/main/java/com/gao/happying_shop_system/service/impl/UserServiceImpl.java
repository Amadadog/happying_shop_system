package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.User;
import com.gao.happying_shop_system.mapper.UserMapper;
import com.gao.happying_shop_system.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/11 16:38
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public R<String> loginByUser(User user, HttpSession httpSession) {
        //1、将页面提交的密码password进行md5加密处理
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail,user.getEmail());
        User user1 = this.getOne(userLambdaQueryWrapper);

        //没有查到该用户
        if(user1 == null) {
            return R.error("用户名不存在，登录失败");
        }

        //对比密码
        if(!user1.getPassword().equals(password)){
            return R.error("密码错误，登录失败");
        }

        //对比状态
        if(user1.getStatus() == 0){
            return R.error("账号已禁用");
        }

        httpSession.setAttribute("userId",user1.getId());
        return R.success("登录成功");
    }
}
