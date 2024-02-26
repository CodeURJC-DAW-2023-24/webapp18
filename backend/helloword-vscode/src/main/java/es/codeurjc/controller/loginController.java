package es.codeurjc.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class loginController {

    @RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/loginerror")
	public String loginerror() {
		return "loginerror";
	}
    
}
