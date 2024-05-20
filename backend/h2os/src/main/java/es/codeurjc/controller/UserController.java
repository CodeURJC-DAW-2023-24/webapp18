package es.codeurjc.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private UserService userService;

    // -------------------------------------- CARDS ------------------------------------------
    private Boolean userToggle = false;

    @GetMapping("/users")
    public String showUsers(Model model, HttpServletRequest request, Pageable page) {
        if (!request.isUserInRole("ADMIN")) {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Solo los administradores pueden acceder a esta página");
            model.addAttribute("back", "/");
            return "feedback";
        }

        if (this.userToggle) {
            model.addAttribute("type", "employers");
            model.addAttribute("title", "Empleadores");
            model.addAttribute("alternative", "Socorristas");
        } else {
            model.addAttribute("type", "lifeguards");
            model.addAttribute("title", "Socorristas");
            model.addAttribute("alternative", "Empleadores");
        }

        return "users";
    }

    @GetMapping("/users/change")
    public String changeUsersType() {
        this.userToggle = !this.userToggle;

        return "redirect:/users";
    }

    @GetMapping("/employers/load")
    public String loadEmployers(HttpServletRequest request, Model model, @RequestParam("page") int pageNumber,
            @RequestParam("size") int size) {

        Page<Employer> employers = userService.findAllEmployers(PageRequest.of(pageNumber, size));
        model.addAttribute("users", employers);
        model.addAttribute("hasMore", employers.hasNext());
        model.addAttribute("alternative", "No hay empleados");

        return "user_cards";
    }

    @GetMapping("/lifeguards/load")
    public String loadLifeguards(HttpServletRequest request, Model model, @RequestParam("page") int pageNumber,
            @RequestParam("size") int size) {

        Page<Lifeguard> lifeguards = userService.findAllLifeguards(PageRequest.of(pageNumber, size));
        model.addAttribute("users", lifeguards);
        model.addAttribute("hasMore", lifeguards.hasNext());
        model.addAttribute("alternative", "No hay socorristas");

        return "user_cards";
    }

    // -------------------------------------- PROFILE ------------------------------------------
    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request, @RequestParam("mail") String profileMail) {

        if (request.getUserPrincipal() == null) {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Debes iniciar sesión para acceder a los perfiles");
            model.addAttribute("back", "/login");
            return "feedback";
        }

        String myMail = request.getUserPrincipal().getName();
        Boolean isMe = myMail.equals(profileMail);
        Boolean isAdmin = request.isUserInRole("ADMIN");
        model.addAttribute("me", isMe);
        model.addAttribute("iamAdmin", isAdmin);
        model.addAttribute("canSeeOffers", isMe || isAdmin);

        Optional<Employer> employerOp = employerRepository.findByMail(profileMail);
        Optional<Lifeguard> lifeguardOp = lifeguardRepository.findByMail(profileMail);
        if (employerOp.isPresent()) {
            Employer employer = employerOp.get();
            model.addAttribute("employerProfile", true);
            model.addAttribute("user", employer);
            model.addAttribute("canSeeOffers", true);
        }
        else if (lifeguardOp.isPresent()) {
            Lifeguard lifeguard = lifeguardOp.get();
            model.addAttribute("lifeguardProfile", true);
            model.addAttribute("user", lifeguard);
            for (Offer offer : lifeguard.getOffers()) {
                Boolean accepted = (offer.isAccepted()) && (offer.getLifeguard().getMail().equals(profileMail));
                offer.setAcceptedByProfileUser(accepted);
            }
        }
        else {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Usuario no encontrado");
            model.addAttribute("back", "/");
            return "feedback";
        }

        return "profile";
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(Model model, HttpServletRequest request, @RequestParam("mail") String mail) {

        userService.deleteUserByEmail(mail);
        model.addAttribute("employer", request.isUserInRole("EMP"));

        return "redirect:/";
    }

    @GetMapping("/loged")
    public String loged(Model model, HttpServletRequest request) {
        // CHECK USER LOGED OR NOT
        model.addAttribute("loged", request.getUserPrincipal() != null);

        model.addAttribute("title", "Sesión iniciada");
        model.addAttribute("message", "Has iniciado sesión correctamente");
        model.addAttribute("back", "/profile");
        return "feedback";
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
            return "feedback";
        }

        model.addAttribute("title", "Éxito");
        model.addAttribute("back", "/");

        String typeUser = request.getParameter("typeUser");
        switch (typeUser) {
            case "employer":
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

        return "feedback";
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

        return "redirect:/profile?&mail="+mail;
    }

    // -------------------------------------- LOGIN --------------------------------------
    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            String mail = request.getUserPrincipal().getName();
            return "redirect:/profile?mail="+mail;
        }
        else
            return "login";
    }

    @RequestMapping("/login/error")
    public String loginerror(Model model) {
        model.addAttribute("title", "Error");
        model.addAttribute("message", "Credenciales inválidas");
        model.addAttribute("back", "/login");

        return "feedback";
    }
}
