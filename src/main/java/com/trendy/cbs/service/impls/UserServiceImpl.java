package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.User;
import com.trendy.cbs.repos.UserRepository;
import com.trendy.cbs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByJwt(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();
        return userRepository.findByAuthProviderUserId(keycloakUserId);
    }
}
