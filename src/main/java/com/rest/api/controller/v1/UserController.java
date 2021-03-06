package com.rest.api.controller.v1;

import com.rest.api.entity.User;
import com.rest.api.advice.exception.UserNotFoundException;
import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.ListResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.repository.UserRepository;
import com.rest.api.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Collections.singletonList;

@Api(tags = {"2. User"})
@RestController
@RequestMapping(value = "/v1")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다.")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userRepository.findAll());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(uid)로 회원을 조회한다.")
    @GetMapping(value = "/user")
    public SingleResult<User> findUser(@ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        log.info("id = {}", id);
        return responseService.getSingleResult(userRepository.findByUid(id).orElseThrow(UserNotFoundException::new));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다.")
    @PostMapping(value = "/user")
    public SingleResult<User> save(@ApiParam(value = "회원 아이디", required = true) @RequestParam String uid,
                                   @ApiParam(value = "회원 이름", required = true) @RequestParam String name,
                                   @ApiParam(value = "회원 비밀번호", required = true) @RequestParam String password) {
        User user = User.builder()
                .uid(uid)
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(singletonList("ROLE_USER"))
                .build();
        return responseService.getSingleResult(userRepository.save(user));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원 정보를 수정한다.")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(@ApiParam(value = "회원 번호", required = true) @RequestParam Long id,
                                     @ApiParam(value = "회원 아이디", required = true) @RequestParam String uid,
                                     @ApiParam(value = "회원 이름", required = true) @RequestParam String name,
                                     @ApiParam(value = "회원 비밀번호", required = true) @RequestParam String password) {
        User user = User.builder()
                .id(id)
                .uid(uid)
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(singletonList("ROLE_USER"))
                .build();
        return responseService.getSingleResult(userRepository.save(user));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "userId로 회원 정보를 삭제한다.")
    @DeleteMapping(value = "/user/{id}")
    public CommonResult delete(@ApiParam(value = "회원 번호", required = true) @PathVariable Long id) {
        userRepository.deleteById(id);
        return responseService.getSuccessResult();
    }

}
