package com.example.tp7.Controller;

import com.example.tp7.DAO.ProjDevRepository;
import com.example.tp7.DAO.ProjetRepository;
import com.example.tp7.Service.ChefProjetService;
import com.example.tp7.Service.DevelopeurService;
import com.example.tp7.Service.ProjDevService;
import com.example.tp7.Service.ProjetService;
import com.example.tp7.entity.Developeur;
import com.example.tp7.entity.ProjDev;
import com.example.tp7.entity.Projet;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/developer")
public class DevelopeurController {

    private final DevelopeurService developeurService;
    private final ProjDevService projDevService;  // Constructor injection
    private final ProjDevRepository projDevRepository;
    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private ProjetService projetService;
    @Autowired
    public DevelopeurController(DevelopeurService developeurService,
                                ProjDevService projDevService,
                                ProjDevRepository projDevRepository) {
        this.developeurService = developeurService;

        this.projDevService = projDevService;  // Constructor injection
        this.projDevRepository = projDevRepository;
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
            // Log developer ID directly from the object
            model.addAttribute("developer", developeur);

            // Get the finished and unfinished project counts
            long finishedProjects = projDevService.countFinishedProjectsByDeveloper(developeur.getId());
            long unfinishedProjects = projDevService.countUnfinishedProjectsByDeveloper(developeur.getId());

            // Get lists of finished and unfinished projects
            List<Projet> projectsUnfinished = projDevService.findBystatusAndDeveloppeur_Id(0, developeur.getId());
            List<Projet> projectsFinished = projDevService.findBystatusAndDeveloppeur_Id(1, developeur.getId());

            // Combine both lists
            List<Projet> allProjects = new ArrayList<>();
            allProjects.addAll(projectsUnfinished);
            allProjects.addAll(projectsFinished);

            // Debugging output
            System.out.println("Unfinished Projects: " + projectsUnfinished);
            System.out.println("Finished Projects: " + projectsFinished);
            System.out.println("Developer ID: " + developeur.getId());

            // Get the average rating for the developer
            Double averageRating = projDevService.findAverageRatingByDeveloper(developeur.getId());

            // Add attributes to the model
            model.addAttribute("finishedProjects", finishedProjects);
            model.addAttribute("unfinishedProjects", unfinishedProjects);
            model.addAttribute("finishedProjectsList", projectsFinished);
            model.addAttribute("unfinishedProjectsList", projectsUnfinished);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("ListProjects", allProjects); // Use merged list

            return "developer-dashboard"; // Return to the developer's dashboard page
        } else {
            return "redirect:/developer/login"; // Redirect to login if not logged in
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
            System.out.println("Developer ID: " + developeur.getId());
            return "Profile-dev"; // Return to the developer's profile page
        } else {
            return "redirect:/developer/login"; // Redirect to login if not logged in
        }
    }
    @PostMapping("/update")
    public String updateDev(@ModelAttribute("developeur") Developeur developeur, Model model) {
        model.addAttribute("developer", developeur);
        if (developeur.getId() != null) {
            developeurService.updateByid(developeur.getId(), developeur);

        }


        // Return the same page (profile page in this case)
        return "Profile-dev"; // or the name of the page you are updating
    }

    // Logout and invalidate the session
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session to log out the user
        return "redirect:/developer/login"; // Redirect to the login page after logout
    }

  @GetMapping("/details/{idProj}")
public String projectDetails(@PathVariable("idProj") Integer idProj, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    Developeur developeur = (Developeur) session.getAttribute("developer");
    if (developeur == null) {
        return "redirect:/developer/login"; // Redirect to login if not logged in
    }

    // Fetch the project
    Projet project = projetRepository.findById(idProj).orElse(null);
    if (project == null || project.getStatut() != 1) { // Ensure the project is "Terminé"
        redirectAttributes.addFlashAttribute("error", "The requested project is not available or not marked as 'Terminé'.");
        return "redirect:/ChefProjet/Projet";
    }

    // Fetch associated developers and their reviews
    List<ProjDev> projectDevelopers = projDevRepository.findByProjet_IdProj(idProj);

    model.addAttribute("project", project);
    model.addAttribute("developers", projectDevelopers);

    return "Project-details-dev";
}
}
