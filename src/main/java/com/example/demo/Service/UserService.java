package com.example.demo.Service;

import com.example.demo.Entity.User;
import com.example.demo.Vo.UserInfoVo;
import com.example.demo.Vo.UserVo;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.qo.Login;

public interface UserService {

    String login(Login loginQo);

    User register(RegisterDTO registerDTO);
    UserInfoVo getInfo(String userId);

    UserVo getUserInfo(String userId);
}
