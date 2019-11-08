package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageVo {
    private Integer error=0;//0没错,1有错
    private String url;//图片路径
    private Integer height;
    private Integer width;

    public static ImageVo fail(){
        return new ImageVo(1,null,null,null);
    }
}
