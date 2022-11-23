package com.gao.happying_shop_system.controller;

import com.gao.happying_shop_system.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/10/13 14:26
 * @Description:通用controller
 */
@RestController
@Slf4j
@RequestMapping("/universal")
public class UniversalController {
    @Value("${happying_shop_system.path}")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(@RequestPart("file") MultipartFile uploadFile){
        //file是一个临时文件，需要转存到扮定位置，否则本次请求完成后临时文件会郦陲
        log.info(uploadFile.toString());

        //原始文件名
        String originalFilename = uploadFile.getOriginalFilename();

        //后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用时间戳作新文件名，以防文件名重复造成文件覆盖
        String fileName = String.valueOf(System.currentTimeMillis()) + suffix;

        //创建目录对象
        File file = new File(basePath);
        //判断当前目录是否存在
        if (!file.isFile()) {
            file.mkdir();
        }
        try {
            //临时文件转存到指定文件
            uploadFile.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(fileName);
        return R.success(fileName);
    }
    /**
    * @Description: 文件下载有两种方式：1.附件下载 2.写出到浏览器 注：此处因要回显数据，故用第二种方式
    * @Param: [name, response]
    * @return: void
    * @Author: GaoWenQiang
    * @Date: 2022/10/13 17:04
    */
    @GetMapping("/download")
    public void download(@RequestParam("name") String fileName, HttpServletResponse response) throws FileNotFoundException {
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + fileName));

            //输出流，将文件写回浏览器响应体，响应给浏览器展示
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            //读到-1表示读完
            while((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //回收资源
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
