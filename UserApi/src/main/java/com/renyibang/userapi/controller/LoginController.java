package com.renyibang.userapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.userapi.entity.LoginRequest;
import com.renyibang.userapi.service.UserService;
import com.renyibang.userapi.util.JwtUtil;
import com.renyibang.userapi.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public JSONObject login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = JwtUtil.createJWT(loginRequest.getName());
            JSONObject data = new JSONObject();
            data.put("jwt", jwt);

            return ResponseUtil.success("登录成功", data);
        } catch (Exception e) {
            return ResponseUtil.error("登录失败: " + e.getMessage());
        }
    }
}
