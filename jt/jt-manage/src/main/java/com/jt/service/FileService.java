package com.jt.service;

import com.jt.vo.ImageVo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    ImageVo upload(MultipartFile uploadFile);
}
