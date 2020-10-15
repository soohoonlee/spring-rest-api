package com.rest.api.controller.exception;

import com.rest.api.advice.exception.AuthenticationEntryPointException;
import com.rest.api.model.response.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/exception")
@RestController
public class ExceptionController {

    @GetMapping(value = "/entry-point")
    public CommonResult entryPointException() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping(value = "/access-denied")
    public CommonResult accessDeniedException() {
        throw new AccessDeniedException("");
    }
}
