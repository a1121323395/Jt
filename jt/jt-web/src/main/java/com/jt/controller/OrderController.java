package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Reference(check = false)
    private DubboCartService cartService;
    @Reference(check = false)
    private DubboOrderService orderService;

    /**
     * 实现订单确认页面的跳转
     * @return
     */
    @RequestMapping("/create")
    public String create(Model model) {
        Long userId = ThreadLocalUtil.get().getId();
        List<Cart> carts = cartService.findCartListByUserId(userId);
        model.addAttribute("carts",carts);
        return "order-cart";
    }

    @ResponseBody
    @RequestMapping("/submit")
    public SysResult saveOrder(Order order) {
        if (order == null)
            return SysResult.fail();
        order.setUserId(ThreadLocalUtil.get().getId());
        Order saveOrder = orderService.saveOrder(order);
        cartService.deleteCartByUserId(ThreadLocalUtil.get().getId());
        return SysResult.success(saveOrder.getOrderId());
    }

    @RequestMapping("/success")
    public String findOrderById(String id,Model model) {
        Order order = orderService.findOrderById(id);
        model.addAttribute("order", order);
        return "success";
    }

    @RequestMapping("myOrder")
    public String myOrder(){
        return "my-orders";
    }
}
