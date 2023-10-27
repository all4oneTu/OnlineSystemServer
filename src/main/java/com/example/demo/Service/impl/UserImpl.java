package com.example.demo.Service.impl;

import cn.hutool.core.codec.Base64;
import com.example.demo.Entity.User;
import com.example.demo.Enum.LoginTypeEnum;
import com.example.demo.Repo.UserRepo;
import com.example.demo.Service.UserService;
import com.example.demo.Utils.JwtUtils;
import com.example.demo.qo.Login;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserImpl implements UserService {

    @Autowired
    UserRepo userRepository;
    @Override
    public String login(Login loginQo) {
        User user;
        if (LoginTypeEnum.USERNAME.getType().equals(loginQo.getLoginType())) {
            user = userRepository.findByUserUsername(loginQo.getUserInfo());
        } else {
            user = userRepository.findByUserEmail(loginQo.getUserInfo());
        }
        if (user != null) {
            String passwordDb = Base64.decodeStr(user.getUserPassword());
            String passwordQo = loginQo.getPassword();
            System.out.println(passwordDb);
            System.out.println(passwordQo);
            if (passwordQo.equals(passwordDb)) {
                return JwtUtils.genJsonWebToken(user);
            }
        }
        return null;
    }
}
