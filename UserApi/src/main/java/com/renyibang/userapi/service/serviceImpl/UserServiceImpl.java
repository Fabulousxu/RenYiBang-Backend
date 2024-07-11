package com.renyibang.userapi.service.serviceImpl;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import com.renyibang.global.dto.UserDTO;
import com.renyibang.userapi.dao.UserAuthDAO;
import com.renyibang.userapi.dao.UserDao;
import com.renyibang.userapi.entity.User;
import com.renyibang.userapi.entity.UserAuth;
import com.renyibang.userapi.repository.UserRepository;
import com.renyibang.userapi.service.UserService;
import com.renyibang.userapi.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDAO;

    @Autowired
    private UserAuthDAO userAuthDAO;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findById(long userId) {
        return userDAO.findById(userId);
    }

    @Override
    public User save(User user) {
        return userDAO.save(user);
    }

    @Override
    public void deleteById(long userId) {
        userDAO.deleteById(userId);
    }
    @Override
    public long register(String password, String nickname, String intro, String avatar) throws Exception {
        System.out.println("1");
        User user = new User();
        user.setNickname(nickname);
        user.setIntro(intro);
        user.setAvatar(avatar);
        userRepository.save(user);
        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(user.getUserId());
        userAuth.setPassword(password);
        userAuthDAO.save(userAuth);

        System.out.println("3");
        return user.getUserId();
    }

    @Override
    public JSONObject getUserInfo(long userId)
    {
        try
        {
            User user = userDAO.findById(userId).orElse(null);
            if(user == null)
            {
                return ResponseUtil.error("用户不存在！");
            }

            return ResponseUtil.success("获取当前用户信息成功！", user.toJSON());
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject modifyUserInfo(long userId, JSONObject body)
    {
        try
        {
            Object requestNickname = body.get("nickname");
            Object requestAvatar = body.get("avatar");
            Object requestIntro = body.get("intro");

            if(requestNickname == null || requestAvatar == null || requestIntro == null)
            {
                return ResponseUtil.error("请求体不完整！");
            }

            String nickname = requestNickname.toString();
            if(nickname.isEmpty())
            {
                return ResponseUtil.error("用户昵称不能为空！");
            }

            String avatar = requestAvatar.toString();
            String intro = requestIntro.toString();

            String result = userDAO.updateUserByUserId(userId, nickname, avatar, intro);
            if("修改用户信息成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject getUserDtoById(long userId)
    {
        try
        {
            User user = userDAO.findById(userId).orElse(null);
            if(user == null)
            {
                return ResponseUtil.error("用户不存在！");
            }

            UserDTO userDTO = new UserDTO(user.getUserId(), user.getBalance(), user.getNickname(), user.getAvatar());

            return ResponseUtil.success("获取当前用户信息成功！", userDTO);
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject updateUserByDto(UserDTO userDTO)
    {
        try
        {
            long userId = userDTO.getId();
            User user = userDAO.findById(userId).orElse(null);
            if(user == null)
            {
                return ResponseUtil.error("用户不存在！");
            }

            user.setBalance(userDTO.getBalance());
            user.setNickname(userDTO.getNickname());
            user.setAvatar(userDTO.getAvatar());

            userDAO.save(user);

            return ResponseUtil.success("更新成功！");
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject getUserInfos(Collection<Long> userIds)
    {
        try
        {
            return userDAO.findAllById(userIds);
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e));
        }
    }
}
