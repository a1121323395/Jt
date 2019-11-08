package com.jt.service.impl;

import com.jt.service.FileService;
import com.jt.vo.ImageVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")//加载pro配置文件
public class FileServiceImpl implements FileService {
    //文件存储根目录
    @Value("${image.localDirPath}")
    private String localDirPath; //= "E:/com.jt-software/com.jt-images";
    @Value("${image.urlDirpath}")
    private String urlDirpath;//="http://image.jt.com/";

    /**
     * 文件上传
     * 1.校验图片类型 JPG/JPEG/PNG/GIF
     * 2.校验是否为恶意程序
     * 3.分文件存储  yyyy/MM/dd
     * 4.防止文件重名 自定义文件名称
     */
    public ImageVo upload(MultipartFile uploadFile) {
        //获取图片名并转换为小写
        String fileName;
        fileName = Objects.requireNonNull(uploadFile.getOriginalFilename()).toLowerCase();
        //校验图片类型
        if (!fileName.matches("^.+\\.(jpg|png|gif)$"))//校验文件类型(JPG/PNG/GIF)
            return ImageVo.fail();
        //校验恶意程序 图片:高度/宽度(px)
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            int width = bufferedImage.getWidth();//获取宽度
            int height = bufferedImage.getHeight();//获取高度
            if (width == 0 || height == 0)
                return ImageVo.fail();
            //分文件存储(年/月/日)
            String dateDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());//获取当前日期并生成文件路径
            String dirFilePath = localDirPath + dateDir;//生成完整路径
            File dirFile = new File(dirFilePath);
            if (!dirFile.exists()) dirFile.mkdirs();//如果目录不存在,就创建文件
            String uuid = UUID.randomUUID().toString();//随机字符串
            String fileType = fileName.substring(fileName.lastIndexOf("."));//获取图片后缀名
            String realFileName = uuid + fileType;//生成图片的完整名称
            String realFilePath = dirFilePath + realFileName;//生成图片的完整路径
            uploadFile.transferTo(new File(realFilePath));//上传文件
            //实现数据的回显
            String url = urlDirpath+dateDir+realFileName;
            return new ImageVo(0,url,width,height);//发送成功信息
        } catch (Exception e) {
            e.printStackTrace();
            return ImageVo.fail();
        }
    }
}
