package com.example.tp7.Controller;

import com.example.tp7.Service.ChefProjetService;
import com.example.tp7.Service.DevelopeurService;
import com.example.tp7.Service.ProjetService;
import com.example.tp7.entity.ChefProjet;
import com.example.tp7.entity.Developeur;
import com.example.tp7.entity.ProjDev;
import com.example.tp7.entity.Projet;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ChefProjet")
public class ChefProjetController {

    private final DevelopeurService developeurService;
    private final ChefProjetService chefProjetService;

    @Autowired
    private ProjetService projetService;

    @Autowired
    public ChefProjetController(DevelopeurService developeurService, ChefProjetService chefProjetService) {
        this.developeurService = developeurService;
        this.chefProjetService = chefProjetService;
        this.projetService = projetService;
    }

    // Session validation utility method
    private boolean isSessionValid(HttpSession session) {
        return session.getAttribute("chefprojet") != null;
    }

    // Show form to add a new developer
    @GetMapping("/showForm")
    public String showAddFormDev(HttpSession session, Model model) {
        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef";
        }
        model.addAttribute("developeur", new Developeur());
        return "/Admin/Dev-Form";
    }

    @PostMapping("/saveProject")
    public String saveProjectWithDevelopers(
            HttpSession session,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("debutProj") @DateTimeFormat(pattern = "yyyy-MM-dd") Date debutProj,
            @RequestParam("finProj") @DateTimeFormat(pattern = "yyyy-MM-dd") Date finProj,
            @RequestParam("duree") int duree,
            @RequestParam("statut") int statut,
            @RequestParam("competencesRequise") String competencesRequise,
            @RequestParam("assignedDevs") List<Integer> developerIds,
            Model model) {

        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef";
        }

        // Create a new project
        Projet projet = new Projet();
        projet.setTitre(titre);
        projet.setDescription(description);
        projet.setDebutProj(debutProj);
        projet.setFinProj(finProj);
        projet.setDuree(duree);
        projet.setCompetencesRequise(competencesRequise);
        projet.setStatut(statut);

        // Link project to ChefProjet from session
        ChefProjet chefProjet = (ChefProjet) session.getAttribute("chefprojet");
        projet.setChefProjet(chefProjet);

        // Create ProjDev instances for the developers
        List<ProjDev> projDevs = developerIds.stream()
                .map(devId -> new ProjDev(projet, developeurService.findById(devId), "Assigned", 0))
                .collect(Collectors.toList());

        // Save project with developers
        projetService.saveProjetWithDevelopers(projet, projDevs);

        return "redirect:/ChefProjet/Projet";
    }

    // Display the list of projects and developers
    @GetMapping("/Projet")
    public String showProjet(HttpSession session, Model model) {
        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef";
        }

        List<Developeur> developpers = developeurService.findAll();
        List<Projet> projects = projetService.getAllProjets();

        model.addAttribute("developpers", developpers);
        model.addAttribute("projects", projects);
        return "/Admin/Project";
    }

    // Dashboard
    @GetMapping("/Dashboard")
    public String showDashboard(HttpSession session, Model model) {
        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef";
        }

        model.addAttribute("chefprojet", session.getAttribute("chefprojet"));
        return "Admin/Dashboard";
    }

    // Save developer details from the form
    @PostMapping("/save")
    public String saveDev(HttpSession session, @ModelAttribute("developeur") Developeur developeur, Model model) {
        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef";
        }
        developeurService.save(developeur);
        return "redirect:/ChefProjet/list";
    }

    // List all developers
    @GetMapping("/list")
    public String listDeveloppers(HttpSession session, Model model) {
        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef";
        }

        model.addAttribute("developpers", developeurService.findAll());
        return "/Admin/Dev-list";
    }

    // Delete a developer
    @PostMapping("/delete")
    public String deleteDev(HttpSession session, @RequestParam("id") int id, Model model) {
        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef";
        }

        developeurService.deleteById(id);
        return "redirect:/ChefProjet/list";
    }

    // Show update form for a developer
    @GetMapping("/updateForm")
    public String showUpdateForm(HttpSession session, @RequestParam("id") int id, Model model) {
        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef";
        }

        Developeur developeur = developeurService.findById(id);
        if (developeur == null) {
            return "redirect:/ChefProjet/list?error=notfound";
        }
        model.addAttribute("developeur", developeur);
        return "/Admin/Update-Form";
    }

    // Update developer details
    @PostMapping("/update")
    public String updateDev(HttpSession session, @ModelAttribute("developeur") Developeur developeur) {
        if (!isSessionValid(session)) {
            return "/Admin/login-chef";
        }

        if (developeur.getId() != null) {
            developeurService.updateByid(developeur.getId(), developeur);
        }
        return "redirect:/ChefProjet/list";
    }

    @GetMapping("/search")
    public String searchDevelopers(HttpSession session, @RequestParam("query") String query, Model model) {
        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef";
        }

        List<Developeur> developers = developeurService.searchByName(query);
        model.addAttribute("developpers", developers);
        model.addAttribute("searchQuery", query); // For showing the query in the UI
        return "/Admin/Dev-list";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "/Admin/login-chef";
    }

    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        @RequestParam("password") String password,
                        Model model, HttpSession session) {
        ChefProjet chefprojet = chefProjetService.validateLogin(login, password);

        if (chefprojet != null) {
            session.setAttribute("chefprojet", chefprojet);
            return "redirect:/ChefProjet/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password. Please try again.");
            return "/Admin/login-chef";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef";
        }

        model.addAttribute("chefprojet", session.getAttribute("chefprojet"));
        return "Admin/Dashboard";
    }

    // Show forgot password form
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "/Admin/forgot-password-chef";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("login") String login,
                                       @RequestParam("newPassword") String newPassword,
                                       Model model) {
        ChefProjet chefProjet = chefProjetService.updatePassword(login, newPassword);

        if (chefProjet != null) {
            model.addAttribute("success", "Password reset successfully!");
            return "/Admin/login-chef";
        } else {
            model.addAttribute("error", "Username not found. Please try again.");
            return "/Admin/forgot-password-chef";
        }
    }

    @GetMapping("/Profile")
    public String showProfile(HttpSession session, Model model) {
        // Check if session is valid
        if (!isSessionValid(session)) {
            model.addAttribute("error", "You must log in first!");
            return "/Admin/login-chef"; // Redirect to login if not logged in
        }

        // Get the 'chefProjet' object from the session
        ChefProjet chefProjet = (ChefProjet) session.getAttribute("chefProjet");

        // Add 'chefProjet' object to the model for displaying in the profile form
        model.addAttribute("chefProjet", chefProjet);

        return "/Admin/Profile"; // Render the profile page
    }


}
