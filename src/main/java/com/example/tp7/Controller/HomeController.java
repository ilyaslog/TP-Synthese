package com.example.tp7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // This will serve the index.html page from the static folder
        return "index"; // This references index.html in the /static folder
    }
}
