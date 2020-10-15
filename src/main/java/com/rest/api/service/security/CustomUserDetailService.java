package com.rest.api.service.security;

import com.rest.api.advice.exception.UserNotFoundException;
import com.rest.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findById(Long.valueOf(username)).orElseThrow(UserNotFoundException::new);
    }
}
