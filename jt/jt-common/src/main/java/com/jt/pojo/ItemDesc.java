package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_item_desc")
public class ItemDesc extends BasePojo{
	private static final long serialVersionUID = 1016593866051306734L;
	//id必须与item的id一致.
    @TableId
    private Long itemId;
    private String itemDesc;

}
