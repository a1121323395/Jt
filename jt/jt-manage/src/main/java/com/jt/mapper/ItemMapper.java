package com.jt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemMapper extends BaseMapper<Item>{
	
	@Select("select * from tb_item order by updated desc limit #{start},#{rows}")
	List<Item>findItemByPage(
            @Param("start") Integer start,
            @Param("rows") Integer rows);

}
