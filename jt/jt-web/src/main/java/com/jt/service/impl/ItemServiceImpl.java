package com.jt.service.impl;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private HttpClientService httpClientService;

    @Override
    public Item findItemById(Long itemid) {
        String url = "http://manage.jt.com/web/item/findItemById";
        Map<String, String> params = new HashMap<>();
        params.put("id",itemid + "");
        String result = httpClientService.doGet(url,params);
        return ObjectMapperUtil.toObject(result,Item.class );
    }

    @Override
    public ItemDesc findItemDescById(Long itemid) {
        String url = "http://manage.jt.com/web/item/findItemDescById";
        Map<String, String> params = new HashMap<>();
        params.put("id",itemid + "");
        String result = httpClientService.doGet(url,params);
        return ObjectMapperUtil.toObject(result,ItemDesc.class );
    }
}
