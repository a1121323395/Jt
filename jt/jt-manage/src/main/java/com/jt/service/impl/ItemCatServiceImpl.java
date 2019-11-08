package com.jt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public ItemCat findItemNameById(Integer itemId) {
        return itemCatMapper.findNameById(itemId);
    }

    @Override
    public List<EasyUITree> findEasyUITree(Long parentId) {
        List<EasyUITree> treeList = new ArrayList<>();
        for (ItemCat itemCat : findItemCatByParentId(parentId))
            treeList.add(new EasyUITree(itemCat.getId(),itemCat.getName(),itemCat.getIsParent() ? "closed" : "open"));//如果是父级closed 否则open
        System.out.println("从数据库获取数据");
        return treeList;
    }

    public List<ItemCat> findItemCatByParentId(Long parentId) {
        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId);
        return itemCatMapper.selectList(queryWrapper);
    }
}
