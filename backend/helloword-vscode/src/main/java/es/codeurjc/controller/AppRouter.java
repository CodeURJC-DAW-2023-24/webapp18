package es.codeurjc.controller;

import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Message;
import es.codeurjc.model.Offer;
import es.codeurjc.model.Person;
import es.codeurjc.model.Pool;
import es.codeurjc.repository.DataBase;
import es.codeurjc.service.UserService;

@Controller
public class AppRouter {
    private DataBase db = new DataBase();

    @Autowired
    private UserService userService;

    // -------------------------------------- MAIN --------------------------------------
    @GetMapping("/")
    public String initial(Model model) {
        System.out.println("LOG DEL ROUTER INICIAL");
        return "index";
    }

    // -------------------------------------- OFFERS --------------------------------------
    @GetMapping("/loadOffers")
    public String loadPlayers(@RequestParam("from") int from, @RequestParam("amount") int amount, Model model) {
        Offer[] offers = DataBase.getOffers(from, amount);
        model.addAttribute("offers", offers);
        model.addAttribute("alternative", "No hay ofertas aún");
        return "offers";
    }

    @GetMapping("/allOffersLoaded")
    public ResponseEntity<?> allPlayersLoaded() {
        boolean value = DataBase.allOffersLoaded();
        return ResponseEntity.ok().body(Map.of("value", value));
    }

    @GetMapping("/offer")
    public String offer(@RequestParam("id") int id, Model model) {
        Offer offer = DataBase.getOffer(id);
        model.addAttribute("offer", offer);
        return "offer";
    }

    // -------------------------------------- PROFILE --------------------------------------
    @GetMapping("/profile")
    public String profile(Model model) {
        Person pers = db.getPerson(0);
        model.addAttribute("person", pers);
        return "profile";
    }

    @GetMapping("/newUser")
    public String loadNewUser(Model model) {
        return "newUser";
    }

    @PostMapping("/newUser")
    public String newUser(Model model, Lifeguard lifeguard, Employer employer, String typeUser, boolean reliability,
            boolean effort, boolean communication, boolean attitude, boolean problemsResolution, boolean leadership)
            throws IOException {
        if ("employer".equals(typeUser)) {
            userService.saveEmployer(employer);
            model.addAttribute("message", "Nuevo empleado creado correctamente");
        } else if ("lifeguard".equals(typeUser)) {
            userService.saveLifeguard(lifeguard);
            List<String> skills = new ArrayList<>();
            if (reliability) {
                skills.add("Confianza");
            }
            if (effort) {
                skills.add("Esfuerzo");
            }
            if (communication) {
                skills.add("Comunicación");
            }
            if (attitude) {
                skills.add("Actitud positiva");
            }
            if (problemsResolution) {
                skills.add("Resolución de problemas");
            }
            if (leadership) {
                skills.add("Liderazgo");
            }
            lifeguard.setSkills(skills);
            model.addAttribute("message", "Nuevo socorrista creado correctamente");
        } else {
            model.addAttribute("message", "Tienes que seleccionar si eres un socorrista o un empleado");
        }

        return "savedUser";
    }

    // -------------------------------------- POOL --------------------------------------
    @GetMapping("/pool")
    public String pool(Model model) {
        Pool pool = db.getPool(0);
        model.addAttribute("pool", pool);
        model.addAttribute("nMessages", pool.messages.size());
        return "pool";
    }

    @GetMapping("/pagePartPoolMsg")
    public String ppPoolMsg(@RequestParam("id") int id, Model model) {
        System.out.println("PETICION DE PP POOL RECIBIDA");
        Message m = db.getMessage(id);
        model.addAttribute("message", m);
        model.addAttribute("id", id);
        return "poolMessage";
    }

    @PostMapping("/newMsg")
    @ResponseBody
    public String newMsg(@RequestParam("msg") String input, Model model) {
        Message m = new Message("null", input);
        db.addMessage(m);
        System.out.println("Añadido: " + input);
        return db.getPool(0).messages.size() + "";
    }

    @PostMapping("/deleteMsg")
    @ResponseBody
    public String deletePoolMsg(@RequestParam("id") int id) {
        db.deleteMsg(id);
        Pool p = db.getPool(0);
        return p.messages.size() + "";
    }
}