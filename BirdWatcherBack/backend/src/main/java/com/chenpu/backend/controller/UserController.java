package com.chenpu.backend.controller;

import com.chenpu.backend.service.servicelmpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.chenpu.backend.service.UserService;
import com.chenpu.backend.domain.ResultObject;
import com.chenpu.backend.domain.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public ResultObject getUserByName(@RequestParam String username) {
        System.out.println(username);
        User user = userService.getUserByName(username);
        ResultObject resultObject;
        if(user != null) {
            resultObject = new ResultObject(200, "success", user);
        }else{
            resultObject = new ResultObject(401, "fail", null);
        }
        return resultObject;
    }

    @GetMapping("/email")
    public ResultObject getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        ResultObject resultObject;
        if(user != null){
            //已经存在该邮箱，返回错误信息
            resultObject = new ResultObject(401, "fail", user);
        }else {
            //不存在该邮箱，返回正确
            resultObject = new ResultObject(200, "success", null);
        }
        return resultObject;
    }

    @PostMapping("/add")
    public ResultObject insertUser(@RequestBody User user){
        /*User user =new User();
        user.setPass(password);
        user.setEmail(email);
        user.setName(username);*/
        System.out.println(user);
        int modifyid = userService.insertUser(user);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("modifyId", modifyid);
        ResultObject resultObject = new ResultObject(0, "success", map);
        return resultObject;
    }
}
