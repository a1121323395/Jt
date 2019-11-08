package com.jt.conf;

import java.util.Calendar;
import java.util.Date;

import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;


//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 当用户订单提交30分钟后,如果还没有支付.则交易关闭
     * 目的:每隔一分钟将超时订单状态修改1为6
     * 条件:now - created >30分钟 status=1
     * SQL:update tb_order set status=6,updated=#{date}
     * where status=1 and created < {now-30分钟}
     */
    @Override
    @Transactional
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //获取当前时间的日历对象
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,-30);
        Date timeout = calendar.getTime();
        Order order = new Order();
        order.setStatus(6).setUpdated(new Date());
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("status",1).lt("created",timeout);
        orderMapper.update(order,updateWrapper);
        System.out.println("定时任务执行成功");
    }
}
