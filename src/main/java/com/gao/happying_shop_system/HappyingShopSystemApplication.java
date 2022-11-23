package com.gao.happying_shop_system;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.gao.happying_shop_system.mapper")
@EnableTransactionManagement
public class HappyingShopSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappyingShopSystemApplication.class, args);
        log.info("客官，项目启动啦^_^");
    }

}
