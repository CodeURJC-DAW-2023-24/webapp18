package es.codeurjc.restcontroller;

import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.DTO.OfferDTO;
import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.model.Pool;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.PoolService;
import es.codeurjc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/offers")
public class OfferRestController {
    @Autowired
    private PoolService poolService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    // ----------------------------------------------- GET -----------------------------------------------
    @Operation(summary = "Get paged offers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offers found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class)) }),
            @ApiResponse(responseCode = "204", description = "Offers not found, probably high page number supplied", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<OfferDTO>> getOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Offer> offers = offerService.findAll(PageRequest.of(page, size));
        List<OfferDTO> offersDTO = new ArrayList<>();

        for (Offer o : offers) {
            offersDTO.add(new OfferDTO(o));
        }

        if (offersDTO.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(offersDTO);
    }

    @Operation(summary = "Get an offer by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Offer not found, probably invalid id supplied", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable int id) {
        Optional<Offer> offer = offerService.findById(id);
        if (offer.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(new OfferDTO(offer.get()));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



    @Operation(summary = "Get the pool photo of the offer by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Photo not found, probably invalid offer id supplied", content = @Content)
    })
    @GetMapping("/{id}/photo")
    public ResponseEntity<Object> getOfferPhoto(@PathVariable int id) throws SQLException {
        Optional<Offer> offerOp = offerService.findById(id);
        if (offerOp.isPresent()){
            Pool pool = offerOp.get().getPool();
            InputStreamResource photo = new InputStreamResource(pool.getPhotoBlob().getBinaryStream());
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(photo);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @Operation(summary = "View lifeguards who applied to an offer and the selected one.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lifeguards found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HashMap.class)) }),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the owner or the admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "Offer not found, probably invalid id supplied", content = @Content)
    })
    @GetMapping("/{id}/lifeguards")
    public ResponseEntity<HashMap<String, ArrayList<String>>> getLifeguards(@PathVariable int id, Principal principal) {
        Optional<Offer> offerOptional = offerService.findById(id);
        if (!offerOptional.isPresent()) 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Offer offer = offerOptional.get();

        if (!userService.isAuthorized(principal, offer))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        HashMap<String, ArrayList<String>> mapa = buildMap(offer);
        return ResponseEntity.status(HttpStatus.OK).body(mapa);
    }

    // ----------------------------------------------- POST -----------------------------------------------
    @Operation(summary = "Create an offer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Offer created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not a an employer", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data in new offer", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pool not found, probably invalid id supplied", content = @Content)
    })
    @PostMapping
    public ResponseEntity<OfferDTO> createOffer(@RequestBody OfferDTO offerDTO, Principal principal) {
        if (principal != null) {
            Optional<Employer> eOP = userService.findEmployerByEmail(principal.getName());
            if (eOP.isPresent()) {
                Employer e = eOP.get();
                try {
                    checkOfferDTO(offerDTO);
                } catch (IllegalArgumentException exception) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Error-Message", exception.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
                }
                Optional<Pool> pOP = poolService.findById(offerDTO.getPoolID());
                if (pOP.isPresent()) {
                    Pool pool = pOP.get();
                    Offer offer = offerFromDTO(offerDTO);
                    offer.addEmployer(e);
                    e.addOffer(offer);
                    offerService.save(offer);
                    userService.saveEmployer(e);
                    pool.addOffer(offer);
                    poolService.save(pool); // Error code missimg in data are incorect
                    URI location = ServletUriComponentsBuilder.fromHttpUrl("https://localhost:8443")
                            .path("/api/offers/{id}")
                            .buildAndExpand(offer.getId())
                            .toUri();
                    OfferDTO returnOfferDTO = new OfferDTO(offer);
                    return ResponseEntity.created(location).body(returnOfferDTO);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Apply to an offer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer applied", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not a lifeguard", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict, you applied more than once", content = @Content),
            @ApiResponse(responseCode = "404", description = "Offer not found, probably invalid id supplied", content = @Content)
    })
    @PostMapping("/{id}/lifeguards") // Only lifeguards not already applied
    public ResponseEntity<OfferDTO> newApply(@PathVariable int id, Principal principal) {
        if (principal != null) {
            Optional<Lifeguard> lOP = userService.findLifeguardByEmail(principal.getName());
            if (lOP.isPresent()) {
                Optional<Offer> offer = offerService.findById(id);
                Lifeguard l = lOP.get();
                if (offer.isPresent()) {
                    if (!offer.get().isOffered(l.getMail())) {
                        offer.get().addOffered(l);
                        offerService.save(offer.get());
                        l.addOffer(offer.get());
                        userService.saveLifeguard(l);
                        return ResponseEntity.status(HttpStatus.OK).body(new OfferDTO(offer.get()));
                    } else {
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Error-Message", "Ya has aplicado");
                        return ResponseEntity.status(HttpStatus.CONFLICT).headers(headers).build();
                    }

                } else
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // offer not found
            } else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // user not found
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // not logged
    }

    // ----------------------------------------------- PUT -----------------------------------------------
    @Operation(summary = "Edit an offer by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer edited", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data in changes", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the owner or the admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "Offer not found, probably invalid id supplied", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> editOffer(@PathVariable int id, @RequestBody OfferDTO offerDTO, Principal principal) throws SQLException {
        Optional<Offer> offerOptional = offerService.findById(id);
        if (!offerOptional.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Offer offer = offerOptional.get();

        if (!userService.isAuthorized(principal, offer))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            checkOfferDTO(offerDTO);
        } catch (IllegalArgumentException exception) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        }

        updateOfferFromDTO(offer, offerDTO);
        offerService.save(offer);
        return ResponseEntity.status(HttpStatus.OK).body(new OfferDTO(offer));
    }

    @Operation(summary = "Select lifeguard for an offer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lifeguard selected", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HashMap.class)) }),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the owner or the admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "Offer not found, probably invalid id supplied", content = @Content)
    })
    @PutMapping("/{id}/lifeguards/{nSelected}") // Only for admin and owner
    public ResponseEntity<HashMap<String, ArrayList<String>>> selectLifeguard(@PathVariable int id,
            @PathVariable int nSelected, Principal principal) {
        Optional<Offer> offerOptional = offerService.findById(id);
        if (!offerOptional.isPresent()) 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Offer offer = offerOptional.get();

        if (!userService.isAuthorized(principal, offer))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (nSelected < offer.getLifeguards().size()) {
            Lifeguard l = offer.getLifeguards().get(nSelected);
            offer.setLifeguard(l);
            offerService.save(offer);
            l.setofferAssigned(true);
            l.addOfferAccepted(offer);
            userService.saveLifeguard(l);
            HashMap<String, ArrayList<String>> mapa = buildMap(offer);
            return ResponseEntity.status(HttpStatus.OK).body(mapa);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", "Selecciona un socorrista valido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        }
    }

    // ----------------------------------------------- DELETE -----------------------------------------------
    @Operation(summary = "Delete an offer by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the owner or the admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "Offer not found, probably invalid id supplied", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable int id, Principal principal) { // Only for admin and owner
        Optional<Offer> offerOptional = offerService.findById(id);
        if (!offerOptional.isPresent()) 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Offer offer = offerOptional.get();

        if (!userService.isAuthorized(principal, offer))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        offerService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Unselect lifeguard of an offer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lifeguard unselected", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = HashMap.class)) }),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the owner or the admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "Offer not found, probably invalid id supplied", content = @Content)
    })
    @DeleteMapping("/{id}/lifeguards") // Only for admin and owner
    public ResponseEntity<HashMap<String, ArrayList<String>>> unselectLifeguard(@PathVariable int id,
            Principal principal) {
        Optional<Offer> offerOptional = offerService.findById(id);
        if (!offerOptional.isPresent()) 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Offer offer = offerOptional.get();

        if (!userService.isAuthorized(principal, offer))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (offer.getLifeguard() != null) {
            Lifeguard l = offer.getLifeguard();
            offer.setLifeguard(null);
            offerService.save(offer);
            l.setofferAssigned(false);
            l.deleteOfferAccepted(offer);
            userService.saveLifeguard(l);
            HashMap<String, ArrayList<String>> mapa = buildMap(offer);
            return ResponseEntity.status(HttpStatus.OK).body(mapa);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", "No hay ningun socorrista seleccionado");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        }
    }

    // ----------------------------------------------- SERVICE -----------------------------------------------
    public Offer offerFromDTO(OfferDTO o) {
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

    public Offer updateOfferFromDTO(Offer offer, OfferDTO offerDTO){
        Offer.Builder offerBuilder = new Offer.Builder();
        offerBuilder
            .salary(offerDTO.getSalary())
            .start(offerDTO.getStart())
            .type(offerDTO.getType())
            .description(offerDTO.getDescription());

        offer.update(offerBuilder);
        return offer;
	}

    public HashMap<String, ArrayList<String>> buildMap(Offer offer) {
        HashMap<String, ArrayList<String>> mapa = new HashMap<>();
        if (offer.getLifeguard() != null) {
            String lifeguard = offer.getLifeguard().getMail();
            ArrayList<String> l1 = new ArrayList<>();
            l1.add(lifeguard);
            mapa.put("Seleccionado", l1);
        } else
            mapa.put("Seleccionado", null);
            ArrayList<String> l2 = new ArrayList<>();
            ArrayList<String> l3 = new ArrayList<>();
        for (Lifeguard l : offer.getLifeguards()) {
            l2.add(l.getMail());
            l3.add(l.getDescription());
        }
        mapa.put("Propuestos", l2);
        mapa.put("Descripciones", l3);
        return mapa;
    }

    public boolean checkOfferDTO(OfferDTO offerDTO) {
        if (offerDTO.getSalary() != null && Integer.valueOf(Integer.valueOf(offerDTO.getSalary())) < 1300)
            throw new IllegalArgumentException("No se puede introducir un salario menor al salario minimo");
        return true;
    }
}
