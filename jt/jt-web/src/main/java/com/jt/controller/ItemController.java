package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/items/")
public class ItemController {
    @Autowired
    private ItemService itemService;

    //根据页面的url请求，跳转通用的商品展现页面
    @RequestMapping("{itemid}")
    public String findItemById(@PathVariable Long itemid,Model model){
        Item item=itemService.findItemById(itemid);
        ItemDesc itemDesc=itemService.findItemDescById(itemid);
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",itemDesc);
        return "item";//跳转到商品展现页面
    }
}
