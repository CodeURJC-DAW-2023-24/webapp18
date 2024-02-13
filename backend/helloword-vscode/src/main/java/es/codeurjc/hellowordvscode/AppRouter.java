package es.codeurjc.hellowordvscode;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AppRouter {
    @GetMapping("/")
    public String initial(Model model) {

        model.addAttribute("name","World");
        return "index";
    }

    @GetMapping("/profile")
    public String profile(Model model) {

        model.addAttribute("name","World");
        return "profile";
    }

    @GetMapping("/pool")
    public String pool(Model model) {

        model.addAttribute("name","World");
        return "pool";
    }
    
}
