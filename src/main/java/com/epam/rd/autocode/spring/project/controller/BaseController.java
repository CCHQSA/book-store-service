package com.epam.rd.autocode.spring.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {

    @GetMapping("/")
    public String viewPage(){
        return "page";
    }
}
