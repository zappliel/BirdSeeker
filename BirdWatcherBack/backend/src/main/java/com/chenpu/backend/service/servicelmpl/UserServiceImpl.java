package com.chenpu.backend.service.servicelmpl;

import org.springframework.beans.factory.annotation.Autowired;
import com.chenpu.backend.mapper.UserMapper;
import com.chenpu.backend.service.UserService;
import com.chenpu.backend.domain.PageObject;
import com.chenpu.backend.domain.User;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageObject getAllUsers(int page_num,int page_size) {
        PageHelper.startPage(page_num, page_size);
        List<User> users = userMapper.findAllUsers();
        PageInfo<User> appsPageInfo = new PageInfo<User>(users);
        long total = appsPageInfo.getTotal();
        PageObject pageObject = new PageObject(users, page_num, page_size, total);
        return pageObject;
    }

    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    public User getUserByName(String username) {
        return userMapper.findByName(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.findByEmail(email);
    }
}