package com.gao.happying_shop_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gao.happying_shop_system.entity.ShoppingCart;
import com.gao.happying_shop_system.utils.BaseContext;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.User;
import com.gao.happying_shop_system.mapper.UserMapper;
import com.gao.happying_shop_system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/11 16:38
 * @Description:
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public R<String> loginByUser(User user, HttpSession httpSession) {
        //1、将页面提交的密码password进行md5加密处理
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, user.getEmail());
        User user1 = this.getOne(userLambdaQueryWrapper);

        //没有查到该用户
        if (user1 == null) {
            return R.error("用户名不存在，登录失败");
        }

        //对比密码
        if (!user1.getPassword().equals(password)) {
            return R.error("密码错误，登录失败");
        }

        //对比状态
        if (user1.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        httpSession.setAttribute("userId", user1.getId());
        return R.success("登录成功");
    }

    @Override
    public Boolean isExit(String email) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, email);
        User user = this.getOne(userLambdaQueryWrapper);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public R<String> registerByUser(User user) {
        try {
//            int count = 5;
//            String email1 = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, count - 1)) + ' ');
//            String email2 =   (int) ((Math.random() * 9 + 1) * Math.pow(10, count - 1)) + "@qq.com";
//            user.setEmail(email1+email2);
//            String email = prefix + "@qq.com";
//            log.info(email1+email2);
            String password = user.getPassword();
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            user.setPassword(password);
            user.setStatus(1);
//            user.setCreateTime(LocalDateTime.of(2023, 2, 13, 14, 5, 20));
//            user.setCreateTime(LocalDateTime.of(2023, 3, 13, 14, 8, 24));
            user.setCreateTime(LocalDateTime.now());
            redisTemplate.opsForValue().get(user.getEmail());
//            if (redisTemplate.opsForValue().get(user.getEmail()) == null) {
//                return R.error("验证码已过期");
//            } else if (!redisTemplate.opsForValue().get(user.getEmail()).equals(user.getCode())) {
//                return R.error("验证码错误");
//            } else {
                //如果用户登录成功，删除redis 中的验证码
                redisTemplate.delete(user.getEmail());
                this.save(user);
                return R.success("注册成功");
//            }
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return R.error("注册失败");
        }
    }
}
