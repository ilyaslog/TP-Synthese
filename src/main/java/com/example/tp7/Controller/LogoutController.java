package com.example.tp7.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the session
        session.invalidate();
        return "redirect:/index"; // Redirect to login page after logout
    }

    @GetMapping("/index")
    public String showLoginPage() {
        return "/index"; // Path to your login page
    }
}
