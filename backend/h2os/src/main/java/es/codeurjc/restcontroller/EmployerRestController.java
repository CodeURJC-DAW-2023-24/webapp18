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
import es.codeurjc.DTO.OfferDTO;
import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class EmployerRestController {
    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployerRepository employerService;

	@Autowired
	private LifeguardRepository lifeguardService;

	@Autowired
    private PasswordEncoder passwordEncoder;

	@Operation(summary = "Get paged employers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employers found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = EmployerDTO.class)) }),
            @ApiResponse(responseCode = "204", description = "Employers not found, probably high page number supplied", content = @Content)
    })
    @GetMapping("/api/employers")
    public ResponseEntity<List<EmployerDTO>> getEmployers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Employer> employers = employerService.findAll(PageRequest.of(page, size));
        List<EmployerDTO> employersDTO = new ArrayList<>();

        for (Employer employer : employers) {
            employersDTO.add(new EmployerDTO(employer));
        }

        if (employersDTO.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(employersDTO);
    }

    @Operation(summary = "Get an employer by its id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the employer",
	 content = {@Content(
	 mediaType = "application/json",
	 schema = @Schema(implementation=EmployerDTO.class)
	 )}
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Employer not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @GetMapping("/api/employers/{id}")
    public ResponseEntity<EmployerDTO> getEmployer(@PathVariable long id){ 
        Optional<Employer> employer = employerService.findById(id);
        if (employer.isPresent()) return ResponseEntity.status(HttpStatus.OK).body(new EmployerDTO(employer.get()));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Delete an employer by its id. You need to be an administrator or the own employer.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Employer deleted",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Employer not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @DeleteMapping("/api/employers/{id}")
    public ResponseEntity<?> deleteEmployer(@PathVariable long id, Principal principal){ //Only for admin and owner
        if(principal !=null){
			Optional<Employer> employer = employerService.findById(id);
			if (employer.isPresent()){
				employerService.deleteById(id);
				return ResponseEntity.status(HttpStatus.OK).build();
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Post a new employer")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Employer posted correctly",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=EmployerDTO.class)
		)}
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly.",
	 content = @Content
	 )
	})
    @PostMapping("/api/employers")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Employer> createEmployer(@RequestBody EmployerDTO employerDTO) {
		String messageForm = userService.checkForm(employerDTO.getMail(), employerDTO.getAge(),employerDTO.getPhone());
		if (messageForm.equals("")) {
			Employer employer = employerDTO.toEmployer();
			employer.setPass(passwordEncoder.encode(employer.getPass()));
        	employer.setRoles("USER", "EMP");
			employerService.save(employer);
			return new ResponseEntity<>(employer, HttpStatus.OK);
		}else{
			HttpHeaders headers = new HttpHeaders();
			headers.add("Error-Message", messageForm);
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
		}
	}

    @Operation(summary = "Update an employer fields by ID. You need to be an administrator or the own employer.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Employer updated correctly. You can update only the fields you want",
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
	 description = "Employer not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @PutMapping("/api/employers/{id}")
	public ResponseEntity<EmployerDTO> updateEmployer(@PathVariable long id, @RequestBody EmployerDTO employerDTO, Principal principal) throws SQLException {
		if(principal !=null){
			if (employerService.existsById(id)) {

				String messageForm = userService.checkForm(employerDTO.getMail(), employerDTO.getAge(),employerDTO.getPhone());
				Employer employer = employerService.findById(id).get();
				if (messageForm.equals("") || (messageForm.equals("Correo ya en uso por otro empleado. ") && employerDTO.getMail().equals(employer.getMail()))) {
					updateEmployerDTO(employerDTO, employer);
					return new ResponseEntity<>(employerDTO, HttpStatus.OK);

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

    @Operation(summary = "Post a new photo of the company of an employer by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "201",
	 description = "Company photo posted correctly",
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
    @PostMapping("/api/employers/{id}/photoCompany")
	public ResponseEntity<Object> uploadPhotoCompany(@PathVariable long id, @RequestParam MultipartFile imageFile, Principal principal)
			throws IOException {
		if(principal !=null){
			if (employerService.existsById(id)) {
				Employer employer = employerService.findById(id).orElseThrow();

				URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

				employer.setImageCompany(true);
				employer.setPhotoCompany(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				employerService.save(employer);

				return ResponseEntity.created(location).build();
			}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Get the photo of the company of an employer by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the company photo",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "404",
	 description = "That employer does not have a company photo",
	 content = @Content
	 )
	})
	@GetMapping("/api/employers/{id}/photoCompany")
	public ResponseEntity<Object> downloadPhotoCompany(@PathVariable long id) throws SQLException {
		if (employerService.existsById(id)) {
			Employer employer = employerService.findById(id).orElseThrow();
		
			if (employer.getPhotoCompany() != null) {

				org.springframework.core.io.Resource file = new InputStreamResource(employer.getPhotoCompany().getBinaryStream());

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
						.contentLength(employer.getPhotoCompany().length()).body(file);

			} else {
				return ResponseEntity.notFound().build();
			}
		}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Delete the photo of the company of an employer by id. You need to be an admin or the own employer")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Company photo deleted correctly",
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
	@DeleteMapping("/api/employers/{id}/photoCompany")
	public ResponseEntity<Object> deletePhotoCompany(@PathVariable long id, Principal principal) throws IOException {
		if(principal !=null){
			if (employerService.existsById(id)) {
				Employer employer = employerService.findById(id).orElseThrow();

				employer.setPhotoCompany(null);
				employer.setImageCompany(false);

				employerService.save(employer);

				return ResponseEntity.noContent().build();
			}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}


	@Operation(summary = "Get the offers of an employer by id. You need to be an admin or the own employer")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the offer list of the employer",
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
	@GetMapping("/api/employers/{id}/offers")
	public ResponseEntity<List<OfferDTO>> getEmployerOffers(@PathVariable long id, Principal principal){ 
    	if(principal !=null){
			Optional<Employer> employerOptional = employerService.findById(id);
			if (employerOptional.isPresent()) {
				Employer employer = employerOptional.get();
			
				List<OfferDTO> offersDTO = employer.getOffers().stream()
					.map(OfferDTO::new)
					.collect(Collectors.toList());
			
				return ResponseEntity.status(HttpStatus.OK).body(offersDTO);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Delete an offer of the employer by id. You need to be an admin or the own employer")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Offer deleted correctly",
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
	@DeleteMapping("/api/employers/{id}/offers/{offerId}")
	public ResponseEntity<Void> deleteOfferFromEmployer(@PathVariable long id, @PathVariable long offerId, Principal principal) {
		if(principal !=null){
			Optional<Employer> employerOptional = employerService.findById(id);
			if (employerOptional.isPresent()) {
				Employer employer = employerOptional.get();
				Offer offerToRemove = null;
				for (Offer offer : employer.getOffers()) {
					if (offer.getId() == offerId) {
						offerToRemove = offer;
						break;
					}
				}
				if (offerToRemove != null) {
					employer.getOffers().remove(offerToRemove);
					employerService.save(employer);
					
					offerService.deleteById(offerToRemove.getId());
					
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Get a list of email addresses")
	@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "List of email addresses retrieved successfully",
        content = @Content(mediaType = "application/json")
    )
	})
	@GetMapping("/api/mails")
	public ResponseEntity<List<String>> getMails() {
		List<String> mails = new ArrayList<String>();
		long i = 1;
		long j = 1;
		boolean done = true;
		while (done){
			boolean emp = false;
			boolean life = false;
			Optional<Employer> employerOptional = employerService.findById(i);
			if (employerOptional.isPresent()) {
				mails.add(employerOptional.get().getMail());
				i = i+1;
			}else{
				emp = true;
			}
			Optional<Lifeguard> lifeguardOptional = lifeguardService.findById(j);
			if (lifeguardOptional.isPresent()) {
				mails.add(lifeguardOptional.get().getMail());
				j = j+1;
			}else{
				life = true;
			}
			if (emp && life){
				done = false;
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(mails);
	}
	

    public void updateEmployerDTO(EmployerDTO employerDTO, Employer employer){
		if (employerDTO.getName()!=null) employer.setName(employerDTO.getName());
		if (employerDTO.getSurname()!=null) employer.setSurname(employerDTO.getSurname());
		if (employerDTO.getDescription()!=null) employer.setDescription(employerDTO.getDescription());
		if (employerDTO.getDni()!=null) employer.setDni(employerDTO.getDni());
		if (employerDTO.getMail()!=null) employer.setMail(employerDTO.getMail());
		if (employerDTO.getAge()!=null) employer.setAge(employerDTO.getAge());
		if (employerDTO.getPass()!=null) employer.setPass((employerDTO.getPass()));
		if (employerDTO.getPhone()!=null) employer.setPhone(employerDTO.getPhone());
		if (employerDTO.getCountry()!=null) employer.setCountry(employerDTO.getCountry());
		if (employerDTO.getLocality()!=null) employer.setLocality(employerDTO.getLocality());
		if (employerDTO.getProvince()!=null) employer.setProvince(employerDTO.getProvince());
		if (employerDTO.getDirection()!=null) employer.setDirection(employerDTO.getDirection());
		if (employerDTO.getCompany()!=null) employer.setCompany(employerDTO.getCompany());
		employerService.save(employer);
	}
}
