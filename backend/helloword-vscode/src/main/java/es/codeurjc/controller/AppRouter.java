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

    @GetMapping("/offer/form")
    public String newOffer(Model model) {
        return "new_offer";
    }

    @PostMapping("/offer/add")
    public String addOffer(Model model) {
        return "redirect:/offer/added";
    }

    @GetMapping("/offer/added")
    public String addedOffer(Model model) {
        model.addAttribute("title", "Oferta añadida");
        model.addAttribute("message", "Oferta añadida correctamente. ¡Gracias por confiar en nosotros!");
        model.addAttribute("back", "/");
        return "message";
    }

    // -------------------------------------- PROFILE --------------------------------------
    @GetMapping("/profile")
    public String profile(Model model) {
        Person person = DataBase.getPerson(0);
        model.addAttribute("user", person);
        return "profile";
    }

    @GetMapping("/login")
    public String sign(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model) {
        return "redirect:/loged";
    }

    @GetMapping("/loged")
    public String loged(Model model) {
        model.addAttribute("title", "Sesión iniciada");
        model.addAttribute("message", "Has iniciado sesión correctamente");
        model.addAttribute("back", "/profile");
        return "message";
    }

    @GetMapping("/user/form")
    public String loadNewUser(Model model) {
        return "new_user";
    }

    @PostMapping("/user/register")
    public String newUser(Model model) { /*, Lifeguard lifeguard, Employer employer, String typeUser, boolean reliability,
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
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Tienes que seleccionar si eres un socorrista o un empleado");
            model.addAttribute("back", "javascript:history.back()");

            return "message";
        }
        */

        return "redirect:/user/registered";
    }

    @GetMapping("/user/registered")
    public String registeredUser(Model model) {
        model.addAttribute("title", "Usuario registrado");
        model.addAttribute("message", "Usuario registrado correctamente");
        model.addAttribute("back", "/profile");
        return "message";
    }

    // -------------------------------------- POOL --------------------------------------
    @GetMapping("/pool")
    public String pool(@RequestParam("id") int id, Model model) {
        Pool pool = DataBase.getPool(id);

        model.addAttribute("pool", pool);
        model.addAttribute("nMessages", pool.getMessages().length);
        return "pool";
    }

    @GetMapping("/pool/message/load")
    public String ppPoolMsg(@RequestParam("idP") int idP, @RequestParam("idM") int idM, Model model) {
        Pool pool = DataBase.getPool(idP);
        Message message = pool.getMessage(idM);

        model.addAttribute("messages", message);
        model.addAttribute("messageId", idM);
        model.addAttribute("poolId", idP);
        return "poolMessage";
    }

    @PostMapping("/pool/message/add")
    @ResponseBody
    public String newMsg(@RequestParam("msg") String input, @RequestParam("id") int id, Model model) {
        Message message = new Message("null", input);
        Pool pool = DataBase.getPool(id);

        pool.addMessage(message);
        return pool.getMessages().length + "";
    }

    @PostMapping("/pool/message/delete")
    @ResponseBody
    public String deletePoolMsg(@RequestParam("idP") int idP, @RequestParam("idM") int idM, Model model) {
        Pool pool = DataBase.getPool(idP);

        pool.deleteMessage(idM);
        return pool.getMessages().length + "";
    }
}