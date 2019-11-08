package com.jt.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class DubboCartServiceImpl implements DubboCartService {
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}

	/**
	 * sql: update tb_cart set num=#{num}, updated=#{updated} where
	 * user_id=#{usreId} and item_id=#{itemId}
	 * 
	 * @param cart 要修改的数据
	 */
	@Override
	public void updateCartNum(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("item_id", cart.getItemId()).eq("user_id", cart.getUserId());
		cartMapper.update(cartTemp, updateWrapper);
	}

	@Override
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
		cartMapper.delete(queryWrapper);
	}

	@Override
	public void saveCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
		queryWrapper.eq("item_id", cart.getItemId()).eq("user_id", cart.getUserId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if (cartDB == null) {
			cart.setCreated(new Date()).setUpdated(cart.getCreated());
			cartMapper.insert(cart);// 用户第一次新增
		} else {
			int num = cart.getNum() + cartDB.getNum();
			 //cartDB.setNum(num).setUpdated(new Date());
            Cart cartTemp=new Cart();
            cartTemp.setId(cartDB.getId()).setNum(num).setUpdated(new Date());
            cartMapper.updateById(cartTemp);
		}
	}

	@Override
	public void deleteCartByUserId(Long id) {
	    QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
	    queryWrapper.eq("user_id",id );
		cartMapper.delete(queryWrapper);
	}
}
