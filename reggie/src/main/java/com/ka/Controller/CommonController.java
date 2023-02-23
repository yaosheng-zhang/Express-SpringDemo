package com.ka.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;


    @PostMapping
    public Result upload(@RequestParam("file") MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());
        //原始文件名 截取后缀前的文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //形成新的文件名
        String newFileName = UUID.randomUUID() + suffix;

        //创建一个目录对象
        File file1 = new File(basePath);
        //判断该目录是否存在
        if(!file1.exists())
        {
            file1.mkdirs();
        }
        //将临时文件的内容存放到指定位置
        try {
            file.transferTo(new File(basePath+newFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(newFileName);
    }
}
