package com.dr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HrController {

    @GetMapping("/hrWelcome")
    public String HrWelcomePage(){
        return "hrWelcome";
    }

    @GetMapping("/login")
    public String HrLoginPage(){
        return "login";
    }

    @GetMapping("/hrHome")
    public String HrLoginSubmit(){
        return "hrHome";
    }
}
