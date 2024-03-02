package es.codeurjc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MapsController {
    @GetMapping("/maps")
    public String maps(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        return "maps";
    }
}
