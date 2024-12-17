package com.example.tp7.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/session")
public class SessionController {

    // Retrieve session attribute
    @GetMapping("/get")
    public String getSession(HttpSession session) {
        // Check if the user is logged in
        Object chefprojet = session.getAttribute("chefprojet");

        if (chefprojet == null) {
            return "No active session or user not logged in!";
        }

        return "Session active with ChefProjet: " + chefprojet.toString();
    }

    // Invalidate the session
    @GetMapping("/invalidate")
    public String invalidateSession(HttpSession session) {
        session.invalidate();
        return "Session invalidated!";
    }
}
