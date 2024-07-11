package com.renyibang.global.client;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@FeignClient(name = "UserApi")
public interface UserClient {
  @GetMapping("/api/user/{userId}/exist")
  boolean getUserExist(@PathVariable long userId);

  @GetMapping("/api/user/profile/{userId}")
  JSONObject getUserInfo(@PathVariable long userId);

  @GetMapping("/api/user/profile/users/{userIds}")
  JSONObject getUserInfos(@PathVariable Collection<Long> userIds);

  /////////Order模块的调用/////////
 /*
  public class UserDTO {
    long id;
    long balance;
    long nickname;
    String avatar;
  }
  */

  @GetMapping("/api/user/{userId}")
  JSONObject getUserById(@PathVariable("userId") Long id);

  @PostMapping("/api/user/update")
  JSONObject updateUser(@RequestBody UserDTO userDTO);
  //////////////////////////
}
