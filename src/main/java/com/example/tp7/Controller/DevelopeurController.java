package com.example.tp7.Controller;

import com.example.tp7.Service.DevelopeurService;
import com.example.tp7.entity.Developeur;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/developer")
public class DevelopeurController {

    private final DevelopeurService developeurService;

    @Autowired
    public DevelopeurController(DevelopeurService developeurService) {
        this.developeurService = developeurService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login-dev"; // Name of the login Thymeleaf template
    }

    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        @RequestParam("password") String password,
                        Model model, HttpSession session) {
        Developeur developeur = developeurService.validateLogin(login, password);

        if (developeur != null) {
            session.setAttribute("developer", developeur);
            return "redirect:/developer/dashboard"; // Redirect to dashboard
        } else { // If login fails
            model.addAttribute("error", "Invalid username or password. Please try again.");
            return "login-dev"; // Reload login page with error
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Developeur developeur = (Developeur) session.getAttribute("developer");

        if (developeur != null) {
            model.addAttribute("developer", developeur);
            return "developer-dashboard"; // Return to the developer's dashboard template
        } else {
            return "redirect:/developer/login";
        }
    }
}