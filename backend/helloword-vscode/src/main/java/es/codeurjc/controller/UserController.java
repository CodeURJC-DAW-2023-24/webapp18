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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    // -------------------------------------- MAIN
    // --------------------------------------
    @GetMapping("/")
    public String initial(Model model, HttpServletRequest request) {
        // CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null) {
            model.addAttribute("loged", true);
        } else {
            model.addAttribute("loged", false);
        }

        return "index";
    }

    // -------------------------------------- PROFILE
    // ------------------------------------------
    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request, @RequestParam("type") int type,
            @RequestParam("mail") String m) {

        // CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null) {
            model.addAttribute("loged", true);
        } else {
            model.addAttribute("loged", false);
        }

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

        return "index";
    }

    @GetMapping("/loged")
    public String loged(Model model, HttpServletRequest request) {

        // CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null) {
            model.addAttribute("loged", true);
        } else {
            model.addAttribute("loged", false);
        }

        model.addAttribute("title", "Sesión iniciada");
        model.addAttribute("message", "Has iniciado sesión correctamente");
        model.addAttribute("back", "/profile");
        return "message";
    }

    @GetMapping("/user/form")
    public String loadNewUser(Model model, HttpServletRequest request) {

        // CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null) {
            model.addAttribute("loged", true);
        } else {
            model.addAttribute("loged", false);
        }

        return "new_user";
    }

    public String checkForm(String mail, String age, String phone) {
        int phoneNum = 0;
        int ageNum = 0;
        String message1 = "";
        String message2 = "";
        String message3 = "";
        if(!phone.equals("")){
            try {
                phoneNum = Integer.parseInt(phone);
            } catch (NumberFormatException e) {
                message1 = "El teléfono debe ser un número.";
            }

            if (phoneNum < 0) {
                message1 = "El teléfono debe ser un número positivo.";
            }

            if (String.valueOf(phoneNum).length() != 9) {
                message1 = "El teléfono debe tener 9 cifras.";
            }
        }
        if(!age.equals("")){
            try {
                ageNum = Integer.parseInt(age);
            } catch (NumberFormatException e) {
                message2 = "La edad debe ser un número.";
            }

            if (ageNum < 0) {
                message2 = "La edad debe ser un número positivo.";
            }
            
            if (ageNum % 1 != 0) {
                message2 = "La edad debe ser un número entero.";
            }
        }

        Optional<Employer> employer = employerRepository.findByMail(mail);
        if (employer.isPresent()) {
            message3 = "Correo ya en uso por otro empleado.";
        }
        Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(mail);
        if (lifeguard.isPresent()) {
            message3 = "Correo ya en uso por otro socorrista.";
        }

        return message1 + " " + message2 + " " + message3;
    }

    @PostMapping("/user/register")
    public String newUser(HttpServletRequest request, HttpSession session, Model model, Lifeguard lifeguard,
            Employer employer, String typeUser, boolean reliability,
            boolean effort, boolean communication, boolean attitude, boolean problemsResolution, boolean leadership,
            MultipartFile photoUserField, MultipartFile photoCompanyField) throws IOException {

        // CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null) {
            model.addAttribute("loged", true);
        } else {
            model.addAttribute("loged", false);
        }

        model.addAttribute("title", "Exito");
        String messageForm = checkForm(request.getParameter("mail"), request.getParameter("age"),request.getParameter("phone"));
        if (messageForm.equals("  ")) {
            if ("employer".equals(typeUser)) {
                if (!photoCompanyField.isEmpty()) {
                    employer.setPhotoCompany(
                            BlobProxy.generateProxy(photoCompanyField.getInputStream(), photoCompanyField.getSize()));
                    employer.setImageCompany(true);
                }
                employer.setPass(passwordEncoder.encode(request.getParameter("pass")));
                employer.setRoles("USER", "EMP");
                employerRepository.save(employer);
                model.addAttribute("message", "Nuevo empleado creado correctamente");
                model.addAttribute("back", "/");
            } else if ("lifeguard".equals(typeUser)) {
                if (!photoUserField.isEmpty()) {
                    lifeguard.setPhotoUser(
                            BlobProxy.generateProxy(photoUserField.getInputStream(), photoUserField.getSize()));
                    lifeguard.setImageUser(true);
                }
                lifeguard.setPass(passwordEncoder.encode(request.getParameter("pass")));
                if (reliability) {
                    lifeguard.addSkill("Confianza");
                }
                if (effort) {
                    lifeguard.addSkill("Esfuerzo");
                }
                if (communication) {
                    lifeguard.addSkill("Comunicación");
                }
                if (attitude) {
                    lifeguard.addSkill("Actitud positiva");
                }
                if (problemsResolution) {
                    lifeguard.addSkill("Resolución de problemas");
                }
                if (leadership) {
                    lifeguard.addSkill("Liderazgo");
                }
                lifeguard.setRoles("USER", "LIFE");
                lifeguardRepository.save(lifeguard);
                model.addAttribute("message", "Nuevo socorrista creado correctamente");
                model.addAttribute("back", "/");
            } else {
                model.addAttribute("title", "Error");
                model.addAttribute("message", "Tienes que seleccionar si eres un socorrista o un empleado");
                model.addAttribute("back", "javascript:history.back()");

                return "message";
            }
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", messageForm);
            model.addAttribute("back", "javascript:history.back()");
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

    //// -------------------------------------- LOGIN
    //// --------------------------------------
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
