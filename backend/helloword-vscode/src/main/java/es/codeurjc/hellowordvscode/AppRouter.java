package es.codeurjc.hellowordvscode;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import es.codeurjc.dataClasses.*;
import es.codeurjc.hellowordvscode.DataBase;
@Controller
public class AppRouter {

    @GetMapping("/")
    public String initial(Model model) {

        System.out.println("LOG DEL ROUTER INICIAL");

        DataBase db = new DataBase();
        pool pool = db.getPool(0);
        model.addAttribute("name",pool.name);
        model.addAttribute("start",pool.scheduleStart);
        model.addAttribute("end",pool.scheduleEnd);
        model.addAttribute("desc",pool.description);
        model.addAttribute("pic",pool.pic);
        model.addAttribute("afor",pool.afor);
        return "pool";
    }

    @GetMapping("/pool")
    public String pool(Model model) {
        DataBase db = new DataBase();
        pool pool = db.getPool(0);
        model.addAttribute("name",pool.name);
        model.addAttribute("start",pool.scheduleStart);
        model.addAttribute("end",pool.scheduleEnd);
        model.addAttribute("desc",pool.description);
        model.addAttribute("pic",pool.pic);
        model.addAttribute("afor",pool.afor);
        return "pool";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        DataBase db = new DataBase();
        person pers = db.getPerson(0);
        model.addAttribute("person",pers);
        return "profile";
    }
    
}
