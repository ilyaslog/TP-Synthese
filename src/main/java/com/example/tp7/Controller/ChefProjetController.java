package com.example.tp7.Controller;

import com.example.tp7.DAO.ProjDevRepository;
import com.example.tp7.Service.ChefProjetService;
import com.example.tp7.Service.DevelopeurService;
import com.example.tp7.Service.ProjDevService;
import com.example.tp7.Service.ProjetService;
import com.example.tp7.entity.ChefProjet;
import com.example.tp7.entity.Developeur;
import com.example.tp7.entity.ProjDev;
import com.example.tp7.entity.Projet;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ChefProjet")
public class ChefProjetController {

    private final DevelopeurService developeurService;

    private final ChefProjetService chefProjetService;

    private ProjDevService projDevService;

    @Autowired
    private ProjDevRepository projDevRepository;
    @Autowired
    private ProjetService projetService;

    @Autowired
    public ChefProjetController(DevelopeurService developeurService, ChefProjetService chefProjetService) {
        this.developeurService = developeurService;
        this.chefProjetService = chefProjetService;
        this.projetService = projetService;
    }

    // Show form to add a new developer
    @GetMapping("/showForm")
    public String showAddFormDev(Model model) {
        model.addAttribute("developeur", new Developeur());
        return "/Admin/Dev-Form";
    }
    @PostMapping("/saveProject")
    public String saveProjectWithDevelopers(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("debutProj") @DateTimeFormat(pattern = "yyyy-MM-dd") Date debutProj,
            @RequestParam("finProj") @DateTimeFormat(pattern = "yyyy-MM-dd") Date finProj,
            @RequestParam("duree") int duree,
            @RequestParam("statut") int statut,
            @RequestParam("competencesRequise") String competencesRequise,
            @RequestParam("assignedDevs") List<Integer> developerIds) {

        // Log input data for debugging
        System.out.println("Saving project with title: " + titre);
        System.out.println("Assigned developers: " + developerIds);

        // Create a new project
        Projet projet = new Projet();
        projet.setTitre(titre);
        projet.setDescription(description);
        projet.setDebutProj(debutProj);
        projet.setFinProj(finProj);
        projet.setDuree(duree);
        projet.setCompetencesRequise(competencesRequise);
        projet.setStatut(statut);

        // Link project to ChefProjet (ID = 1)
        ChefProjet chefProjet = chefProjetService.findById(1);
        if (chefProjet == null) {
            throw new IllegalStateException("ChefProjet with ID 1 not found!");
        }
        projet.setChefProjet(chefProjet);

        // Create ProjDev instances for the developers
        List<ProjDev> projDevs = developerIds.stream()
                .map(devId -> new ProjDev(projet, developeurService.findById(devId), "Assigned", 0))
                .collect(Collectors.toList());

        // Save project with developers
        projetService.saveProjetWithDevelopers(projet, projDevs);

        return "redirect:/ChefProjet/Projet"; // Redirect to project listing page
    }

    // Display the list of projects and developers
    @GetMapping("/Projet")
    public String showProjet(Model model) {
        List<Developeur> developpers = developeurService.findAll();
        List<Projet> projects = projetService.getAllProjets();

        if (projects.isEmpty()) {
            System.out.println("No projects found in the database!");
        } else {
            System.out.println("Fetched projects: " + projects.size());
        }

        model.addAttribute("developpers", developpers);
        model.addAttribute("projects", projects);
        return "/Admin/Project";
    }
    @GetMapping("/Dashboard")
    public String showDashboard(Model model) {
        return "/Admin/Dashboard";
    }

    // Save developer details from the form


    @RequestMapping("/save")
    public String saveDev(@ModelAttribute("developeur") Developeur developeur) {
        developeurService.save(developeur);
        return "redirect:/ChefProjet/list";
    }

    // List all developers
    @GetMapping("/list")
    public String listDeveloppers(Model model) {
        model.addAttribute("developpers", developeurService.findAll());
        return "/Admin/Dev-list";
    }

    // Delete a developer
    @PostMapping("/delete")
    public String deleteDev(@RequestParam("id") int id) {
        developeurService.deleteById(id);
        return "redirect:/ChefProjet/list";
    }

    // Show update form for a developer
    @GetMapping("/updateForm")
    public String showUpdateForm(@RequestParam("id") int id, Model model) {
        Developeur developeur = developeurService.findById(id);
        if (developeur == null) {
            // Add an error message or redirect to an error page
            return "redirect:/ChefProjet/list?error=notfound";
        }
        model.addAttribute("developeur", developeur);
        return "/Admin/Update-Form";
    }

