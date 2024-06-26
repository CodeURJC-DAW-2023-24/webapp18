package es.codeurjc.controller;

import java.util.List;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.model.Pool;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.PoolService;
import es.codeurjc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OfferController {
    @Autowired
    private PoolService poolService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @GetMapping("/offers")
    public String showOffers(Model model, HttpServletRequest request, Pageable page) {
        model.addAttribute("employer", request.isUserInRole("EMP"));

        // will be good implement the hasMore and nextPage attributes here
        return "offers";
    }

    @GetMapping("/offers/load")
    public String loadOffers(HttpServletRequest request, Model model, @RequestParam("page") int pageNumber,
            @RequestParam("size") int size) {
        Page<Offer> offers = offerService.findAll(PageRequest.of(pageNumber, size));

        model.addAttribute("offers", offers);
        model.addAttribute("hasMore", offers.hasNext());
        model.addAttribute("alternative", "No hay ofertas");
        return "offer_cards";
    }

    @GetMapping("/offer")
    public String offer(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        Offer offer = offerService.findById(id).get();
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;

        model.addAttribute("mail", username);
        model.addAttribute("offer", offer);
        model.addAttribute("id", offer.getId());
        model.addAttribute("apply", request.isUserInRole("LIFE") && !offer.isOffered(username));
        model.addAttribute("withdraw", offer.isOffered(username));

        boolean flag = false;
        if (offer.getEmployer() != null && request.getUserPrincipal() != null)
            flag = offer.getEmployer().getMail().equals(username);
        model.addAttribute("canEdit", request.isUserInRole("ADMIN") || flag);

        return "offer";
    }

    @GetMapping("/offer/add")
    public String newOffer(Model model, HttpServletRequest request) {
        Collection<Pool> pools = poolService.findAll();
        LocalDate date = LocalDate.now();

        model.addAttribute("pools", pools);
        model.addAttribute("salary", 1200);
        model.addAttribute("start", date);

        return "offer_form";
    }

    @PostMapping("/offer/add")
    public String addOffer(Model model, HttpServletRequest request) {
        try {
            offerService.checkOfferRequest(request, true);
        } catch (Exception e) {
            return offerService.showError(model, e.getMessage());
        }

        int poolId = Integer.parseInt(request.getParameter("pool-id"));
        Pool pool = poolService.findById(poolId).get();
        String mail = request.getUserPrincipal().getName();
        Employer employer = userService.findEmployerByEmail(mail).get();
        Offer offer = offerService.createOffer(pool, request);
        offer.addEmployer(employer);
        employer.addOffer(offer);
        offerService.save(offer);
        userService.saveEmployer(employer);
        pool.addOffer(offer);
        poolService.save(pool);

        return "redirect:/offer/added?id=" + offer.getId();
    }

    @GetMapping("/offer/added")
    public String addedOffer(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        model.addAttribute("title", "Oferta añadida");
        model.addAttribute("message", "Oferta añadida correctamente. ¡Gracias por confiar en nosotros!");
        model.addAttribute("back", "/offer?id=" + id);
        return "feedback";
    }

    @GetMapping("/offer/edit")
    public String editOffer(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        Offer offer = offerService.findById(id).get();
        String start = offerService.formattedDate(offer.getStart());

        model.addAttribute("offer", offer);
        model.addAttribute("start", start);
        model.addAttribute("salary", offer.getSalary());
        model.addAttribute("edit", true);
        return "offer_form";
    }

    @PostMapping("/offer/edit")
    public String updateOffer(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        try {
            offerService.checkOfferRequest(request, false);
        } catch (Exception e) {
            return offerService.showError(model, e.getMessage());
        }

        Offer offer = offerService.findById(id).get();

        offerService.updateOffer(offer, request);
        offerService.resetOffer(offer);
        offerService.save(offer);

        return "redirect:/offer/edited?id=" + offer.getId();
    }

    @GetMapping("/offer/edited")
    public String editedOffer(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        model.addAttribute("title", "Oferta actualizada");
        model.addAttribute("message", "Oferta actualizada correctamente.");
        model.addAttribute("back", "/offer?id=" + id);
        return "feedback";
    }

    @GetMapping("/offer/offered/load")
    public String loadOffered(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        Offer offer = offerService.findById(id).get();
        List<Lifeguard> lifeguards = offer.getLifeguards();
        for (Lifeguard lifeguard : lifeguards) {
            lifeguard.setOfferAssigned(lifeguard.hasOfferAssigned(offer));
        }

        model.addAttribute("offer", offer);
        model.addAttribute("lifeguards", lifeguards);
        model.addAttribute("id", id);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        model.addAttribute("employer", request.isUserInRole("EMP"));
        return "offer_applications";
    }

    @PostMapping("/offer/offered/new")
    public String newOffered(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        Offer offer = offerService.findById(id).get();
        Lifeguard lifeguard = userService.findLifeguardByEmail(request.getUserPrincipal().getName()).get();
        offer.addOffered(lifeguard);
        lifeguard.addOffer(offer);
        offerService.save(offer);
        userService.saveLifeguard(lifeguard);

        return "redirect:/offer?id=" + offer.getId();
    }

    @PostMapping("/offer/offered/withdraw")
    public String LifeguardDessapply(Model model, HttpServletRequest request,
            @RequestParam("ido") int id,
            @RequestParam("lg") String username,
            @RequestParam(name = "profile", required = false) Boolean profile) {

        Offer offer = offerService.findById(id).get();
        Lifeguard lifeguard = userService.findLifeguardByEmail(username).get();

        if (offer.getLifeguard() == lifeguard) {
            offer.setLifeguard(null);
            lifeguard.deleteOfferAccepted(offer);
        }
        offer.deleteOffered(lifeguard);
        lifeguard.deleteOffer(offer);

        offerService.save(offer);
        userService.saveLifeguard(lifeguard);

        if (profile != null) {
            return "redirect:/login";
        }
        return "redirect:/offer?id=" + id;
    }

    @PostMapping("/offer/offered/set")
    public String LifeguardSetted(@RequestParam("ido") int id, @RequestParam("lg") String lg, Model model,
            HttpServletRequest request) {
        Offer offer = offerService.findById(id).get();
        Lifeguard lifeguard = userService.findLifeguardByEmail(lg).get();
        offer.setLifeguard(lifeguard);
        lifeguard.addOfferAccepted(offer);
        offerService.save(offer);
        userService.saveLifeguard(lifeguard);

        return "redirect:/offer?id=" + id;
    }

    @PostMapping("/offer/offered/delete")
    public String LifeguardDeleted(@RequestParam("ido") int id, @RequestParam("lg") String lg, Model model,
            HttpServletRequest request) {
        Offer offer = offerService.findById(id).get();
        Lifeguard lifeguard = userService.findLifeguardByEmail(lg).get();
        offer.setLifeguard(null);
        offerService.save(offer);
        lifeguard.deleteOfferAccepted(offer);
        userService.saveLifeguard(lifeguard);

        return "redirect:/offer?id=" + id;
    }

    @PostMapping("/offer/delete")
    public String offerDelete(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        offerService.deleteById(id);

        return "redirect:/offers";
    }
}
