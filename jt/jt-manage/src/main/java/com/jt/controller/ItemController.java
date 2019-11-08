package com.jt.controller;


import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item/")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("query")
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		return itemService.findItemByPage(page, rows);
	}
	
	/**
	 * 新增商品
	 * url:http://localhost:8091/item/save
	 * 参数id=1&title=112312
	 * 返回值:SysResult(200/201)
	 */
	@RequestMapping("save")
	public SysResult saveItem(Item item,ItemDesc itemDesc){
		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}
	
	/**
	 * 修改商品
	 * 一般通过主键修改商品
	 * url:http://localhost:8091/item/update
	 */
	@RequestMapping("update")
	public SysResult update(Item item,ItemDesc itemDesc) {
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	
	/**
	 * 删除商品
	 * url:http://localhost:8091/item/delete
	 */
	@RequestMapping("delete")
	public SysResult delete(Long[] ids) {
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	
	@RequestMapping("instock")
	public SysResult instock(Long[] ids) {
		int status=2;
		itemService.updateStatus(status,ids);
		return SysResult.success();
	}
	
	@RequestMapping("reshelf")
	public SysResult reshelf(Long[] ids) {
		int status=1;
		itemService.updateStatus(status,ids);
		return SysResult.success();
	}

    /**
     * 实现商品详情信息查询
     * url:http://localhost/item/query/item/desc/1352516
     */
    @RequestMapping("query/item/desc/{itemId}")
    public SysResult findItemDescById(@PathVariable Long itemId){
        ItemDesc itemDesc = itemService.findItemDescById(itemId);
        return SysResult.success(itemDesc);
    }
}
