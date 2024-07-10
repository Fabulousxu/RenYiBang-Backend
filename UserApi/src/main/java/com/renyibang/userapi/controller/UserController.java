package com.renyibang.userapi.controller;

import com.alibaba.fastjson2.JSONObject;

import com.renyibang.feignclient.dto.UserDTO;
import com.renyibang.userapi.entity.RegisterRequest;
import com.renyibang.userapi.entity.User;
import com.renyibang.userapi.repository.UserRepository;
import com.renyibang.userapi.service.UserService;
import com.renyibang.userapi.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.Subject;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;



    @GetMapping("/register")
    public JSONObject register(@RequestBody RegisterRequest registerRequest) {
        System.out.println("register");

        String password = registerRequest.getPassword();
        String nickname = registerRequest.getNickname();
        String intro = registerRequest.getIntro();
        String avatar = registerRequest.getAvatar();
        try {
            long userId = userService.register(password, nickname, intro, avatar);
            JSONObject data = new JSONObject();
            data.put("userId", userId);
            return ResponseUtil.success("注册成功", data);
        } catch (Exception e) {
            return ResponseUtil.error("注册失败: " + e.getMessage());
        }
    }



    @GetMapping("/profile/self")
    public JSONObject getSelfInfo(long userId)
    {
        return userService.getUserInfo(userId);
    }

    @GetMapping("/profile/{userId}")
    public JSONObject getUserInfo(@PathVariable long userId)
    {
        return userService.getUserInfo(userId);
    }

    @PostMapping("/profile/modify/self")
    public JSONObject modifyUserInfo(long userId, @RequestBody JSONObject body)
    {
        return userService.modifyUserInfo(userId, body);
    }

    @GetMapping("/getUserById")
    public JSONObject getUserById(long userId)
    {
        // 返回 查找到的 User 对象，将对象直接封装成JSON返回
        return ResponseUtil.success("查找成功", userRepository.findById(userId).orElse(null).toJSON());
    }

    @GetMapping("/{userId}/exist")
    boolean getUserExist(@PathVariable long userId) {
        return userRepository.existsById(userId);
    }

    // order模块调用
    @GetMapping("/{userId}")
    public Object getUserDTOById(@PathVariable long userId)
    {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            return null;
        }
        return new UserDTO(user.getUserId(), user.getBalance(), user.getNickname(), user.getAvatar());
    }

    @PostMapping("/update")
    public JSONObject updateUser(@RequestBody UserDTO userDTO)
    {
        long userId = userDTO.getId();
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            return ResponseUtil.error("用户不存在");
        }
        user.setBalance(userDTO.getBalance());
        user.setNickname(userDTO.getNickname());
        user.setAvatar(userDTO.getAvatar());
        userRepository.save(user);
        return ResponseUtil.success("更新成功");
    }

    ////////////////
}
