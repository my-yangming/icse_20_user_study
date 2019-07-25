package com.github.vole.portal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class SsoAuthController {


    /**
     * 用户信�?�校验
     *
     * @param authentication 信�?�
     * @return 用户信�?�
     */
    @RequestMapping("/user")
    public Object user(Authentication authentication) {
        if (authentication != null) {
            return authentication.getPrincipal();
        }
        return null;
    }
}
