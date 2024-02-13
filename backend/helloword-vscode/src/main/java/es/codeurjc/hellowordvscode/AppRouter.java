package es.codeurjc.hellowordvscode;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import es.codeurjc.dataClasses.pool;
import es.codeurjc.hellowordvscode.DataBase;
@Controller
public class AppRouter {

    @GetMapping("/")
    public String initial(Model model) {

        System.out.println("LOG DEL ROUTER INICIAL");

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
    
}
