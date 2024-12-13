package com.example.tp7.Controller;

import com.example.tp7.Service.ChefProjetService;
import com.example.tp7.Service.DevelopeurService;
import com.example.tp7.entity.Developeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ChefProjet")
public class ChefProjetController {

    private final DevelopeurService developeurService;


    @Autowired
    public ChefProjetController(DevelopeurService developeurService, ChefProjetService chefProjetService) {
        this.developeurService = developeurService;

    }

    // Show form to add a new developer
    @GetMapping("/showForm")
    public String showAddFormDev(Model model) {
        model.addAttribute("developeur", new Developeur());
        return "/Admin/Dev-Form";
    }

    @GetMapping("/Dashboard")
    public String showDashboard(Model model) {
        return "/Admin/Dashboard";
    }
    @GetMapping("/Projet")
    public String showProjet(Model model) {
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

}
