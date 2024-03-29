package es.codeurjc.controller;

import java.util.List;
import java.util.Optional;
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

    // -------------------------------------- MAIN --------------------------------------
    @GetMapping("/")
    public String showOffers(Model model, HttpServletRequest request, Pageable page) {
        // CHECK USER LOGED OR NOT
        model.addAttribute("loged", request.getUserPrincipal() != null);
        model.addAttribute("employer", request.isUserInRole("EMP"));

        // will be good implement the hasMore and nextPage attributes here
        return "index";
    }

    // -------------------------------------- OFFER --------------------------------------
    @GetMapping("/offers/load")
    public String loadOffers(HttpServletRequest request, Model model,  @RequestParam("page") int pageNumber, @RequestParam("size") int size) {

        Page<Offer> offers = offerService.findAll(PageRequest.of(pageNumber, size));

        model.addAttribute("offers", offers);
        model.addAttribute("hasMore", offers.hasNext());
        model.addAttribute("alternative", "No hay ofertas");
        return "offers";
    }

    @GetMapping("/offer")
    public String offer(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        //CHECK USER LOGED OR NOT
        model.addAttribute("loged", request.getUserPrincipal() != null);

        Offer offer = offerService.findById(id).get();
        Pool pool = offer.getPool();
        if(pool.getPhotoUser()!=null) model.addAttribute("hasPhoto", true);
        else model.addAttribute("hasPhoto", false);
        model.addAttribute("offer", offer);
        model.addAttribute("id", offer.getId());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        model.addAttribute("lifeguard", request.isUserInRole("LIFE") && !offer.isOffered(request.getUserPrincipal().getName()) && ! request.getUserPrincipal().getName().equals("admin"));
        model.addAttribute("employer", request.isUserInRole("EMP"));
        boolean flag = false;
        if(offer.getEmployer()!=null) flag = offer.getEmployer().getMail().equals(request.getUserPrincipal().getName());
        model.addAttribute("canEdit", request.isUserInRole("ADMIN")||flag);
        return "offer";
    }

    @GetMapping("/offer/form")
    public String newOffer(Model model, HttpServletRequest request) {
        //CHECK USER LOGED OR NOT
        model.addAttribute("loged", request.getUserPrincipal() != null);

        Collection<Pool> pools = PoolService.findAll();
        model.addAttribute("pools", pools);
        return "new_offer";
    }

    @PostMapping("/offer/add")
    public String addOffer(Model model, HttpServletRequest request) {
        //CHECK USER LOGED OR NOT
        model.addAttribute("loged", request.getUserPrincipal() != null);

        try {
            offerService.checkOffer(request);
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

        return "redirect:/offer/added";
    }

    @GetMapping("/offer/added")
    public String addedOffer(Model model, HttpServletRequest request) {
        //CHECK USER LOGED OR NOT
        model.addAttribute("loged", request.getUserPrincipal() != null);

        model.addAttribute("title", "Oferta añadida");
        model.addAttribute("message", "Oferta añadida correctamente. ¡Gracias por confiar en nosotros!");
        model.addAttribute("back", "/");
        return "message";
    }


    @GetMapping("/offer/offered/load")
    public String loadOffered(@RequestParam("id") int id, Model model,HttpServletRequest request) {
        Offer offer = offerService.findById(id).get();
        List<Lifeguard> lifeguards = offer.getLifeguards();
        //Collection<Message> messagesBD = messageService.findAll();

        if(offer.getLifeguard()!=null){
            for (Lifeguard lifeguard: lifeguards){
                if(offer.getLifeguard().getMail().equals(lifeguard.getMail())){
                    lifeguard.setofferAssigned(true);
                    break;
                }
            }
        }

        model.addAttribute("offer", offer);
        model.addAttribute("lifeguards",lifeguards);
        model.addAttribute("id", id);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        model.addAttribute("employer", request.isUserInRole("EMP"));
        return "offered";
    }

    @PostMapping("/offer/offered/new")
    public String newOffered(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        //   Pool pool = DataBase.getPool(id);
        Offer offer = offerService.findById(id).get();
        Lifeguard lifeguard = userService.findLifeguardByEmail(request.getUserPrincipal().getName()).get();
        offer.addOffered(lifeguard);
        lifeguard.addOffer(offer);
        offerService.save(offer);
        userService.saveLifeguard(lifeguard);
     
        model.addAttribute("offer", offer);
        model.addAttribute("id", offer.getId());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        model.addAttribute("lifeguard", false);
        model.addAttribute("employer", request.isUserInRole("EMP"));
        return "offer";
    }
    @PostMapping("/offer/offered/set")
    public String LifeguardSetted(@RequestParam("ido") int id,@RequestParam("lg") String lg, Model model, HttpServletRequest request) {
        //   Pool pool = DataBase.getPool(id);
        Offer offer = offerService.findById(id).get();
        Lifeguard lifeguard = userService.findLifeguardByEmail(lg).get();
        offer.setLifeguard(lifeguard);
        lifeguard.addOfferAccepted(offer);
        offerService.save(offer);
        userService.saveLifeguard(lifeguard);
        
        return "redirect:/offer?id="+id;
    }

    @PostMapping("/offer/offered/delete")
    public String LifeguardDeleted(@RequestParam("ido") int id,@RequestParam("lg") String lg, Model model, HttpServletRequest request) {
        //   Pool pool = DataBase.getPool(id);
        Offer offer = offerService.findById(id).get();
        Lifeguard lifeguard = userService.findLifeguardByEmail(lg).get();
        offer.setLifeguard(null);
        offerService.save(offer);
        lifeguard.deleteOfferAccepted(offer);
        userService.saveLifeguard(lifeguard);
        
        return "redirect:/offer?id="+id;
    }

    @PostMapping("/offer/delete")
    public String offerDelete(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        //   Pool pool = DataBase.getPool(id);
        Offer offer = offerService.findById(id).get();
        offerService.deleteById(id);
        model.addAttribute("loged", request.getUserPrincipal() != null);
        model.addAttribute("employer", request.isUserInRole("EMP"));
        return "index";
    }
}
