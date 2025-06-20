package com.data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @GetMapping("/404")
    public String handle404() {
        return "templates/error/404";
    }

    @GetMapping("/403")
    public String handle403() {
        return "templates/error/403";
    }
}
