package com.renyibang.userapi.dao;



import com.renyibang.userapi.entity.User;

import java.util.Optional;

public interface UserDao {
  User save(User user);
  void deleteById(long userId);
  Optional<User> findById(long userId);

  String updateUserByUserId(long userId, String nickname, String avatar, String intro);
}
