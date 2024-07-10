package com.renyibang.userapi.service;

import com.renyibang.userapi.entity.UserAuth;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface UserAuthService {
    Optional<UserAuth> findByUserId(long userId);
    UserAuth save(UserAuth userAuth);
    void deleteByUserId(long userId);
}
