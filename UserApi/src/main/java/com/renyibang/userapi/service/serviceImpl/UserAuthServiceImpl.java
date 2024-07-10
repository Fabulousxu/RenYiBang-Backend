package com.renyibang.userapi.service.serviceImpl;


import com.renyibang.userapi.dao.UserAuthDAO;
import com.renyibang.userapi.entity.UserAuth;
import com.renyibang.userapi.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private UserAuthDAO userAuthDAO;

    @Override
    public Optional<UserAuth> findByUserId(long userId) {
        return userAuthDAO.findByUserId(userId);
    }

    @Override
    public UserAuth save(UserAuth userAuth) {
        return userAuthDAO.save(userAuth);
    }

    @Override
    public void deleteByUserId(long userId) {
        userAuthDAO.deleteByUserId(userId);
    }
}
