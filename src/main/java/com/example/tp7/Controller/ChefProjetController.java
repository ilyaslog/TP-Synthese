package com.example.tp7.Controller;

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
    public ChefProjetController(DevelopeurService developeurService) {
        this.developeurService = developeurService;
    }

    // Show form to add a new developer
    @GetMapping("/showForm")
    public String showAddFormDev(Model model) {
        // Create a new Developeur object to bind to the form
        Developeur developeur = new Developeur();
        model.addAttribute("developeur", developeur);
        return "Dev-form"; // Return the name of the Thymeleaf template
    }

    // Save developer details from the form
    @PostMapping("/save")
    public String saveDev(@ModelAttribute("developeur") Developeur developeur) {
        // Save the developer using the service
        developeurService.save(developeur);
        // Redirect to a listing page after saving
        return "redirect:/developeur/list";
    }

    // List all developers
    @GetMapping("/list")
    public String listDeveloppers(Model model) {
        model.addAttribute("developpers", developeurService.findAll());
        return "Dev-list"; // Return the name of the Thymeleaf template for the list page
    }

    // Delete a developer
    @PostMapping("/delete")
    public String deleteDev(@RequestParam("devId") int id) {
        // Delete the developer using the service
        developeurService.deleteById(id);
        // Redirect to a listing page after deleting
        return "redirect:/developeur/list";
    }

    @GetMapping("/updateForm")
    public String showUpdateForm(@RequestParam("id") int id, Model model) {
        Developeur developeur = developeurService.findById(id); // Fetch the developer by ID
        model.addAttribute("developeur", developeur); // Add it to the model
        return "Dev-form"; // Return the form view
    }

    // Update developer details from the form
    @PostMapping("/update")
    public String updateDev(@ModelAttribute("developeur") Developeur developeur) {
        developeurService.save(developeur); // Save the updated developer details
        return "redirect:/developper/list"; // Redirect to the list page after updating
    }


}