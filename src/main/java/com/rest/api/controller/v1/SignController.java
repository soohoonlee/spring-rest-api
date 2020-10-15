package com.rest.api.controller.v1;

import com.rest.api.advice.exception.EmailSignInFailedException;
import com.rest.api.config.security.JwtTokenProvider;
import com.rest.api.entity.User;
import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.repository.UserRepository;
import com.rest.api.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Collections.singletonList;

@Api(tags = {"1. Sign"})
@RestController
@RequestMapping(value = "/v1")
@RequiredArgsConstructor
public class SignController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/sign-in")
    public SingleResult<String> signIn(@ApiParam(value = "회원 ID : 이메일", required = true) @RequestParam String id,
                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
        User user = userRepository.findByUid(id).orElseThrow(EmailSignInFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new EmailSignInFailedException();
        }
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles()));
    }

    @ApiOperation(value = "가입", notes = "회원 가입을 한다.")
    @PostMapping(value = "/sign-up")
    public CommonResult signUp(@ApiParam(value = "회원 ID : 이메일", required = true) @RequestParam String id,
                               @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
                               @ApiParam(value = "이름", required = true) @RequestParam String name) {
        User user = User.builder()
                .uid(id)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(singletonList("ROLE_USER"))
                .build();
        userRepository.save(user);
        return responseService.getSuccessResult();
    }
}
