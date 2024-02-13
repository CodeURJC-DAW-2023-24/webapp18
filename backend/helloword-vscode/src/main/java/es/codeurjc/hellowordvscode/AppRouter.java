package es.codeurjc.hellowordvscode;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.dataClasses.*;
import es.codeurjc.hellowordvscode.DataBase;
@Controller
public class AppRouter {

    @GetMapping("/")
    public String initial(Model model) {

        System.out.println("LOG DEL ROUTER INICIAL");  
        DataBase db = new DataBase();
        pool pool = db.getPool(0);
        model.addAttribute("pool",pool);
        model.addAttribute("nMessages",pool.messages.size());
        return "pool";
    }

    @GetMapping("/pool")
    public String pool(Model model) {
        DataBase db = new DataBase();
        pool pool = db.getPool(0);
        model.addAttribute("pool",pool);
        return "pool";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        DataBase db = new DataBase();
        person pers = db.getPerson(0);
        model.addAttribute("person",pers);
        return "profile";
    }
    @GetMapping("/pagePartPoolMsg")
    public String ppPoolMsg(@RequestParam("id") int id ,Model model) {
        System.out.println("PETICION DE PP POOL RECIBIDA");
        DataBase db = new DataBase();
        Message m = db.getMessage(id);
        model.addAttribute("message",m);
        return "poolMessege";
    }
    
}
