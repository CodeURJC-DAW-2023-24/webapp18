package es.codeurjc.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;
import es.codeurjc.service.OfferService;
import es.codeurjc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private LifeguardRepository lifeguardRepository;

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    // -------------------------------------- PROFILE ------------------------------------------
    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request, @RequestParam("type") int type,
            @RequestParam("mail") String m) {
        // CHECK USER LOGED OR NOT
        model.addAttribute("loged", request.getUserPrincipal() != null);

        // Here you must pass the ID of the person logged in when publishing the
        // referenced message.
        if (type == 1) {
            String mail = request.getUserPrincipal().getName();

            model.addAttribute("me", true);

            Optional<Employer> employer = employerRepository.findByMail(mail);
            Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(mail);
            Collection<Offer> offers = offerService.findAll();
            if (employer.isPresent()) {
                Employer employerCast = employer.get();
                employerCast.setOffersEmpty();
                for (Offer offer : offers) {
                    if (offer.getPool().getCompany().equals(employerCast.getCompany())) {
                        employerCast.addOffer(offer);
                    }
                }
                employerRepository.save(employerCast);
                model.addAttribute("user", employerCast);
                model.addAttribute("employer", request.isUserInRole("USER"));

            } else if (lifeguard.isPresent()) {
                Lifeguard lifeguardCast = lifeguard.get();
                model.addAttribute("user", lifeguardCast);
                model.addAttribute("lifeguard", request.isUserInRole("USER"));
                List<Offer> offersLifeguard = new ArrayList<Offer>();
                for (Offer offer : offers) {
                    if (offer.getLifeguard() != null
                            && offer.getLifeguard().getMail().equals(lifeguardCast.getMail())) {
                        offersLifeguard.add(offer);
                    }
                }
                model.addAttribute("offersLifeguard", offersLifeguard);
            }
        } else if (type == 0) {
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            Optional<Employer> employer = employerRepository.findByMail(m);
            Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(m);
            model.addAttribute("me", false);
            if (employer.isPresent()) {
                Employer employerCast = employer.get();
                model.addAttribute("user", employerCast);

            } else if (lifeguard.isPresent()) {
                Lifeguard lifeguardCast = lifeguard.get();
                model.addAttribute("user", lifeguardCast);
            }
        }

        return "profile";
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(Model model, HttpServletRequest request, @RequestParam("mail") String mail) {

        userService.deleteUserByEmail(mail);
        model.addAttribute("employer", request.isUserInRole("EMP"));

        return "index";
    }

    @GetMapping("/loged")
    public String loged(Model model, HttpServletRequest request) {
        // CHECK USER LOGED OR NOT
        model.addAttribute("loged", request.getUserPrincipal() != null);

        model.addAttribute("title", "Sesión iniciada");
        model.addAttribute("message", "Has iniciado sesión correctamente");
        model.addAttribute("back", "/profile");
        return "message";
    }

    @GetMapping("/user/form")
    public String loadNewUser(Model model, HttpServletRequest request) {

        // CHECK USER LOGED OR NOT
        model.addAttribute("loged", request.getUserPrincipal() != null);

        model.addAttribute("edit", false);

        return "user_form";
    }

    @PostMapping("/user/register")
    public String newUser(HttpServletRequest request, HttpSession session, Model model, MultipartFile photoField) throws IOException {
        // CHECK USER LOGED OR NOT
        model.addAttribute("loged", request.getUserPrincipal() != null);

        String messageForm = userService.checkForm(request.getParameter("mail"), request.getParameter("age"),request.getParameter("phone"));
        if (!messageForm.equals("")) {
            model.addAttribute("title", "Error");
            model.addAttribute("message", messageForm);
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }

        model.addAttribute("title", "Éxito");
        model.addAttribute("back", "/");

        String typeUser = request.getParameter("typeUser");
        switch (typeUser) {
            case "employer":
            System.out.println(request);
                Employer employer = userService.createEmployer(request);
                if (!photoField.isEmpty()) {
                    employer.setPhotoCompany(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
                    employer.setImageCompany(true);
                }
                employerRepository.save(employer);
                model.addAttribute("message", "Nuevo empleado creado correctamente");
                break;

            case "lifeguard":
                Lifeguard lifeguard = userService.createLifeguard(request);
                if (!photoField.isEmpty()) {
                    lifeguard.setPhotoUser(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
                    lifeguard.setImageUser(true);
                }
                lifeguardRepository.save(lifeguard);
                model.addAttribute("message", "Nuevo socorrista creado correctamente");
                break;

            default:
                model.addAttribute("title", "Error");
                model.addAttribute("message", "Tienes que seleccionar si eres un socorrista o un empleado");
                model.addAttribute("back", "javascript:history.back()");
                break;
        }

        return "message";
    }

    @GetMapping("/user/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Optional<Employer> employer = employerRepository.findById(id);
        Optional<Lifeguard> lifeguard = lifeguardRepository.findById(id);
        if (employer.isPresent() && employer.get().getPhotoCompany() != null) {

            Resource file = new InputStreamResource(employer.get().getPhotoCompany().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(employer.get().getPhotoCompany().length()).body(file);

        } else if (lifeguard.isPresent() && lifeguard.get().getPhotoUser() != null) {

            Resource file = new InputStreamResource(lifeguard.get().getPhotoUser().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(lifeguard.get().getPhotoUser().length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/availableMail")
    public ResponseEntity<?> checkMailAvailability(@RequestParam String mail) {
        boolean available = true;
        Optional<Employer> employer = employerRepository.findByMail(mail);
        if (employer.isPresent()) {
            available = false;
        }
        Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(mail);
        if (lifeguard.isPresent()) {
            available = false;
        }
        return ResponseEntity.ok().body(Map.of("available", available));
    }

    @GetMapping("/user/{mail}/edit")
    public String showFormEdit(@PathVariable String mail, Model model, HttpServletRequest request) {
        Optional<Employer> employer = employerRepository.findByMail(mail);
        Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(mail);
        if (employer.isPresent()) {
            model.addAttribute("user", employer.get());
            model.addAttribute("employer", true);
        } else if (lifeguard.isPresent()) {
            model.addAttribute("user", lifeguard.get());
            model.addAttribute("lifeguard", true);
        }
        model.addAttribute("edit", true);
        return "user_form";
    }

    @PostMapping("/user/{mail}/edit")
    public String editUser(@PathVariable String mail, HttpServletRequest request, Model model, @RequestParam("photoField") MultipartFile photoField) throws IOException {
        Optional<Employer> employer = employerRepository.findByMail(mail);
        Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(mail);
        if (employer.isPresent()) {
            userService.updatePerson(employer.get(), request);
            employer.get().setCompany(request.getParameter("company"));
            if (!photoField.isEmpty()) {
			    employer.get().setPhotoCompany(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
		    }
            employerRepository.save(employer.get());
        } else if (lifeguard.isPresent()) {
            userService.updatePerson(lifeguard.get(), request);
            lifeguard.get().setDocument(request.getParameter("document"));
            if (!photoField.isEmpty()) {
			    lifeguard.get().setPhotoUser(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
		    }
            lifeguardRepository.save(lifeguard.get());
        }

        return "redirect:/profile?type=1&mail="+mail;
    }

    // -------------------------------------- LOGIN --------------------------------------
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/loginerror")
    public String loginerror(Model model) {
        model.addAttribute("title", "Error");
        model.addAttribute("message", "Credenciales inválidas");
        model.addAttribute("back", "/login");

        return "message";
    }
}
