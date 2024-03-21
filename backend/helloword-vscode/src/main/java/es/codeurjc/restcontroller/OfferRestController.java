package es.codeurjc.restcontroller;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<OfferDTO> getOffer(@PathVariable int id){ 
        Optional<Offer> offer = offerService.findById(id);
        if (offer.isPresent()) return ResponseEntity.status(HttpStatus.OK).body(new OfferDTO(offer.get()));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/api/offers/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable int id, Principal principal){ //Only for admin and owner
        if(principal !=null){
            Optional<Employer> eOP = userService.findEmployerByEmail(principal.getName());
            if(eOP.isPresent()){
                Optional<Offer> offer = offerService.findById(id);
                Employer e = eOP.get();
                if(offer.isPresent()){
                    if(isOwner(offer.get(), e.getMail()) || isAdmin(e.getMail())){
                        offerService.deleteById(id);
                        return ResponseEntity.status(HttpStatus.OK).build();
                    }
                    else{
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Error-Message", "No eres propietario de esta oferta");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build();
                    }
                }
                else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PutMapping("/api/offers/{id}")
    public String editOffer(@PathVariable int id){ //Only for admin and owner
        //GEORGE MANIN YOU HAVE TO IMPLEMENT THIS MF PETITION :3
        return "Se ha editado correctamente";
    }
    @GetMapping("/api/offers/{id}/lifeguards")
    public ResponseEntity<HashMap<String, ArrayList<String>>> getLifeguards(@PathVariable int id, Principal principal){
        if(principal !=null){
            Optional<Employer> eOP = userService.findEmployerByEmail(principal.getName());
            if(eOP.isPresent()){
                Optional<Offer> offer = offerService.findById(id);
                Employer e = eOP.get();
                if(offer.isPresent()){
                    if(isOwner(offer.get(), e.getMail()) || isAdmin(e.getMail())){
                        
                        HashMap mapa = buildMap(offer.get());
                        return ResponseEntity.status(HttpStatus.OK).body(mapa);
                    }
                    else{
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Error-Message", "No eres propietario de esta oferta");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build();
                    }
                }
                else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        
    }
    @DeleteMapping("/api/offers/{id}/lifeguards") //Only for admin and owner
    public ResponseEntity<HashMap<String, ArrayList<String>>> unSelectProposed(@PathVariable int id, Principal principal){

        if(principal !=null){
            Optional<Employer> eOP = userService.findEmployerByEmail(principal.getName());
            if(eOP.isPresent()){
                Optional<Offer> offer = offerService.findById(id);
                Employer e = eOP.get();
                if(offer.isPresent()){
                    if(isOwner(offer.get(), e.getMail()) || isAdmin(e.getMail())){
                        if(offer.get().getLifeguard()!=null){
                            Lifeguard l = offer.get().getLifeguard();
                            offer.get().setLifeguard(null);
                            offerService.save(offer.get());
                            l.setofferAssigned(false);
                            l.deleteOfferAccepted(offer.get());
                            userService.saveLifeguard(l);
                            HashMap mapa = buildMap(offer.get());
                            return ResponseEntity.status(HttpStatus.OK).body(mapa);
                        }
                        else{
                            HttpHeaders headers = new HttpHeaders();
                            headers.add("Error-Message", "No hay ningun socorrista seleccionado");
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build(); 
                        }
                            

                    }
                    else{
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Error-Message", "No eres propietario de esta oferta");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build();
                    }
                }
                else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
    @PostMapping("/api/offers/{id}/lifeguards") //Only lifeguards not already applyed
    public ResponseEntity<OfferDTO> NewApply(@PathVariable int id, Principal principal) {

        if (principal !=null){
            Optional<Lifeguard> lOP = userService.findLifeguardByEmail(principal.getName());
            if (lOP.isPresent()){
                Optional<Offer> offer = offerService.findById(id);
                Lifeguard l = lOP.get();
                if (offer.isPresent()){
                    if(!offer.get().isOffered(l.getMail())){
                        offer.get().addOffered(l);
                        offerService.save(offer.get());
                        l.addOffer(offer.get());
                        userService.saveLifeguard(l);
                        return ResponseEntity.status(HttpStatus.OK).body(new OfferDTO(offer.get()));
                    }
                    else{
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Error-Message", "Ya has aplicado");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build();                        
                    }
                    
                
                }
                else return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //offer not found
            }
            else  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // user not found
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //not logged
        
    }
    @PutMapping("/api/offers/{id}/lifeguards/{nSelected}") //Only for admin and owner
    public ResponseEntity<HashMap<String, ArrayList<String>>> selectLifeguard(@PathVariable int id, @PathVariable int nSelected, Principal principal) {
        if(principal !=null){
            Optional<Employer> eOP = userService.findEmployerByEmail(principal.getName());
            if(eOP.isPresent()){
                Optional<Offer> offer = offerService.findById(id);
                Employer e = eOP.get();
                if(offer.isPresent()){
                    if(isOwner(offer.get(), e.getMail()) || isAdmin(e.getMail())){
                        if(nSelected<offer.get().getLifeguards().size()){
                            Lifeguard l = offer.get().getLifeguards().get(nSelected);
                            offer.get().setLifeguard(l);
                            offerService.save(offer.get());
                            l.setofferAssigned(true);
                            l.addOfferAccepted(offer.get());
                            userService.saveLifeguard(l);
                            HashMap mapa = buildMap(offer.get());
                            return ResponseEntity.status(HttpStatus.OK).body(mapa);

                        }
                        else{
                            HttpHeaders headers = new HttpHeaders();
                            headers.add("Error-Message", "Selecciona un socorrista valido");
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build();
                        }
                    }
                    else{
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Error-Message", "No eres propietario de esta oferta");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build();
                    }
                }
                else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("/api/offers")
    public ResponseEntity<OfferDTO> postMethodName(@RequestBody OfferDTO offerDTO, Principal principal) { 
        if(principal !=null){
            Optional<Employer> eOP = userService.findEmployerByEmail(principal.getName());
            if(eOP.isPresent()){
                Employer e = eOP.get();
                if(isValid(offerDTO)){
                Pool pool = poolService.findById(offerDTO.getPoolID()).get();
                Offer offer = offerFromDTO(offerDTO);
                offer.addEmployer(e);
                e.addOffer(offer);
                offerService.save(offer);
                userService.saveEmployer(e);
                pool.addOffer(offer);
                poolService.save(pool); //Falta poner caso con codigo de error si los datos no son correctos
                URI location = ServletUriComponentsBuilder.fromHttpUrl("https://localhost:8443")
                .path("/api/offers/{id}")
                .buildAndExpand(offer.getId())
                .toUri();
                OfferDTO returnOfferDTO = new  OfferDTO(offer);
                return ResponseEntity.created(location).body(returnOfferDTO);
                }
                else{
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Error-Message", "Los datos no son válidos");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
                 }
            }
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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

@GetMapping("/api/aut/offers/{id}")
public OfferDTO getOfferp(@PathVariable int id){  //how to take credentials 
    Optional<Offer> offer = offerService.findById(id);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();
    if (offer.isPresent() && currentUsername!=offer.get().getEmployer().getMail()) return (new OfferDTO(offer.get()));
    else return null;
}

public boolean isOwner(Offer o, String n){
    return o.getEmployer().getMail().equals(n);
}

public boolean isAdmin(String n){
    return n.equals("admin"); //request.isuser in role (Admin) 
}

public HashMap<String, ArrayList<String>> buildMap(Offer offer){
    HashMap mapa = new HashMap<>();
                        if(offer.getLifeguard()!=null){
                            String lifeguard = offer.getLifeguard().getMail();
                            ArrayList l1 = new ArrayList<>();
                            l1.add(lifeguard);
                            mapa.put("Seleccionado",l1);
                        }     
                        else   mapa.put("Seleccionado",null);     
                        ArrayList l2 = new ArrayList<>();
                        for(Lifeguard l: offer.getLifeguards()){
                            l2.add(l.getMail());
                        }
                        mapa.put("Propuestos", l2);
    return mapa;
}

public boolean isValid(OfferDTO offerDTO){
    if(Integer.valueOf(offerDTO.getSalary())<1300) return false;
    return true;
}
}
