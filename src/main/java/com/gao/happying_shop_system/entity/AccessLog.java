package com.gao.happying_shop_system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2023/03/18 16:55
 * @Description:
 */

@Data
public class AccessLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String requestUrl;

    private String requestMethod;

    private String requestIp;

    private Long requestTime;

    private String requestType;

    private LocalDateTime CreateTime;
}
