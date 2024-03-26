package es.codeurjc.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.codeurjc.DTO.EmployerDTO;
import es.codeurjc.DTO.LifeguardDTO;
import es.codeurjc.DTO.OfferDTO;
import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
public class UserRestController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @Autowired
    private LifeguardRepository lifeguardService;

    @Autowired
    private EmployerRepository employerService;

	@Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/api/me")
	public ResponseEntity<Object> me(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String mail = authentication.getName();
		if (mail != null){
			Optional<Employer> employer = employerService.findByMail(mail);
			Optional<Lifeguard> lifeguard = lifeguardService.findByMail(mail);
	
			if (employer.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(new EmployerDTO(employer.get()));
	
			} else if(lifeguard.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(new LifeguardDTO(lifeguard.get()));
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

    @GetMapping("/api/lifeguard/{id}")
    public ResponseEntity<LifeguardDTO> getLifeguard(@PathVariable long id){ 
        Optional<Lifeguard> lifeguard = lifeguardService.findById(id);
        if (lifeguard.isPresent()) return ResponseEntity.status(HttpStatus.OK).body(new LifeguardDTO(lifeguard.get()));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/api/employer/{id}")
    public ResponseEntity<EmployerDTO> getEmployer(@PathVariable long id){ 
        Optional<Employer> employer = employerService.findById(id);
        if (employer.isPresent()) return ResponseEntity.status(HttpStatus.OK).body(new EmployerDTO(employer.get()));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/api/lifeguard/{id}")
    public String deleteLifeguard(@PathVariable long id){ //Only for admin and owner
        lifeguardService.deleteById(id);
        return "Se ha borrado correctamente";
    }

    @DeleteMapping("/api/employer/{id}")
    public String deleteEmployer(@PathVariable long id){ //Only for admin and owner
        employerService.deleteById(id);
        return "Se ha borrado correctamente";
    }

    @PostMapping("/api/lifeguard")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Lifeguard> createLifeguard(@RequestBody LifeguardDTO lifeguardDTO) {
		String messageForm = userService.checkForm(lifeguardDTO.getMail(), lifeguardDTO.getAge(),lifeguardDTO.getPhone());
		if (messageForm.equals("")) {
			Lifeguard lifeguard = lifeguardDTO.toLifeguard();
			lifeguard.setPass(passwordEncoder.encode(lifeguard.getPass()));
			lifeguard.setRoles("USER", "LIFE");
			lifeguardService.save(lifeguard);
			return new ResponseEntity<>(lifeguard, HttpStatus.OK);
		}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	
	}

    @PostMapping("/api/employer")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Employer> createEmployer(@RequestBody EmployerDTO employerDTO) {
		String messageForm = userService.checkForm(employerDTO.getMail(), employerDTO.getAge(),employerDTO.getPhone());
		if (messageForm.equals("")) {
			Employer employer = employerDTO.toEmployer();
			employer.setPass(passwordEncoder.encode(employer.getPass()));
        	employer.setRoles("USER", "EMP");
			employerService.save(employer);
			return new ResponseEntity<>(employer, HttpStatus.OK);
		}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

    @PutMapping("/api/lifeguard/{id}")
	public ResponseEntity<Lifeguard> updateLifeguard(@PathVariable long id, @RequestBody LifeguardDTO lifeguardDTO) throws SQLException {
		if (lifeguardService.existsById(id)) {
	
			/*if (updatedLifeguard.getImageUser()) {
				Lifeguard dbLifeguard = lifeguardService.findById(id).orElseThrow();
				if (dbLifeguard.getImageUser()) {
					updatedLifeguard.setPhotoUser(BlobProxy.generateProxy(dbLifeguard.getPhotoUser().getBinaryStream(),
							dbLifeguard.getPhotoUser().length()));
				}
			}*/
			String messageForm = userService.checkForm(lifeguardDTO.getMail(), lifeguardDTO.getAge(),lifeguardDTO.getPhone());
			Lifeguard lifeguard = lifeguardService.findById(id).get();
			if (messageForm.equals("") || (messageForm.equals("Correo ya en uso por otro socorrista. ") && lifeguardDTO.getMail().equals(lifeguard.getMail()))) {
				updateLifeguardDTO(lifeguardDTO, lifeguard);
				return new ResponseEntity<>(lifeguard, HttpStatus.OK);

			}return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		} else	{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

    @PutMapping("/api/employer/{id}")
	public ResponseEntity<Employer> updateEmployer(@PathVariable long id, @RequestBody EmployerDTO employerDTO) throws SQLException {

		if (employerService.existsById(id)) {

			/**if (updatedEmployer.getImageCompany()) {
				Employer dbEmployer = employerService.findById(id).orElseThrow();
				if (dbEmployer.getImageCompany()) {
					updatedEmployer.setPhotoCompany(BlobProxy.generateProxy(dbEmployer.getPhotoCompany().getBinaryStream(),
							dbEmployer.getPhotoCompany().length()));
				}
			}*/
			String messageForm = userService.checkForm(employerDTO.getMail(), employerDTO.getAge(),employerDTO.getPhone());
			Employer employer = employerService.findById(id).get();
			if (messageForm.equals("") || (messageForm.equals("Correo ya en uso por otro empleado. ") && employerDTO.getMail().equals(employer.getMail()))) {
				updateEmployerDTO(employerDTO, employer);
				return new ResponseEntity<>(employer, HttpStatus.OK);

			}return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		} else	{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

    @PostMapping("/api/employer/{id}/photoCompany")
	public ResponseEntity<Object> uploadPhotoCompany(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {

		Employer employer = employerService.findById(id).orElseThrow();

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

		employer.setImageCompany(true);
		employer.setPhotoCompany(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
		employerService.save(employer);

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/api/employer/{id}/photoCompany")
	public ResponseEntity<Object> downloadPhotoCompany(@PathVariable long id) throws SQLException {

		Employer employer = employerService.findById(id).orElseThrow();

		if (employer.getPhotoCompany() != null) {

			Resource file = (Resource) new InputStreamResource(employer.getPhotoCompany().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(employer.getPhotoCompany().length()).body(file);

		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/api/employer/{id}/photoCompany")
	public ResponseEntity<Object> deletePhotoCompany(@PathVariable long id) throws IOException {

		Employer employer = employerService.findById(id).orElseThrow();

		employer.setPhotoCompany(null);
		employer.setImageCompany(false);

		employerService.save(employer);

		return ResponseEntity.noContent().build();
	}

    @PostMapping("/api/lifeguard/{id}/photoUser")
	public ResponseEntity<Object> uploadPhotoUser(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {

		Lifeguard lifeguard = lifeguardService.findById(id).orElseThrow();

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

		lifeguard.setImageUser(true);
		lifeguard.setPhotoUser(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
		lifeguardService.save(lifeguard);

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/api/lifeguard/{id}/photoUser")
	public ResponseEntity<Object> downloadPhotoUser(@PathVariable long id) throws SQLException {

		Lifeguard lifeguard = lifeguardService.findById(id).orElseThrow();

		if (lifeguard.getPhotoUser() != null) {

			Resource file = (Resource) new InputStreamResource(lifeguard.getPhotoUser().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(lifeguard.getPhotoUser().length()).body(file);

		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/api/lifeguard/{id}/photoUser")
	public ResponseEntity<Object> deletePhotoUser(@PathVariable long id) throws IOException {

		Lifeguard lifeguard = lifeguardService.findById(id).orElseThrow();

		lifeguard.setPhotoUser(null);
		lifeguard.setImageUser(false);

		lifeguardService.save(lifeguard);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/api/lifeguard/{id}/offers")
	public ResponseEntity<List<OfferDTO>> getLifeguardOffers(@PathVariable long id){ 
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
	}

	@GetMapping("/api/lifeguard/{id}/offersAccepted")
	public ResponseEntity<List<OfferDTO>> getLifeguardOffersAccepted(@PathVariable long id){ 
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
	}

	@DeleteMapping("/api/lifeguard/{id}/offers/{offerId}")
	public ResponseEntity<Void> deleteOfferFromLifeguard(@PathVariable long id, @PathVariable long offerId) {
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
	}

	@DeleteMapping("/api/lifeguard/{id}/offersAccepted/{offerId}")
	public ResponseEntity<Void> deleteOfferAcceptedFromLifeguard(@PathVariable long id, @PathVariable long offerId) {
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
	}

	@GetMapping("/api/employer/{id}/offers")
	public ResponseEntity<List<OfferDTO>> getEmployerOffers(@PathVariable long id){ 
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
	}

	@DeleteMapping("/api/employer/{id}/offers/{offerId}")
	public ResponseEntity<Void> deleteOfferFromEmployer(@PathVariable long id, @PathVariable long offerId) {
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
	}

	public void updateLifeguardDTO(LifeguardDTO lifeguardDTO, Lifeguard lifeguard){
		if (lifeguardDTO.getName()!=null) lifeguard.setName(lifeguardDTO.getName());
		if (lifeguardDTO.getSurname()!=null) lifeguard.setSurname(lifeguardDTO.getSurname());
		if (lifeguardDTO.getDescription()!=null) lifeguard.setDescription(lifeguardDTO.getDescription());
		if (lifeguardDTO.getDni()!=null) lifeguard.setDni(lifeguardDTO.getDni());
		if (lifeguardDTO.getMail()!=null) lifeguard.setMail(lifeguardDTO.getMail());
		if (lifeguardDTO.getAge()!=null) lifeguard.setAge(lifeguardDTO.getAge());
		if (lifeguardDTO.getPass()!=null) lifeguard.setPass(passwordEncoder.encode(lifeguardDTO.getPass()));
		if (lifeguardDTO.getPhone()!=null) lifeguard.setPhone(lifeguardDTO.getPhone());
		if (lifeguardDTO.getCountry()!=null) lifeguard.setCountry(lifeguardDTO.getCountry());
		if (lifeguardDTO.getLocality()!=null) lifeguard.setLocality(lifeguardDTO.getLocality());
		if (lifeguardDTO.getProvince()!=null) lifeguard.setProvince(lifeguardDTO.getProvince());
		//if (lifeguardDTO.getPhoto()!=null) lifeguard.setPhotoUser(lifeguardDTO.getPhoto());
		if (lifeguardDTO.getDocument()!=null) lifeguard.setDocument(lifeguardDTO.getDocument());
		lifeguardService.save(lifeguard);
	}

	public void updateEmployerDTO(EmployerDTO employerDTO, Employer employer){
		if (employerDTO.getName()!=null) employer.setName(employerDTO.getName());
		if (employerDTO.getSurname()!=null) employer.setSurname(employerDTO.getSurname());
		if (employerDTO.getDescription()!=null) employer.setDescription(employerDTO.getDescription());
		if (employerDTO.getDni()!=null) employer.setDni(employerDTO.getDni());
		if (employerDTO.getMail()!=null) employer.setMail(employerDTO.getMail());
		if (employerDTO.getAge()!=null) employer.setAge(employerDTO.getAge());
		if (employerDTO.getPass()!=null) employer.setPass(passwordEncoder.encode(employerDTO.getPass()));
		if (employerDTO.getPhone()!=null) employer.setPhone(employerDTO.getPhone());
		if (employerDTO.getCountry()!=null) employer.setCountry(employerDTO.getCountry());
		if (employerDTO.getLocality()!=null) employer.setLocality(employerDTO.getLocality());
		if (employerDTO.getProvince()!=null) employer.setProvince(employerDTO.getProvince());
		//if (employerDTO.getPhotoCompany()!=null) employer.setPhotoCompany(employerDTO.getPhotoCompany());
		if (employerDTO.getCompany()!=null) employer.setCompany(employerDTO.getCompany());
		employerService.save(employer);
	}

}