package com.gao.happying_shop_system.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/11/11 14:43
 * @Description:
 */
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class QQEmail {
    public static void send_mail(String toPepole, String text) throws MessagingException {
        //创建连接对象 连接到邮件服务器
        Properties properties = new Properties();
        //设置发送邮件的基本参数
        //发送邮件服务器
        properties.put("mail.smtp.host", "smtp.qq.com");
        //发送端口
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");


        //设置发送邮件的账号和密码
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//                return new javax.mail.PasswordAuthentication("你的QQ邮箱", "你的qq邮箱授权码");
                return new javax.mail.PasswordAuthentication("1316591052@qq.com", "metehbvjslecfhce");
            }
        });

        //创建邮件对象
        Message message = new MimeMessage(session);
        //设置发件人
        try {
            message.setFrom(new InternetAddress("1316591052@qq.com"));
            //设置收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toPepole));
            //设置主题
            message.setSubject("Please verify your device");
            message.setContent(text, "text/html;charset=UTF-8");
            Transport.send(message);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }
    }

}