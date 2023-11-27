package com.example.demo.Service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.example.demo.Entity.Action;
import com.example.demo.Entity.Page;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Enum.LoginTypeEnum;
import com.example.demo.Enum.RoleEnum;
import com.example.demo.Repo.ActionRepo;
import com.example.demo.Repo.PageRepo;
import com.example.demo.Repo.RoleRepo;
import com.example.demo.Repo.UserRepo;
import com.example.demo.Service.UserService;
import com.example.demo.Utils.JwtUtils;
import com.example.demo.Vo.*;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.qo.Login;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class UserImpl implements UserService {

    @Autowired
    UserRepo userRepository;

    @Autowired
    RoleRepo roleRepository;
    @Autowired
    PageRepo pageRepository;
    @Autowired
    ActionRepo actionRepository;
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
    @Override
    public User register(RegisterDTO registerDTO) {
        try {
            User user = new User();
            user.setUserId(IdUtil.simpleUUID());
            String defaultUsername = "user";

            user.setUserUsername(defaultUsername + "_" + registerDTO.getMobile());
            user.setUserNickname(user.getUserUsername());
            user.setUserPassword(Base64.encode(registerDTO.getPassword()));
            user.setUserRoleId(RoleEnum.STUDENT.getId());
            String defaultAvatar = "https://pbs.twimg.com/profile_images/1614667602378518530/k3NtPbnN_400x400.jpg";
            user.setUserAvatar(defaultAvatar);
            user.setUserDescription("welcome to online exam system");
            user.setUserEmail(registerDTO.getEmail());

            user.setUserPhone(registerDTO.getMobile());
            userRepository.save(user);
            System.out.println(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserInfoVo getInfo(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        Integer roleId = user.getUserRoleId();
        Role role = roleRepository.findById(roleId).orElse(null);
        assert role != null;
        String roleName = role.getRoleName();

        userInfoVo.setRoleName(roleName);

        RoleVo roleVo = new RoleVo();
        BeanUtils.copyProperties(role, roleVo);

        String rolePageIds = role.getRolePageIds();
        String[] pageIdArr = rolePageIds.split("-");
        List<PageVo> pageVoList = new ArrayList<>();
        for (String pageIdStr : pageIdArr) {
            Integer pageId = Integer.parseInt(pageIdStr);

            Page page = pageRepository.findById(pageId).orElse(null);
            PageVo pageVo = new PageVo();
            BeanUtils.copyProperties(page, pageVo);

            List<ActionVo> actionVoList = new ArrayList<>();
            String actionIdsStr = page.getActionIds();
            String[] actionIdArr = actionIdsStr.split("-");
            for (String actionIdStr : actionIdArr) {
                Integer actionId = Integer.parseInt(actionIdStr);
                Action action = actionRepository.findById(actionId).orElse(null);
                ActionVo actionVo = new ActionVo();
                assert action != null;
                BeanUtils.copyProperties(action, actionVo);
                actionVoList.add(actionVo);
            }
            pageVo.setActionVoList(actionVoList);
            pageVoList.add(pageVo);
        }
        roleVo.setPageVoList(pageVoList);
        userInfoVo.setRoleVo(roleVo);
        return userInfoVo;
    }

    @Override
    public UserVo getUserInfo(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        UserVo userVo = new UserVo();
        assert user != null;
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }
}
