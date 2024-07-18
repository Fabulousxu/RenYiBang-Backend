package com.renyibang.global.client;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.dto.UserDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "UserApi")
public interface UserClient {
  @GetMapping("/api/user/{userId}/exist")
  boolean getUserExist(@PathVariable long userId);

  @GetMapping("/api/user/{userId}")
  JSONObject getUserInfo(@PathVariable long userId);

  @GetMapping("/api/user/{userId}/dto")
  JSONObject getUserById(@PathVariable long userId);

  @GetMapping("/api/user/list/{userIds}")
  JSONObject getUserInfos(@PathVariable List<Long> userIds);

  @PostMapping("/api/user/update")
  JSONObject updateUser(@RequestBody UserDTO userDTO);
}