    // Update developer details
    @PostMapping("/update")
    public String updateDev(@ModelAttribute("developeur") Developeur developeur) {
        if (developeur.getId() != null) { // Ensure the ID is present
            System.out.println("updateDev: developeur.getId() = " + developeur.getId());
            developeurService.updateByid(developeur.getId(), developeur);
        }
        return "redirect:/ChefProjet/list";  // Redirect back to the developer list page
    }
    @GetMapping("/search")
    public String searchDevelopers(@RequestParam("query") String query, Model model) {
        List<Developeur> developers = developeurService.searchByName(query);
        model.addAttribute("developpers", developers);
        model.addAttribute("searchQuery", query); // For showing the query in the UI
        return "/Admin/Dev-list";
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "/Admin/login-chef"; // Path to your login page
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
    public String showDashboard(Model model, HttpSession session) {
        ChefProjet chefProjet = (ChefProjet) session.getAttribute("chefprojet");

        if (chefProjet == null) {
            return "redirect:/ChefProjet/login"; // Redirect to  if session is missing
        }

        model.addAttribute("chefProjet", chefProjet);
        return "/Admin/Dashboard"; // Replace with your actual dashboard template
    }

    // Show forgot password form
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "/Admin/forgot-password-chef";  // Return forgot password page
    }
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("login") String login,
                                       @RequestParam("newPassword") String newPassword,
                                       Model model) {
        ChefProjet chefProjet = chefProjetService.updatePassword(login, newPassword);

        if (chefProjet != null) {
            model.addAttribute("success", "Password reset successfully!");
            return "/Admin/login-chef";  // Redirect to login page
        } else {
            model.addAttribute("error", "Username not found. Please try again.");
            return "/Admin/forgot-password-chef";  // Show error and return to form
        }
    }

    @GetMapping("/Profile")
    public String showProfile() {
        return "/Admin/Profile"; // Path to your login page
    }

    // Display edit form for a specific project
    @GetMapping("/edit/{id}")
    public String editProject(@PathVariable("id") Integer id, Model model){
        // Retrieve project by ID
        Projet projet = projetService.findById(id);

        if (projet == null) {
            // If project not found, redirect to project listing with an error parameter
            return "redirect:/ChefProjet/Projet?error=notfound";
        }

        // Add project to model for editing
        model.addAttribute("projet", projet);
        return "/Admin/Update-Project"; // Thymeleaf template for editing
    }

    // Save the updated project
    // Save the updated project and redirect back to the project page
    @PostMapping("/updateProject")
    public String updateProject(@ModelAttribute("projet") Projet updatedProjet, RedirectAttributes redirectAttributes) {
        // Retrieve the existing project
        Projet existingProject = projetService.findById(updatedProjet.getIdProj());

        if (existingProject == null) {
            // If project is not found, redirect to the project page with an error message
            redirectAttributes.addFlashAttribute("error", "Project not found!");
            return "redirect:/ChefProjet/Projet";
        }

        // Update fields of the existing project
        existingProject.setTitre(updatedProjet.getTitre());
        existingProject.setDescription(updatedProjet.getDescription());
        existingProject.setDebutProj(updatedProjet.getDebutProj());
        existingProject.setFinProj(updatedProjet.getFinProj());
        existingProject.setDuree(updatedProjet.getDuree());
        existingProject.setCompetencesRequise(updatedProjet.getCompetencesRequise());

        // Save the updated project
        projetService.update(existingProject);

        // Add a success message for confirmation
        redirectAttributes.addFlashAttribute("success", "Project updated successfully!");

        // Redirect to the project listing page
        return "redirect:/ChefProjet/Projet";
    }



    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }



    @PostMapping("/deleteProject")
    public String deleteProject(@RequestParam("idProj") Integer idProj, RedirectAttributes redirectAttributes) {
        try {
            projetService.deleteProjectById(idProj);
            redirectAttributes.addFlashAttribute("success", "Project deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error while deleting the project.");
        }
        return "redirect:/ChefProjet/Projet";
    }



    // Show review page
    @GetMapping("/review/{idProj}")
    public String showReviewPage(@PathVariable("idProj") Integer idProj, Model model) {
        // Retrieve project by ID
        Projet projet = projetService.findById(idProj);

        if (projet != null) {
            // Fetch the ProjDev instance by project ID
            ProjDev projDev = projDevRepository.findByProjet_IdProj(idProj);

            if (projDev != null) {
                // Pass project and developer info to the review page
                model.addAttribute("projet", projet);
                model.addAttribute("developer", projDev.getDeveloppeur());
            } else {
                model.addAttribute("error", "Developer information not found for this project.");
            }
        } else {
            model.addAttribute("error", "Project not found.");
        }

        return "Project-Review"; // Name of your review page
    }

    // Submit review for a developer in a project
    @PostMapping("/submitReview")
    public String submitReview(@RequestParam("projectId") Integer projectId,
                               @RequestParam("developerId") Integer developerId,
                               @RequestParam("stars") Integer stars,
                               @RequestParam("commentaire") String commentaire,
                               RedirectAttributes redirectAttributes) {

        // Fetch the ProjDev entity using project and developer IDs
        ProjDev projDev = projDevRepository.findByProjet_IdProjAndDeveloppeur_Id(projectId, developerId);

        if (projDev != null) {
            // Update the ProjDev entity with review data
            projDev.setStars(stars);
            projDev.setCommentaire(commentaire);

            // Save the updated ProjDev
            projDevRepository.save(projDev);

            redirectAttributes.addFlashAttribute("success", "Review submitted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Developer or project not found.");
        }

        return "redirect:/ChefProjet/Projet"; // Redirect back to the project listing page
    }



}