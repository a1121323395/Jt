package com.jt.service.impl;


import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Override
    public EasyUITable findItemByPage(Integer page,Integer rows) {
        // 获取商品的记录总数
        int total = itemMapper.selectCount(null);
        /*
          获取分页的数据
          sql:select * from tb_item limit arg1 起始页下标,arg2 查询条数
          第一页: select * from tb_item limit 0,20
          第二页: select * from tb_item limit 20,20
          第三页: select * from tb_item limit 40,20
          第N页: select * from tb_item limit (N-1)*rows,rows
         */
        int start = (page - 1) * rows;
        List<Item> itemList = itemMapper.findItemByPage(start,rows);
        return new EasyUITable(total,itemList);
    }

    @Override
    @Transactional//控制事务
    public void saveItem(Item item,ItemDesc itemDesc) {
        item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
        itemMapper.insert(item);

        //新增商品详情
        //数据库操作中主键自增之后都会自动的回填主键信息
        itemDesc.setItemId(item.getId()).setCreated(item.getCreated()).setUpdated(item.getCreated());
        itemDescMapper.insert(itemDesc);
    }

    @Override
    public void updateItem(Item item,ItemDesc itemDesc) {
        item.setUpdated(new Date());
        itemDesc.setItemId(item.getId()).setUpdated(new Date());
        itemDescMapper.updateById(itemDesc);
        itemMapper.updateById(item);
    }

    @Override
    public void deleteItems(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        itemMapper.deleteBatchIds(idList);
        itemDescMapper.deleteBatchIds(idList);
    }

    @Override
    public void updateStatus(int status,Long[] ids) {
        for (Long id : ids)
            itemMapper.updateById((Item) new Item().setId(id).setStatus(status).setUpdated(new Date()));
    }

    @Override
    public ItemDesc findItemDescById(Long itemId) {
        return itemDescMapper.selectById(itemId);
    }

    @Override
    public Item findItemById(Long id) {
        return itemMapper.selectById(id);
    }
}
