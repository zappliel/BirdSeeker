package com.chenpu.backend.service;

import com.chenpu.backend.domain.User;
import com.chenpu.backend.domain.PageObject;

import org.springframework.stereotype.Service;


public interface UserService {
    public PageObject getAllUsers(int page_num,int page_size);

    public int insertUser(User user);

    public User getUserByName(String username);

    public User getUserByEmail(String email);
}
