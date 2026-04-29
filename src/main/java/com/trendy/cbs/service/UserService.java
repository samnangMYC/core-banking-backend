package com.trendy.cbs.service;

import com.trendy.cbs.entity.User;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {

    User loadUserByJwt(Jwt jwt);
}
