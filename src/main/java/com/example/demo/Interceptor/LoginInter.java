package com.example.demo.Interceptor;


import com.example.demo.Utils.JwtUtils;
import com.example.demo.Vo.JsonData;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

@Component
public class LoginInter implements HandlerInterceptor {
    @Value("${interceptors.auth-ignore-uris}")
    private String authIgnoreUris;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        System.out.println(uri);
        String[] authIgnoreUriArr = authIgnoreUris.split(",");
        for (String authIgnoreUri : authIgnoreUriArr) {
            if (authIgnoreUri.equals(uri)) {
                return true;
            }
        }

        String token = request.getHeader("Access-Token");
        if (token == null) {
            token = request.getParameter("token");
        }
        if (token != null) {
            Claims claims = JwtUtils.checkJWT(token);
            if (claims == null) {
                sendJsonMessage(response, JsonData.buildError("Token không hợp lệ, vui lòng đăng nhập lại"));
                return false;
            }
            String id = (String) claims.get("id");
            String username = (String) claims.get("username");
            // Đặt hai tham số này vào yêu cầu để có thể lấy chúng trong bộ điều khiển. Không cần sử dụng Jwt để giải mã trong bộ điều khiển. Có thể lấy được request.getAttribution ("Tên thuộc tính").
            request.setAttribute("user_id", id);
            request.setAttribute("username", username);
            return true;
        }
        sendJsonMessage(response, JsonData.buildError("token null"));
        return false;
    }
    public static void sendJsonMessage(HttpServletResponse response, Object obj) throws Exception {
        Gson g = new Gson();
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(g.toJson(obj));
        writer.close();
        response.flushBuffer();
    }

}
