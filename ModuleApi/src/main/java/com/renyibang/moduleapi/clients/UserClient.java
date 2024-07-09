package com.renyibang.moduleapi.clients;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "UserApi")
public interface UserClient {
    @GetMapping("/user/exist")
    boolean getUserExist(@RequestParam("id") long userId);

    @GetMapping("/user/userJSON")
    JSONObject getUserJSON(@RequestParam("id") long userId);
}
