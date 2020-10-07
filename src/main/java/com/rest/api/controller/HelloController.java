package com.rest.api.controller;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/hello-world")
public class HelloController {

    @GetMapping(value = "/string")
    @ResponseBody
    public String string() {
        return "helloWorld";
    }

    @GetMapping(value = "/json")
    @ResponseBody
    public Hello json() {
        return Hello.builder().message("helloWorld").build();
    }

    @GetMapping(value = "/page")
    public String page() {
        return "hello-world";
    }

    @Builder
    @Getter
    public static class Hello {

        private final String message;
    }

}
