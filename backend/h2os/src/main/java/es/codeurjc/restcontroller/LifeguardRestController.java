package es.codeurjc.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.DTO.EmployerDTO;
import es.codeurjc.DTO.LifeguardDTO;
import es.codeurjc.DTO.OfferDTO;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.repository.LifeguardRepository;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class LifeguardRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private LifeguardRepository lifeguardService;

	@Autowired OfferService offerService;
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Operation(summary = "Get paged lifeguards.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lifeguards found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LifeguardDTO.class)) }),
            @ApiResponse(responseCode = "204", description = "Lifeguards not found, probably high page number supplied", content = @Content)
    })
    @GetMapping("/api/lifeguards")
    public ResponseEntity<List<LifeguardDTO>> getLifeguards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Lifeguard> lifeguards = lifeguardService.findAll(PageRequest.of(page, size));
        List<LifeguardDTO> lifeguardsDTO = new ArrayList<>();

        for (Lifeguard lifeguard : lifeguards) {
            lifeguardsDTO.add(new LifeguardDTO(lifeguard));
        }

        if (lifeguardsDTO.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(lifeguardsDTO);
    }

    @Operation(summary = "Get a lifeguard by its id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the lifeguard",
	 content = {@Content(
	 mediaType = "application/json",
	 schema = @Schema(implementation=LifeguardDTO.class)
	 )}
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Lifeguard not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @GetMapping("/api/lifeguards/{id}")
    public ResponseEntity<LifeguardDTO> getLifeguard(@PathVariable long id){ 
        Optional<Lifeguard> lifeguard = lifeguardService.findById(id);
        if (lifeguard.isPresent()) return ResponseEntity.status(HttpStatus.OK).body(new LifeguardDTO(lifeguard.get()));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Delete a lifeguard by its id. You need to be an administrator or the own employer.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Lifeguard deleted",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Lifeguard not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @DeleteMapping("/api/lifeguards/{id}")
    public ResponseEntity<?> deleteLifeguard(@PathVariable long id, Principal principal){ //Only for admin and owner
        if(principal !=null){
			Optional<Lifeguard> lifeguard = lifeguardService.findById(id);
			if (lifeguard.isPresent()){
				Lifeguard l = lifeguard.get();
				List<Offer> offers= l.getOffers();
				for (Offer offer: offers){
					offer.deleteOffered(l);
					offerService.save(offer);
				}
				l.clearOffers();
				List<Offer> offers_accepted= l.getOffersAccepted();
				for (Offer offer: offers_accepted){
					offer.setLifeguard(null);
					offerService.save(offer);
				}
				l.clearOffersAccepted();
				lifeguardService.save(l);
				lifeguardService.deleteById(id);
				return ResponseEntity.status(HttpStatus.OK).build();
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Post a new lifeguard")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Lifeguard posted correctly",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=LifeguardDTO.class)
		)}
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly.",
	 content = @Content
	 )
	})
    @PostMapping("/api/lifeguards")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Lifeguard> createLifeguard(@RequestBody LifeguardDTO lifeguardDTO) {
		String messageForm = userService.checkForm(lifeguardDTO.getMail(), lifeguardDTO.getAge(),lifeguardDTO.getPhone());
		if (messageForm.equals("")) {
			Lifeguard lifeguard = lifeguardDTO.toLifeguard();
			lifeguard.setPass(passwordEncoder.encode(lifeguard.getPass()));
			lifeguard.setRoles("USER", "LIFE");
			lifeguard.setofferAssigned(false);
			lifeguardService.save(lifeguard);
			return new ResponseEntity<>(lifeguard, HttpStatus.OK);
		}else{
			HttpHeaders headers = new HttpHeaders();
			headers.add("Error-Message", messageForm);
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
		}
	}

    @Operation(summary = "Update a lifeguard fields by ID. You need to be an administrator or the own lifeguard.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Lifeguard updated correctly. You can update only the fields you want",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=EmployerDTO.class)
		)}
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly.",
	 content = @Content
	 ),	
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Lifeguard not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @PutMapping("/api/lifeguards/{id}")
	public ResponseEntity<LifeguardDTO> updateLifeguard(@PathVariable long id, @RequestBody LifeguardDTO lifeguardDTO, Principal principal) throws SQLException {
		if(principal !=null){
			if (lifeguardService.existsById(id)) {
				String messageForm = userService.checkForm(lifeguardDTO.getMail(), lifeguardDTO.getAge(),lifeguardDTO.getPhone());
				Lifeguard lifeguard = lifeguardService.findById(id).get();
				if (messageForm.equals("") || (messageForm.equals("Correo ya en uso por otro socorrista. ") && lifeguardDTO.getMail().equals(lifeguard.getMail()))) {
					updateLifeguardDTO(lifeguardDTO, lifeguard);
					return new ResponseEntity<>(lifeguardDTO, HttpStatus.OK);

				}else{
					HttpHeaders headers = new HttpHeaders();
					headers.add("Error-Message", messageForm);
        			return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
				}
			} else	{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

    @Operation(summary = "Post a new photo of the user of a lifeguard by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "201",
	 description = "User photo posted correctly",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 )
	})
    @PostMapping("/api/lifeguards/{id}/photoUser")
	public ResponseEntity<Object> uploadPhotoUser(@PathVariable long id, @RequestParam MultipartFile imageFile, Principal principal)
			throws IOException {
		if(principal !=null){
			if (lifeguardService.existsById(id)) {
				Lifeguard lifeguard = lifeguardService.findById(id).orElseThrow();

				URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

				lifeguard.setImageUser(true);
				lifeguard.setPhotoUser(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				lifeguardService.save(lifeguard);

				return ResponseEntity.created(location).build();
			}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Get the photo of the user of a lifeguard by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the user photo",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "404",
	 description = "That lifeguard does not have an user photo",
	 content = @Content
	 )
	})
	@GetMapping("/api/lifeguards/{id}/photoUser")
	public ResponseEntity<Object> downloadPhotoUser(@PathVariable long id) throws SQLException {
		if (lifeguardService.existsById(id)) {
			Lifeguard lifeguard = lifeguardService.findById(id).orElseThrow();

			if (lifeguard.getPhotoUser() != null) {

				org.springframework.core.io.Resource file = new InputStreamResource(lifeguard.getPhotoUser().getBinaryStream());

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
						.contentLength(lifeguard.getPhotoUser().length()).body(file);

			} else {
				return ResponseEntity.notFound().build();
			}
		}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Delete the photo of the user of an lifeguard by id. You need to be an admin or the own lifeguard")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "User photo deleted correctly",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 )
	})
	@DeleteMapping("/api/lifeguards/{id}/photoUser")
	public ResponseEntity<Object> deletePhotoUser(@PathVariable long id, Principal principal) throws IOException {
		if(principal !=null){
			if (lifeguardService.existsById(id)) {
				Lifeguard lifeguard = lifeguardService.findById(id).orElseThrow();

				lifeguard.setPhotoUser(null);
				lifeguard.setImageUser(false);

				lifeguardService.save(lifeguard);

				return ResponseEntity.noContent().build();
			}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Get the offers of a lifeguard by id. You need to be an admin or the own lifeguard")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the offer list of the lifeguard",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),	
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	})
	@GetMapping("/api/lifeguards/{id}/offers")
	public ResponseEntity<List<OfferDTO>> getLifeguardOffers(@PathVariable long id, Principal principal){ 
    	if(principal !=null){
			Optional<Lifeguard> lifeguardOptional = lifeguardService.findById(id);
			if (lifeguardOptional.isPresent()) {
				Lifeguard lifeguard = lifeguardOptional.get();
			
				List<OfferDTO> offersDTO = lifeguard.getOffers().stream()
					.map(OfferDTO::new)
					.collect(Collectors.toList());
			
				return ResponseEntity.status(HttpStatus.OK).body(offersDTO);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Get the offers accepted of a lifeguard by id. You need to be an admin or the own lifeguard")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the offers accepted list of the lifeguard",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),	
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	})
	@GetMapping("/api/lifeguards/{id}/offersAccepted")
	public ResponseEntity<List<OfferDTO>> getLifeguardOffersAccepted(@PathVariable long id, Principal principal){ 
    	if(principal !=null){
			Optional<Lifeguard> lifeguardOptional = lifeguardService.findById(id);
			if (lifeguardOptional.isPresent()) {
				Lifeguard lifeguard = lifeguardOptional.get();
			
				List<OfferDTO> offersAcceptedDTO = lifeguard.getOffersAccepted().stream()
					.map(OfferDTO::new)
					.collect(Collectors.toList());
			
				return ResponseEntity.status(HttpStatus.OK).body(offersAcceptedDTO);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Remove an offer of the lifeguard's offers list by id. You need to be an admin or the own lifeguard")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Offer removed correctly",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),	
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id or offerId supplied",
	 content = @Content
	 ),	 
	})
	@DeleteMapping("/api/lifeguards/{id}/offers/{offerId}")
	public ResponseEntity<Void> deleteOfferFromLifeguard(@PathVariable long id, @PathVariable long offerId, Principal principal) {
		if(principal !=null){
			Optional<Lifeguard> lifeguardOptional = lifeguardService.findById(id);
			if (lifeguardOptional.isPresent()) {
				Lifeguard lifeguard = lifeguardOptional.get();
				Offer offerToRemove = null;
				for (Offer offer : lifeguard.getOffers()) {
					if (offer.getId() == offerId) {
						offerToRemove = offer;
						break;
					}
				}
				if (offerToRemove != null) {
					lifeguard.getOffers().remove(offerToRemove);
					lifeguardService.save(lifeguard);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Remove an offer of the lifeguard's offersAccepted list by id. You need to be an admin or the own lifeguard")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Offer removed correctly",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),	
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id or offerId supplied",
	 content = @Content
	 ),	 
	})
	@DeleteMapping("/api/lifeguards/{id}/offersAccepted/{offerId}")
	public ResponseEntity<Void> deleteOfferAcceptedFromLifeguard(@PathVariable long id, @PathVariable long offerId, Principal principal) {
		if(principal !=null){
			Optional<Lifeguard> lifeguardOptional = lifeguardService.findById(id);
			if (lifeguardOptional.isPresent()) {
				Lifeguard lifeguard = lifeguardOptional.get();
				Offer offerAcceptedToRemove = null;
				for (Offer offerAccepted : lifeguard.getOffersAccepted()) {
					if (offerAccepted.getId() == offerId) {
						offerAcceptedToRemove = offerAccepted;
						break;
					}
				}
				if (offerAcceptedToRemove != null) {
					lifeguard.getOffersAccepted().remove(offerAcceptedToRemove);
					lifeguardService.save(lifeguard);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

    public void updateLifeguardDTO(LifeguardDTO lifeguardDTO, Lifeguard lifeguard){
		if (lifeguardDTO.getName()!=null) lifeguard.setName(lifeguardDTO.getName());
		if (lifeguardDTO.getSurname()!=null) lifeguard.setSurname(lifeguardDTO.getSurname());
		if (lifeguardDTO.getDescription()!=null) lifeguard.setDescription(lifeguardDTO.getDescription());
		if (lifeguardDTO.getDni()!=null) lifeguard.setDni(lifeguardDTO.getDni());
		if (lifeguardDTO.getMail()!=null) lifeguard.setMail(lifeguardDTO.getMail());
		if (lifeguardDTO.getAge()!=null) lifeguard.setAge(lifeguardDTO.getAge());
		if (lifeguardDTO.getPass()!=null) lifeguard.setPass((lifeguardDTO.getPass()));
		if (lifeguardDTO.getPhone()!=null) lifeguard.setPhone(lifeguardDTO.getPhone());
		if (lifeguardDTO.getCountry()!=null) lifeguard.setCountry(lifeguardDTO.getCountry());
		if (lifeguardDTO.getLocality()!=null) lifeguard.setLocality(lifeguardDTO.getLocality());
		if (lifeguardDTO.getProvince()!=null) lifeguard.setProvince(lifeguardDTO.getProvince());
		if (lifeguardDTO.getDocument()!=null) lifeguard.setDocument(lifeguardDTO.getDocument());
		if (lifeguardDTO.getSkills()!=null) lifeguard.setSkills(lifeguardDTO.getSkills());
		lifeguardService.save(lifeguard);
	}
}
