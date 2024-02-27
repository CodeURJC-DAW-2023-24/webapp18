package es.codeurjc.controller;

import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class loginController {

    @RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/loginerror")
	public String loginerror(Model model) {
        model.addAttribute("title", "Error");
        model.addAttribute("message", "Credenciales inválidas");
        model.addAttribute("back", "/login");

        return "message";
	}
}
