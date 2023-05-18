package com.gao.happying_shop_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gao.happying_shop_system.utils.R;
import com.gao.happying_shop_system.entity.User;
import com.gao.happying_shop_system.service.IUserService;
import com.gao.happying_shop_system.utils.QQEmail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/sendMailCode")
    public R<String> sendMailCode(@RequestBody User user, HttpSession httpSession) throws MessagingException {
        String email = user.getEmail();
        if (StringUtils.isNotEmpty(email)) {
            //生成count位的验证码
            int count = 6;
            String code = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, count - 1)));
            QQEmail.send_mail(user.getEmail(), "本次服务的激活码为： " + code);

            //保存到session
//            httpSession.setAttribute(email,code);
            
            //将验证码存入到Redis，并设置过期时间5min
            redisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);
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
    /**
    * @description: 使用账号密码登录
    * @param: [user, httpSession]
    * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
    * @author: GaoWenQiang
    * @date: 2023/1/31 16:12
    */
    @PostMapping("/loginByUser")
    public R<String> loginByUser(@RequestBody User user, HttpSession httpSession){
        return userService.loginByUser(user,httpSession);
    }
    /**
    * @description: 用户注册
    * @param: [user, httpSession]
    * @return: com.gao.happying_shop_system.utils.R<java.lang.String>
    * @author: GaoWenQiang
    * @date: 2023/3/26 15:08
    */
    @PostMapping("/register")
    public R<String> registerByUser(@RequestBody User user){
        //邮箱号
        String email = user.getEmail();
//        log.info(email);
        //验证码
        String code = user.getCode();
        //从redis获取验证码
        String redisCode = redisTemplate.opsForValue().get(email);

        if (!code.equals(redisCode)) {
            return R.error("验证码错误!!");
        }
        if (userService.isExit(user.getEmail())) {
            return R.error("用户已存在");
        }
        //验证码过期
        if (redisCode == null) {
            return R.error("验证码过期");
        }

//        for (int i = 0; i < 100; i++) {
//            user.setId(null); // 清空ID
//            userService.registerByUser(user);
//            sqlSession.clearCache();// 清空 Mybatis-plus 内部缓存
//        }
        return userService.registerByUser(user);
    }
    /****
    * @description: 根据用户邮箱查询用户信息
    * @param:
    * @return:
    * @author: GaoWenQiang
    * @date: 2023/5/14 16:59
    */
    @GetMapping("/getUserByEmail/{email}")
    public R<User> getUserByEmail( @PathVariable("email") String email){
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail,email);
        User user = userService.getOne(userLambdaQueryWrapper);
        return R.success(user);
    }
}
