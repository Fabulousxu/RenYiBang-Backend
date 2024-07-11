package com.renyibang.userapi.dao.daoImpl;

import com.renyibang.userapi.dao.UserAuthDAO;
import com.renyibang.userapi.entity.UserAuth;
import com.renyibang.userapi.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserAuthDAOImpl implements UserAuthDAO {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    public Optional<UserAuth> findByUserId(long userId) {
        return userAuthRepository.findByUserId(userId);
    }


    @Override
    public UserAuth save(UserAuth userAuth) {
        return userAuthRepository.save(userAuth);
    }

    @Override
    public void deleteByUserId(long userId) {
        userAuthRepository.deleteById(userId);
    }
}
