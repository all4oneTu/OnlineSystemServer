package com.example.demo.Controller;


import com.example.demo.Enum.ResultEnum;
import com.example.demo.Service.UserService;
import com.example.demo.Vo.ResultVo;
import com.example.demo.qo.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
