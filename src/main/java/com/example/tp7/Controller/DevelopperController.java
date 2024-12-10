package com.example.tp7.Controller;

import com.example.tp7.Service.DevelopeurService;
import com.example.tp7.entity.Developeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/developper")
public class DevelopperController {
    @Autowired
    public DevelopperController(DevelopeurService developeurService) {
        this.developeurService = developeurService;
    }

    private DevelopeurService developeurService;

    @GetMapping("/list")
    public String ShowAddFormDev(Model leModel) {
        Developeur developeur = new Developeur();
        leModel.addAttribute("developeur", developeur);
        return "Dev-form";
    }

    @PostMapping("/save")
    public String saveDev(@ModelAttribute("developeur") Developeur developeur) {
        developeurService.save(developeur);
        return "redirect:/developper/list";
    }
}
