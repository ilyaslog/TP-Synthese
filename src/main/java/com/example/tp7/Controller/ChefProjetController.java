package com.example.tp7.Controller;

import com.example.tp7.DAO.ProjDevRepository;
import com.example.tp7.DAO.ProjetRepository;
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
import java.util.Map;
import java.util.stream.Collectors;



@Controller
@RequestMapping("/ChefProjet")
public class ChefProjetController {


    private final DevelopeurService developeurService;
    private final ChefProjetService chefProjetService;
    private final ProjDevService projDevService;  // Constructor injection
    private final ProjDevRepository projDevRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private ProjetService projetService;

    // Constructor injection for services
    @Autowired
    public ChefProjetController(DevelopeurService developeurService,
                                ChefProjetService chefProjetService,
                                ProjDevService projDevService,
                                ProjDevRepository projDevRepository) {
        this.developeurService = developeurService;
        this.chefProjetService = chefProjetService;
        this.projDevService = projDevService;  // Constructor injection
        this.projDevRepository = projDevRepository;
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

   @GetMapping({"/dashboard", "/Dashboard"})
    public String showDashboard(Model model, HttpSession session) {
        ChefProjet chefProjet = (ChefProjet) session.getAttribute("chefprojet");
        if (chefProjet == null) {
            return "redirect:/login"; // Redirect to login if session is missing
        }
        Integer idChef = chefProjet.getId();





        long finishedProjects = projDevService.getFinishedProjectsByChef(idChef);
        long unfinishedProjects = projDevService.getUnfinishedProjectsByChef(idChef);
        Double averageRating = projDevService.getAverageRatingByChef(idChef);

        // Add the fetched data to the model
       List<Projet> ongoingProjects = projetService.getOngoingProjects(); // Fetch ongoing projects
       List<Projet> completedProjects = projetService.getCompletedProjects(); // Fetch completed projects\

       model.addAttribute("ongoingProjects", ongoingProjects); // Add ongoing projects to model
       model.addAttribute("completedProjects", completedProjects); // Add completed projects to model

        model.addAttribute("finishedProjects", finishedProjects);
        model.addAttribute("unfinishedProjects", unfinishedProjects);
        model.addAttribute("averageRating", averageRating != null ? averageRating : 0.0);
        return "/Admin/Dashboard";
     }
    @PostMapping("/ChefProjet/update/{idChef}")
    public String updateChefProjet(@PathVariable("idChef") int idChef, ChefProjet chefProjet,
                                   @SessionAttribute("chefProjet") ChefProjet sessionChefProjet, Model model) {
        // If sessionChefProjet is null, handle the situation (e.g., redirect to login)
        if (sessionChefProjet == null) {
            return "redirect:/login"; // Or another fallback action
        }

        // Update the ChefProjet by ID using the data from the session
        chefProjetService.updateByid(idChef, chefProjet);

        // Optional: update the session with the updated ChefProjet if you need to reflect the changes there
        // session.setAttribute("chefProjet", chefProjet); // Not needed if using @SessionAttribute

        // Redirect or display a confirmation message
        return "redirect:/ChefProjet/Profile"; // Or return the view to show the updated details
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
public String showProfile(HttpSession session, Model model) {
    ChefProjet chefProjet = (ChefProjet) session.getAttribute("chefprojet");
    if (chefProjet == null) {
        return "redirect:/ChefProjet/login"; // Redirect to login if session is missing
    }
    model.addAttribute("chefProjet", chefProjet);
    return "/Admin/Profile"; // Path to your profile page
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




    @GetMapping("/review/{idProj}")
    public String showReviewPage(@PathVariable("idProj") Integer idProj, Model model) {
        // Retrieve the project by its ID
        Projet projet = projetService.findById(idProj);

        if (projet != null) {
            // Fetch all ProjDev instances related to the project ID
            List<ProjDev> projDevs = projDevRepository.findByProjet_IdProj(idProj);

            // Pass the project and associated ProjDev instances to the view
            model.addAttribute("projet", projet);
            model.addAttribute("projDevs", projDevs);

            // Check if there are developers linked to the project
            if (projDevs.isEmpty()) {
                model.addAttribute("warning", "No developers assigned to this project.");
            }
        } else {
            // Handle case where the project is not found
            model.addAttribute("error", "Project not found.");
        }

        return "/Admin/Project-Review"; // Name of your review page template
    }




    @PostMapping("/submitReview")
    public String submitReview(@RequestParam("projectId") Integer projectId,
                               @RequestParam Map<String, String> allParams,
                               RedirectAttributes redirectAttributes) {

        // Log received parameters for debugging
        allParams.forEach((key, value) -> {
            System.out.println("Received param: " + key + " = " + value);
        });

        boolean hasErrors = false;
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("stars_")) {
                try {
                    Integer developerId = Integer.valueOf(entry.getKey().split("_")[1]);
                    Integer stars = Integer.valueOf(entry.getValue());
                    String commentaire = allParams.get("commentaire_" + developerId);

                    ProjDev projDev = projDevRepository.findByProjet_IdProjAndDeveloppeur_Id(projectId, developerId).orElse(null);

                    if (projDev != null) {
                        projDev.setStars(stars);
                        projDev.setCommentaire(commentaire);
                        projDevRepository.save(projDev);
                    } else {
                        System.out.println("ProjDev not found for projectId: " + projectId + " and developerId: " + developerId);
                        hasErrors = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid data for developer review: " + entry.getKey());
                    hasErrors = true;
                }
            }
        }

        // Change project status to "Terminé" if no errors
        if (!hasErrors) {
            Projet project = projetRepository.findById(projectId).orElse(null);
            if (project != null) {
                project.setStatut(1); // 1 = Terminé
                projetRepository.save(project);
            }
            redirectAttributes.addFlashAttribute("success", "Reviews submitted successfully, and project marked as 'Terminé'!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Some reviews could not be submitted due to invalid data or missing associations.");
        }

        return "redirect:/ChefProjet/Projet"; // Redirect after submission
    }




    @GetMapping("/details/{idProj}")
    public String projectDetails(@PathVariable("idProj") Integer idProj, Model model, RedirectAttributes redirectAttributes) {
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

        return "/Admin/Project-Details";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/ChefProjet/login";
    }

}