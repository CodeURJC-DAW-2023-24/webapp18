package es.codeurjc.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
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

    @GetMapping("/loadOffers")
    public String loadOffers(HttpServletRequest request, Model model) {
        int from = Integer.parseInt(request.getParameter("from"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        Collection<Offer> offers2 = offerService.findAll();
        List<Offer> offersL = new ArrayList(offers2);
        List<Offer> offerSubL = offersL.subList(from, amount);
        model.addAttribute("offers", offerSubL);
        model.addAttribute("alternative", "No hay ofertas aún");
        return "offers";
    }

    @GetMapping("/allOffersLoaded")
    public ResponseEntity<?> allOffersLoaded() {
        boolean value = false; //TO IMPLEMENT JORGE
        return ResponseEntity.ok().body(Map.of("value", value));
    }

    @GetMapping("/offer")
    public String offer(@RequestParam("id") int id, Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        Offer offer = offerService.findById(id).get();
        model.addAttribute("offer", offer);
        model.addAttribute("id", offer.getId());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        model.addAttribute("lifeguard", request.isUserInRole("LIFE") && !offer.isOffered(request.getUserPrincipal().getName()) && ! request.getUserPrincipal().getName().equals("admin"));
        model.addAttribute("employer", request.isUserInRole("EMP"));
        return "offer";
    }

    @GetMapping("/offer/form")
    public String newOffer(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }        

        return "new_offer";
    }

    @PostMapping("/offer/add")
    public String addOffer(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }        

        return "redirect:/offer/added";
    }
    


    @GetMapping("/offer/added")
    public String addedOffer(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

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
}
