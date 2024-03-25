package es.codeurjc.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
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
import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.PoolService;
import es.codeurjc.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
public class UserRestController {
     @Autowired
    private PoolService poolService;

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
	public Lifeguard createLifeguard(@RequestBody Lifeguard lifeguard, HttpServletRequest request) {

		lifeguardService.save(lifeguard);

		return lifeguard;
	}

    @PostMapping("/api/employer")
	@ResponseStatus(HttpStatus.CREATED)
	public Employer createEmployer(@RequestBody Employer employer, HttpServletRequest request) {
		employer.setPass(passwordEncoder.encode(request.getParameter("pass")));
        employer.setRoles("USER", "EMP");
		employerService.save(employer);

		return employer;
	}

    @PutMapping("/api/lifeguard/{id}")
	public ResponseEntity<Lifeguard> updateLifeguard(@PathVariable long id, @RequestBody Lifeguard updatedLifeguard) throws SQLException {

		if (lifeguardService.existsById(id)) {

			if (updatedLifeguard.getImageUser()) {
				Lifeguard dbLifeguard = lifeguardService.findById(id).orElseThrow();
				if (dbLifeguard.getImageUser()) {
					updatedLifeguard.setPhotoUser(BlobProxy.generateProxy(dbLifeguard.getPhotoUser().getBinaryStream(),
							dbLifeguard.getPhotoUser().length()));
				}
			}

			updatedLifeguard.setId(id);
			lifeguardService.save(updatedLifeguard);

			return new ResponseEntity<>(updatedLifeguard, HttpStatus.OK);
		} else	{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

    @PutMapping("/api/employer/{id}")
	public ResponseEntity<Employer> updateEmployer(@PathVariable long id, @RequestBody Employer updatedEmployer) throws SQLException {

		if (employerService.existsById(id)) {

			if (updatedEmployer.getImageCompany()) {
				Employer dbEmployer = employerService.findById(id).orElseThrow();
				if (dbEmployer.getImageCompany()) {
					updatedEmployer.setPhotoCompany(BlobProxy.generateProxy(dbEmployer.getPhotoCompany().getBinaryStream(),
							dbEmployer.getPhotoCompany().length()));
				}
			}

			updatedEmployer.setId(id);
			employerService.save(updatedEmployer);

			return new ResponseEntity<>(updatedEmployer, HttpStatus.OK);
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

}