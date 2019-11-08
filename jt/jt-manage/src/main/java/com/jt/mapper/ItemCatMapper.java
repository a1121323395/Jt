package com.jt.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.ItemCat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ItemCatMapper extends BaseMapper<ItemCat> {
	@Select("select * from tb_item_cat where id=#{itemId}")
	ItemCat findNameById(@Param("itemId") Integer itemId);
}
