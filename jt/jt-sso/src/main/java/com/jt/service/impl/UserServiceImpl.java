package com.jt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean checkUser(String param,Integer type) {
        String[] array={"username","phone","email"};
        String colum = array[type-1];//获取校验类型
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(colum, param);//定义查询条件
        User user = userMapper.selectOne(queryWrapper);
        return user != null;//如果有数据则为true，否则为false
    }
}
