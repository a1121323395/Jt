package com.jt.service;

import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;

import java.util.List;

public interface ItemCatService {

	ItemCat findItemNameById(Integer itemId);

	List<ItemCat> findItemCatByParentId(Long parent_id);

	List<EasyUITree> findEasyUITree(Long parentId);

}
