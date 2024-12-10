package com.example.tp7.Controller;
import com.example.tp7.Service.DevelopeurService;
import com.example.tp7.entity.Developeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/developper")
public class DevelopperController {

    private final DevelopeurService developeurService;

    @Autowired
    public DevelopperController(DevelopeurService developeurService) {
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
        return "redirect:/developper/list";
    }

    // List all developers (optional, if you want to implement a list page)
    @GetMapping("/list")
    public String listDeveloppers(Model model) {
        model.addAttribute("developpers", developeurService.findAll());
        return "Dev-list"; // Replace with your actual Thymeleaf template for the list page
    }
}
