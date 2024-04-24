package es.codeurjc.restcontroller;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.DTO.MapsDTO;
import es.codeurjc.DTO.OfferDTO;
import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/maps")
public class MapsRestController {
    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get maps elements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offers found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDTO.class)) }),
            @ApiResponse(responseCode = "204", description = "There's no offer", content = @Content)
    })
    @GetMapping
    public ResponseEntity<MapsDTO> getMaps(Principal principal) {
        Collection<Offer> offers = offerService.findAll();

        if (offers.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        String center = "Universidad Rey Juan Carlos, Móstoles (Madrid)";
        if (principal != null) {
            String mail = principal.getName();
            Optional<Employer> employer = userService.findEmployerByEmail(mail);
            Optional<Lifeguard> lifeguard = userService.findLifeguardByEmail(mail);

            if (employer.isPresent())
                center = employer.get().getAddress();
            else if (lifeguard.isPresent())
                center = lifeguard.get().getAddress();
        }

        MapsDTO mapsDTO = new MapsDTO(offers, center);

        return ResponseEntity.status(HttpStatus.OK).body(mapsDTO);
    }
}
