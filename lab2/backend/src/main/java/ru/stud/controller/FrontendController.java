package ru.stud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping({
            "/login",
            "/register",
            "/registration"
    })
    public String frontend() {
        return "forward:/index.html";
    }
}