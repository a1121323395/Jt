package com.jt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestJson {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * 1.对象转化为JSON
     */
    @Test
    public void toJSON() throws IOException {
        ItemDesc itemDesc =new ItemDesc();
        itemDesc.setItemId(100L).setItemDesc("商品详情").setCreated(new Date()).setUpdated(itemDesc.getCreated());
        String json=MAPPER.writeValueAsString(itemDesc);
        System.out.println(json);

        //将json转化为对象
        ItemDesc desc =MAPPER.readValue(json,ItemDesc.class );
        System.out.println(desc);
    }

    @SuppressWarnings("unchecked")
	@Test
    public void testList() throws IOException {
        ItemDesc itemDesc1 =new ItemDesc();
        itemDesc1.setItemId(100L).setItemDesc("商品详情").setCreated(new Date()).setUpdated(itemDesc1.getCreated());
        ItemDesc itemDesc2 =new ItemDesc();
        itemDesc2.setItemId(100L).setItemDesc("商品详情").setCreated(new Date()).setUpdated(itemDesc2.getCreated());
        List<ItemDesc> list=new ArrayList<ItemDesc>();
        list.add(itemDesc1);
        list.add(itemDesc2);
        String json = MAPPER.writeValueAsString(list);
        System.out.println(json);
        List<ItemDesc> list1 = MAPPER.readValue(json,list.getClass());
        System.out.println(list1);
    }
}
