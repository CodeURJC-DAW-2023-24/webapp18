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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AppRouter {
    private DataBase db = new DataBase();

    @Autowired
    private UserService userService;

    // -------------------------------------- MAIN --------------------------------------
    @GetMapping("/")
    public String initial(Model model) {
        return "index";
    }

    // -------------------------------------- OFFERS --------------------------------------
    @GetMapping("/loadOffers")
    public String loadOffers(HttpServletRequest request, Model model) {
        int from = Integer.parseInt(request.getParameter("from"));
        int amount = Integer.parseInt(request.getParameter("amount"));

        Offer[] offers = DataBase.getOffers(from, amount);
        model.addAttribute("offers", offers);
        model.addAttribute("alternative", "No hay ofertas aún");
        return "offers";
    }

    @GetMapping("/allOffersLoaded")
    public ResponseEntity<?> allOffersLoaded() {
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
    public String newUser(HttpSession session, Model model) {
            /*, Lifeguard lifeguard, Employer employer, String typeUser, boolean reliability,
            boolean effort, boolean communication, boolean attitude, boolean problemsResolution, boolean leadership) {
        if ("employer".equals(typeUser)) {
            userService.saveEmployer(employer);
            session.setAttribute("message", "Nuevo empleado creado correctamente");
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
            session.setAttribute("message", "Nuevo socorrista creado correctamente");
        } else {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Tienes que seleccionar si eres un socorrista o un empleado");
            model.addAttribute("back", "javascript:history.back()");

            return "message";
        }
        */
        session.setAttribute("message", "Usuario registrado correctamente");

        return "redirect:/user/registered";
    }

    @GetMapping("/user/registered")
    public String registeredUser(HttpSession session, Model model) {
        String message = (String) session.getAttribute("message");

        model.addAttribute("title", "Usuario registrado");
        model.addAttribute("message", message);
        model.addAttribute("back", "/profile");
        return "message";
    }

    // -------------------------------------- POOL --------------------------------------
    @GetMapping("/pool")
    public String pool(@RequestParam("id") int id, Model model) {
        Pool pool = DataBase.getPool(id);

        model.addAttribute("pool", pool);
        return "pool";
    }

    @GetMapping("/pool/message/load")
    public String loadMessages(@RequestParam("id") int id, Model model) {
        Pool pool = DataBase.getPool(id);
        Message[] messages = pool.getMessages();

        model.addAttribute("messages", messages);
        model.addAttribute("poolId", id);
        return "poolMessage";
    }

    @PostMapping("/pool/message/add")
    @ResponseBody
    public void newMessage(@RequestParam("msg") String input, @RequestParam("id") int id, Model model) {
        Message message = new Message("null", input);
        Pool pool = DataBase.getPool(id);

        pool.addMessage(message);
    }

    @PostMapping("/pool/message/delete")
    @ResponseBody
    public void deletePoolMessage(@RequestParam("idP") int idP, @RequestParam("idM") int idM, Model model) {
        Pool pool = DataBase.getPool(idP);

        pool.deleteMessage(idM);
    }
}