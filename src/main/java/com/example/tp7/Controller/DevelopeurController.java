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

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login-dev"; // Name of the login Thymeleaf template
    }

    // Handle login requests
    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        @RequestParam("password") String password,
                        Model model, HttpSession session) {
        // Validate the login and password against the database
        Developeur developeur = developeurService.validateLogin(login, password);

        if (developeur != null) {
            // Create a secure session for the developer
            session.setAttribute("developer", developeur);
            return "redirect:/developer/dashboard"; // Redirect to the developer's dashboard
        } else { // If login fails
            model.addAttribute("error", "Invalid username or password. Please try again.");
            return "login-dev"; // Reload login page with error message
        }
    }

    // Show developer's dashboard if logged in
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Developeur developeur = (Developeur) session.getAttribute("developer");

        if (developeur != null) {
            model.addAttribute("developer", developeur);
            return "developer-dashboard"; // Return to the developer's dashboard page
        } else {
            return "redirect:/developer/login"; // If not logged in, redirect to login
        }
    }

    // Show forgot password form
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "/forgot-password-dev"; // Return forgot password page for developer
    }

    // Handle password reset request
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("login") String login,
                                       @RequestParam("newPassword") String newPassword,
                                       Model model) {
        // Validate and update the password
        Developeur developeur = developeurService.updatePassword(login, newPassword);

        if (developeur != null) {
            model.addAttribute("success", "Password reset successfully!");
            return "/login-dev"; // Redirect to login page after password reset
        } else {
            model.addAttribute("error", "Username not found. Please try again.");
            return "/forgot-password-dev"; // Reload the forgot password page with an error
        }
    }

    // Show Profile page
    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        Developeur developeur = (Developeur) session.getAttribute("developer");

        if (developeur != null) {
            model.addAttribute("developer", developeur);
            return "Profile-dev"; // Return to the developer's profile page
        } else {
            return "redirect:/developer/login"; // Redirect to login if not logged in
        }
    }
    @PostMapping("/update")
    public String updateDev(@ModelAttribute("developeur") Developeur developeur, Model model) {
        if (developeur.getId() != null) {
            developeurService.updateByid(developeur.getId(), developeur);
        }
        model.addAttribute("developer", developeur);

        // Return the same page (profile page in this case)
        return "Profile-dev"; // or the name of the page you are updating
    }

    // Logout and invalidate the session
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session to log out the user
        return "redirect:/developer/login"; // Redirect to the login page after logout
    }
}
