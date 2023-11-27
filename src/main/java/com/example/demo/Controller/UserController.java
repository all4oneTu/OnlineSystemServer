package com.example.demo.Controller;


import com.example.demo.Entity.User;
import com.example.demo.Enum.ResultEnum;
import com.example.demo.Service.UserService;
import com.example.demo.Vo.ResultVo;
import com.example.demo.Vo.UserInfoVo;
import com.example.demo.Vo.UserVo;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.qo.Login;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    ResultVo<String> login(@RequestBody Login loginQo) {
        ResultVo<String> resultVO;
        String token = userService.login(loginQo);
        if (token != null) {
            resultVO = new ResultVo<>(ResultEnum.LOGIN_SUCCESS.getCode(), ResultEnum.LOGIN_SUCCESS.getMessage(), token);
        } else {
            resultVO = new ResultVo<>(ResultEnum.LOGIN_FAILED.getCode(), ResultEnum.LOGIN_FAILED.getMessage(), null);
        }
        return resultVO;
    }
    @PostMapping("/register")
    ResultVo<User> register(@RequestBody RegisterDTO registerDTO) {
        ResultVo<User> resultVO;
        User user = userService.register(registerDTO);
        if (user != null) {
            resultVO = new ResultVo<>(ResultEnum.REGISTER_SUCCESS.getCode(), ResultEnum.REGISTER_SUCCESS.getMessage(), user);
        } else {
            resultVO = new ResultVo<>(ResultEnum.REGISTER_FAILED.getCode(), ResultEnum.REGISTER_FAILED.getMessage(), null);
        }
        return resultVO;
    }
    @GetMapping("/user-info")
    ResultVo<UserVo> getUserInfo(HttpServletRequest request) {
        String userId = (String) request.getAttribute("user_id");
        UserVo userVo = userService.getUserInfo(userId);
        return new ResultVo<>(ResultEnum.GET_INFO_SUCCESS.getCode(), ResultEnum.GET_INFO_SUCCESS.getMessage(), userVo);
    }

    @GetMapping("/info")
    ResultVo<UserInfoVo> getInfo(HttpServletRequest request) {
        System.out.println("/user/info");
        String userId = (String) request.getAttribute("user_id");
        UserInfoVo userInfoVo = userService.getInfo(userId);
        return new ResultVo<>(ResultEnum.GET_INFO_SUCCESS.getCode(), ResultEnum.GET_INFO_SUCCESS.getMessage(), userInfoVo);
    }
}
