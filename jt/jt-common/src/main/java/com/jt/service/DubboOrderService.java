package com.jt.service;

import com.jt.pojo.Order;

public interface DubboOrderService {

    Order saveOrder(Order order);

    Order findOrderById(String  id);
}
