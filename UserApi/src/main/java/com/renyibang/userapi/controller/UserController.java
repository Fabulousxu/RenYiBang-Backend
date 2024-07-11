package com.renyibang.userapi.controller;

import com.renyibang.global.dto.UserDTO;
import com.renyibang.global.util.Response;
import com.renyibang.userapi.dto.Update;
import com.renyibang.userapi.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
  @Autowired private UserService userService;

  @GetMapping("/self")
  public Response getSelfInfo() {
    long userId = 1;
    return userService.getUserInfo(userId);
  }

  @GetMapping("/{userId}")
  public Response getUserInfo(@PathVariable long userId) {
    return userService.getUserInfo(userId);
  }

  @PostMapping("/self/update")
  public Response updateUserInfo(@RequestBody Update update) {
    long userId = 1;
    return userService.updateUserInfo(userId, update);
  }

  // 以下为对外模块接口

  @GetMapping("/profile/{userId}")
  public Response getUserInfo_compatible(@PathVariable long userId) {
    return userService.getUserInfo(userId);
  }
  
  @GetMapping("/{userId}/exist")
  boolean getUserExist(@PathVariable long userId) {
    return userService.existsById(userId);
  }

  @GetMapping("/profile/users/{userIds}")
  public Response getUserInfos(@PathVariable List<Long> userIds) {
    return userService.getUserInfos(userIds);
  }

  @PostMapping("/update")
  public Response updateUserInfo(@RequestBody UserDTO userDTO) {
    return userService.updateUserInfo_compatible(userDTO);
  }
}
