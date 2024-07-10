package com.renyibang.userapi.dao;
import com.renyibang.userapi.entity.UserAuth;
import java.util.Optional;
public interface UserAuthDAO {
    Optional<UserAuth> findByUserId(long userId);
    UserAuth save(UserAuth userAuth);
    void deleteByUserId(long userId);
}
