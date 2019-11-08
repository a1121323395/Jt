package com.jt.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import com.jt.service.DubboOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DubboOrderServiceImpl implements DubboOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;

    @Override
    @Transactional
    public Order saveOrder(Order order) {
        //订单信息入库
        //动态生成订单id（用户ID+当前毫秒值）
        String orderId = order.getUserId().toString() + System.currentTimeMillis();
        order.setOrderId(orderId);//设置订单号
        order.setStatus(1).//设置状态
                setCreated(new Date()).//设置创建时间
                setUpdated(order.getCreated());//设置修改时间
        orderMapper.insert(order);
        //订单物流信息入库
        saveOrderShipping(order);
        //订单商品信息入库
        saveOrderItems(order);
        return order;
    }

    /*订单物流信息入库*/
    public void saveOrderShipping(Order order) {
        OrderShipping orderShipping = order.getOrderShipping();//获取订单物流信息
        if (orderShipping != null) {
            orderShipping.setOrderId(order.getOrderId())//设置订单ID
                    .setCreated(new Date())//设置创建时间
                    .setUpdated(orderShipping.getCreated());//设置修改时间
            orderShippingMapper.insert(orderShipping);
        }
    }

    /*订单商品信息入库*/
    private void saveOrderItems(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();//获取所有订单商品信息
        for (OrderItem orderItem : orderItems) {//遍历商品信息
            if (orderItem != null) {
                orderItem.setOrderId(order.getOrderId())//设置订单ID
                        .setCreated(new Date())//设置创建时间
                        .setUpdated(orderItem.getCreated());//设置修改时间
                orderItemMapper.insert(orderItem);
            }
        }
    }

    @Override
    public Order findOrderById(String id) {
        Order order = orderMapper.selectById(id);
        OrderShipping orderShipping = orderShippingMapper.selectById(id);
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",id);
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
        order.setOrderShipping(orderShipping).setOrderItems(orderItems);
        return order;
        //return orderMapper.findOrderById(id);
    }
}
