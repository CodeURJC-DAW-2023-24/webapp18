package es.codeurjc.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.DTO.OfferDTO;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.PoolService;
import es.codeurjc.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
public class OfferRestController {
     @Autowired
    private PoolService poolService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @GetMapping("/api/offer/{id}")
    public OfferDTO getOffer(@PathVariable int id){
        Optional<Offer> offer = offerService.findById(id);
        if (offer.isPresent()) return (new OfferDTO(offer.get()));
        else return null;
    }

    @DeleteMapping("/api/offer/{id}")
    public String deleteOffer(@PathVariable int id){
        offerService.deleteById(id);
        return "Se ha borrado correctamente";
    }
    @PutMapping("/api/offer/{id}")
    public String editOffer(@PathVariable int id){
        //GEORGE MANIN YOU HAVE TO IMPLEMENT THIS MF PETITION
        return "Se ha editado correctamente";
    }
    @GetMapping("/api/offer/{id}/lifeguards")
    public HashMap<String, ArrayList<String>> getLifeguards(@PathVariable int id){
        Optional<Offer> offer = offerService.findById(id);
        if (offer.isPresent()){
            HashMap mapa = new HashMap<>();
            if(offer.get().getLifeguard()!=null){
                String lifeguard = offer.get().getLifeguard().getMail();
                ArrayList l1 = new ArrayList<>();
                l1.add(lifeguard);
                mapa.put("Seleccionado",l1);
            }     
            else   mapa.put("Seleccionado",null);     
            ArrayList l2 = new ArrayList<>();
            for(Lifeguard l: offer.get().getLifeguards()){
                l2.add(l.getMail());
            }
            mapa.put("Propuestos", l2);
            return mapa;

        }
        else return null;
    }
    @DeleteMapping("/api/offer/{id}/lifeguards") //Only for admin and owner
    public String unSelectProposed(@PathVariable int id){
        Optional<Offer> offer = offerService.findById(id);
        if (offer.isPresent()){
            if(offer.get().getLifeguard()!=null){
                offer.get().setLifeguard(null);
                offerService.save(offer.get());
                Lifeguard l = offer.get().getLifeguard();
                l.setofferAssigned(false);
                l.deleteOfferAccepted(offer.get());
                userService.saveLifeguard(l);
            }
        }
        return "Se ha quitado el socorrista de la oferta";
    }
    @PutMapping("/api/offer/{id}/lifeguards")
    public String putNewApply(@PathVariable int id) {
        Lifeguard l = userService.findLifeguardByEmail(null).get(); //lo catcheas
        Optional<Offer> offer = offerService.findById(id);
        if (offer.isPresent()){
            offer.get().addOffered(l);
            offerService.save(offer.get());
            l.addOffer(offer.get());
            userService.saveLifeguard(l);
        }
        
        return "Has aplicado correctamente";
    }
    @PostMapping("/api/offer/{id}/lifeguards")
    public String selectLifeguard(@RequestBody int id) {
        Lifeguard l = userService.findLifeguardByEmail(null).get(); //lo catcheas
        Optional<Offer> offer = offerService.findById(id);
        if (offer.isPresent()){
            offer.get().setLifeguard(l);;
            offerService.save(offer.get());
            l.setofferAssigned(true);
            l.addOfferAccepted(offer.get());
            userService.saveLifeguard(l);
        }
        
        return "Selecteado correctamente";
    }
    
}
