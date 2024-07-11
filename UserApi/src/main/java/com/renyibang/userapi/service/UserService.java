package com.renyibang.userapi.service;

import com.alibaba.fastjson2.JSONObject;

import com.renyibang.global.dto.UserDTO;
import com.renyibang.userapi.entity.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;



public interface UserService {
    Optional<User> findById(long userId);
    User save(User user);
    void deleteById(long userId);
    long register(String password, String nickname, String intro, String avatar) throws Exception;

    JSONObject getUserInfo(long userId);

    JSONObject modifyUserInfo(long userId, JSONObject body);

    JSONObject getUserDtoById(long userId);

    JSONObject updateUserByDto(UserDTO userDTO);

    JSONObject getUserInfos(Collection<Long> userIds);
}
