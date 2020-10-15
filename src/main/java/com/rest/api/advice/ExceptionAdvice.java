package com.rest.api.advice;

import com.rest.api.advice.exception.AuthenticationEntryPointException;
import com.rest.api.advice.exception.EmailSignInFailedException;
import com.rest.api.advice.exception.UserNotFoundException;
import com.rest.api.model.response.CommonResult;
import com.rest.api.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("unKnown.code")), getMessage("unKnown.message"));
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, UserNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("userNotFound.code")), getMessage("userNotFound.message"));
    }

    @ExceptionHandler(value = EmailSignInFailedException.class)
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    protected CommonResult emailSignInFailedException(HttpServletRequest request, EmailSignInFailedException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("emailSignInFailed.code")), getMessage("emailSignInFailed.message"));
    }

    @ExceptionHandler(value = AuthenticationEntryPointException.class)
    @ResponseStatus(value = UNAUTHORIZED)
    public CommonResult authenticationEntryPointException(HttpServletRequest request, AuthenticationEntryPointException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("entryPointException.code")), getMessage("entryPointException.message"));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(value = UNAUTHORIZED)
    public CommonResult accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("accessDenied.code")), getMessage("accessDenied.message"));
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
