package com.chenpu.backend.mapper;

import com.chenpu.backend.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user (name, pass, email) VALUES(#{name}, #{pass}, #{email})")
    int insert(User user);

    @Select("SELECT name, pass, email FROM user WHERE name = #{username}")
    User findByName(String username);

    @Select("SELECT name, pass, email FROM user")
    List<User> findAllUsers();

    @Select("SELECT name, pass, email FROM user WHERE name = #{email}")
    User findByEmail(String email);
}
