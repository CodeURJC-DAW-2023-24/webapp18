package es.codeurjc.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.repository.OfferRepository;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MapsController {
    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @Autowired
    private OfferRepository offerRepository;

    @GetMapping("/")
    public String maps(Model model, HttpServletRequest request) {
        Boolean logged = request.getUserPrincipal() != null;
        model.addAttribute("logged", logged);

        if (logged) {
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
        return "index";
    }

    @GetMapping("/maps/offers")
    public String loadOffers(@RequestParam("address") String address, Model model, HttpServletRequest request) {
        List<Offer> offers = offerRepository.findByPoolDirection(address);

        model.addAttribute("address", address);
        model.addAttribute("offers", offers);
        model.addAttribute("alternative", "No hay ofertas");
        return "maps_offers";
    }
}
