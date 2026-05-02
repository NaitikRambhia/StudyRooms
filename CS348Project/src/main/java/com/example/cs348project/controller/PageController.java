package com.example.cs348project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/adminPage")
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/studentPage")
    public String studentPage() {
        return "student";
    }

    @GetMapping("/reportPage")
    public String reportPage() {
        return "report";
    }
    @GetMapping("/")
    public String homePage() {
        return "HomePage";
    }

}
