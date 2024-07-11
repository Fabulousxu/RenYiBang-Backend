package com.renyibang.userapi.dao.daoImpl;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.renyibang.userapi.dao.UserDao;
import com.renyibang.userapi.entity.User;
import com.renyibang.userapi.repository.UserRepository;
import com.renyibang.userapi.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public String updateUserByUserId(long userId, String nickname, String avatar, String intro)
    {
        try
        {
            User user = userRepository.findById(userId).orElse(null);
            if(user == null)
            {
                return "用户不存在！";
            }

            user.setNickname(nickname);
            user.setAvatar(avatar);
            user.setIntro(intro);

            userRepository.save(user);

            return "修改用户信息成功！";
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    @Override
    public JSONObject findAllById(Collection<Long> userIds)
    {
        JSONArray users = new JSONArray();

        for(Long userId : userIds)
        {
            User user = userRepository.findById(userId).orElse(null);

            if(user == null)
            {
                return ResponseUtil.error("用户id为" + userId + "不存在！");
            }

            users.add(user.toJSON());
        }

        return ResponseUtil.success("查找成功", users);
    }
}
