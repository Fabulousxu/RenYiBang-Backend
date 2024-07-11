package com.renyibang.userapi.dao;



import com.alibaba.fastjson2.JSONObject;
import com.renyibang.userapi.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserDao {
  User save(User user);
  void deleteById(long userId);
  Optional<User> findById(long userId);

  String updateUserByUserId(long userId, String nickname, String avatar, String intro);

  JSONObject findAllById(Collection<Long> userIds);
}
