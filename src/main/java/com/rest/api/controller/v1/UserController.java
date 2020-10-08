package com.rest.api.controller.v1;

import com.rest.api.entity.User;
import com.rest.api.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"1. User"})
@RestController
@RequestMapping(value = "/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @ApiOperation(value = "회원 조회", notes = "모든 회원을 조회한다.")
    @GetMapping(value = "/user")
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다.")
    @PostMapping(value = "/user")
    public User save(@ApiParam(value = "회원 아이디", required = true) @RequestParam String uid,
                     @ApiParam(value = "회원 이름", required = true) @RequestParam String name) {
        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return userRepository.save(user);
    }
}
