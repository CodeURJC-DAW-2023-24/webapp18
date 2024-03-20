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
import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.model.Pool;
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

    @GetMapping("/api/offers/{id}")
    public OfferDTO getOffer(@PathVariable int id){ 
        Optional<Offer> offer = offerService.findById(id);
        if (offer.isPresent()) return (new OfferDTO(offer.get()));
        else return null;
    }

    @DeleteMapping("/api/offers/{id}")
    public String deleteOffer(@PathVariable int id){ //Only for admin and owner
        offerService.deleteById(id);
        return "Se ha borrado correctamente";
    }
    @PutMapping("/api/offers/{id}")
    public String editOffer(@PathVariable int id){ //Only for admin and owner
        //GEORGE MANIN YOU HAVE TO IMPLEMENT THIS MF PETITION
        return "Se ha editado correctamente";
    }
    @GetMapping("/api/offers/{id}/lifeguards")
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
    @DeleteMapping("/api/offers/{id}/lifeguards") //Only for admin and owner
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
    @PostMapping("/api/offers/{id}/lifeguards") //Only lifeguards not already applyed
    public String NewApply(@PathVariable int id) {
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
    @PutMapping("/api/offers/{id}/lifeguards") //Only for admin and owner
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
    @PostMapping("/api/offers")
    public String postMethodName(@RequestBody OfferDTO offerDTO) {
        Offer offer = offerFromDTO(offerDTO);
        Pool pool = poolService.findById(offerDTO.getPoolID()).get();
        Employer employer = userService.findEmployerByEmail("e1").get();
        offer.addEmployer(employer);
        employer.addOffer(offer);
        offerService.save(offer);
        userService.saveEmployer(employer);
        pool.addOffer(offer);
        poolService.save(pool);
        return "hola";
    }
    

public Offer offerFromDTO(OfferDTO o){
    Pool pool = poolService.findById(o.getPoolID()).get();
    Offer offer = new Offer.Builder()
    .pool(pool)
    .salary(o.getSalary())
    .start(o.getStart())
    .type(o.getType())
    .description(o.getDescription())
    .build();
    return offer;
}
    
}
