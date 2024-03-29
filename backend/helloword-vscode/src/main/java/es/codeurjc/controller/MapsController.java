package es.codeurjc.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.PoolService;
import es.codeurjc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MapsController {
    @Autowired
    private PoolService poolService;  // we need the pools before the offers

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @GetMapping("/maps")
    public String maps(Model model, HttpServletRequest request) {
        //CHECK USER LOGED OR NOT
        Boolean loged = request.getUserPrincipal() != null;
        model.addAttribute("loged", loged);

        if (loged) {
            String mail = request.getUserPrincipal().getName();
            Optional<Lifeguard> lifeguard = userService.findLifeguardByEmail(mail);
            Optional<Employer> employer = userService.findEmployerByEmail(mail);
            if (lifeguard.isPresent())
                model.addAttribute("user", lifeguard.get());
            if (employer.isPresent())
                model.addAttribute("user", employer.get());
        }

        Collection<Offer> offers = offerService.findAll();
        model.addAttribute("offers", offers);
        return "maps";
    }
}
