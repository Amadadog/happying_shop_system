package com.gao.happying_shop_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.User;
import com.gao.happying_shop_system.service.IUserService;
import com.gao.happying_shop_system.utils.QQEmail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/11 16:41
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/sendMailCode")
    public R<String> sendMailCode(@RequestBody User user, HttpSession httpSession) throws MessagingException {
        String email = user.getEmail();
        if (StringUtils.isNotEmpty(email)) {
            //生成count位的验证码
            int count = 6;
            String code = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, count - 1)));
            QQEmail.send_mail(user.getEmail(), "本次服务的激活码为： " + code);

            //保存到session
            httpSession.setAttribute(email,code);
            log.info(code + "发送成功");
            return R.success("邮箱发送成功");
        }
        return R.success("邮箱发送失败");
    }
    /**
    * @description: 使用验证码登录
    * @param: []
    * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
    * @author: GaoWenQiang
    * @date: 2022/11/15 10:20
    */
    @PostMapping("/loginByCode")
    public R<User> loginByCode(@RequestBody Map map, HttpSession httpSession){
        String email = map.get("email").toString();
        String code = map.get("code").toString();
        log.info(email + code);
        //从session中获取保存的验证码
        Object codeSession = httpSession.getAttribute(map.get("email").toString());
        if (codeSession != null && code.equals(codeSession.toString())) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getEmail,email);
            User user = userService.getOne(userLambdaQueryWrapper);
            //判断是否为新用户，是则自动完成注册
            if (user == null){
                 user = new User();
                 user.setEmail(email);
                 String  password = "123456";
                 password = DigestUtils.md5DigestAsHex(password.getBytes());
                 user.setPassword(password);
                 user.setStatus(1);
                 userService.save(user);
            }
            httpSession.setAttribute("userId",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }
    @PostMapping("/loginByUser")
    public R<String> loginByUser(@RequestBody User user, HttpSession httpSession){
        log.info(user.getEmail() + user.getPassword());
        return userService.loginByUser(user,httpSession);
    }
}
