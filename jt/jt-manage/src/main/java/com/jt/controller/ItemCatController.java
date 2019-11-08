package com.jt.controller;

import com.jt.annotation.AddJedis;
import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;

	/**
	 * 根据商品分类id查询商品名称
	 * /item/cat/queryItemName
	 * @param itemCatId
	 * @return
	 */
	@RequestMapping("/queryItemName")
	public String findItemNameById(Integer itemCatId) {
		ItemCat itemCat = itemCatService.findItemNameById(itemCatId);
		System.out.println(itemCat);
		return itemCat.getName();
	}

	/**
	 * 1.url:/item/cat/list
	 * 2.返回值结果  List<EasyUITree>
	 * 业务思想:
	 * 	只查询一级商品分类信息
	 * 	parent_id=0
	 * 
	 * SpringMVC动态接收数据
	 * @RequestParam (value 接收的参数名,defaultValue 默认值,name 与value一样,required 是否必填)
	 */
	@AddJedis
	@RequestMapping("/list")
	public List<EasyUITree> findItemCatByParentId(@RequestParam(value = "id",defaultValue = "0") Long parentId){
        return itemCatService.findEasyUITree(parentId);
	}
}
