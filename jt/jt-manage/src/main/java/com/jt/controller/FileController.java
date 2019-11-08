package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.jt.service.FileService;
import com.jt.vo.ImageVo;

import java.io.File;

@RestController
@RequestMapping("/")
public class FileController {
    @Autowired
    private FileService fileService;

    /**
     * 文件上传成功之后,返回JSON数据
     */
    @RequestMapping("file")
    public String file(MultipartFile fileImage) throws Exception {
        //1.获取图片名称
        String fileName = fileImage.getOriginalFilename();
        //2.创建文件对象,指定上传的目录
        File dir=new File("E:/com.jt-software/com.jt-images");
        if (!dir.exists()) dir.mkdirs();//如果文件不存在,创建目录
        String path="E:/com.jt-software/com.jt-images/"+fileName;
        File file=new File(path);
        //3.利用工具API执行输出动作
        fileImage.transferTo(file);
        return "文件上传成功";
    }

    @RequestMapping("/pic/upload")
    public ImageVo fileUpload(MultipartFile uploadFile){
        return fileService.upload(uploadFile);
    }
}
