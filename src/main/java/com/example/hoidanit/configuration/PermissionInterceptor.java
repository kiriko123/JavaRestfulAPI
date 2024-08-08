package com.example.hoidanit.configuration;

import com.example.hoidanit.model.Permission;
import com.example.hoidanit.model.Role;
import com.example.hoidanit.model.User;
import com.example.hoidanit.service.UserService;
import com.example.hoidanit.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Transactional
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        //check permission
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if(!email.isEmpty()){
            User user = userService.findByEmail(email);
            if(user != null){
                Role role = user.getRole();
                if(role != null){
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow= permissions.stream().anyMatch(item ->
                        item.getApiPath().equals(path) && item.getMethod().equals(httpMethod)
                    );
                    System.out.println("This is is allow: " + isAllow);
                    if (!isAllow){
                        throw new AccessDeniedException("Access denied");
                    }
                }else{
                    throw new AccessDeniedException("Access denied");
                }
            }
        }
        return true;
    }
}