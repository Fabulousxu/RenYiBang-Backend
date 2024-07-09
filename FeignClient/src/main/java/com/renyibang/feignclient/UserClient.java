package com.renyibang.feignclient;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "UserApi")
public interface UserClient {
  @GetMapping("/user/exist")
  boolean getUserExist(@RequestParam("id") long userId);

  @GetMapping("/user/userJSON")
  JSONObject getUserJSON(@RequestParam("id") long userId);

  @GetMapping("/api/user/{userId}/info")
  JSONObject getUserInfo(@PathVariable long userId);

  /////////Order模块的调用/////////
 /*
  public class UserDTO {
    long id;
    long balance;
    long nickname;
    String avatar;
  }
  */

  @GetMapping("/user/{id}")
  Object getUserById(@PathVariable("id") Long id);

  @PostMapping("/user/update")
  void updateUser(@RequestBody Object userDTO);
  //////////////////////////
}
