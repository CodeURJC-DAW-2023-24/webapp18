package es.codeurjc.controller;

import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Message;
import es.codeurjc.model.Offer;
import es.codeurjc.model.Person;
import es.codeurjc.model.Pool;
import es.codeurjc.repository.DataBase;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;
import es.codeurjc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import es.codeurjc.service.MessageService;
import es.codeurjc.service.PoolService;

import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class AppRouter {
    private DataBase db = new DataBase();

    private static final String MESSAGES_FOLDER = "messages";

	@Autowired
	private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private PoolService poolService;

	@Autowired 
	private EmployerRepository employerRepository;

	@Autowired
	private LifeguardRepository lifeguardRepository;

    // -------------------------------------- MAIN --------------------------------------
    @GetMapping("/")
    public String initial(Model model) {
        return "index";
    }

    // -------------------------------------- OFFERS --------------------------------------
    @GetMapping("/loadOffers")
    public String loadOffers(HttpServletRequest request, Model model) {
        int from = Integer.parseInt(request.getParameter("from"));
        int amount = Integer.parseInt(request.getParameter("amount"));

        Offer[] offers = DataBase.getOffers(from, amount);
        model.addAttribute("offers", offers);
        model.addAttribute("alternative", "No hay ofertas aún");
        return "offers";
    }

    @GetMapping("/allOffersLoaded")
    public ResponseEntity<?> allOffersLoaded() {
        boolean value = DataBase.allOffersLoaded();
        return ResponseEntity.ok().body(Map.of("value", value));
    }

    @GetMapping("/offer")
    public String offer(@RequestParam("id") int id, Model model) {
        Offer offer = DataBase.getOffer(id);
        model.addAttribute("offer", offer);
        return "offer";
    }

    @GetMapping("/offer/form")
    public String newOffer(Model model) {
        return "new_offer";
    }

    @PostMapping("/offer/add")
    public String addOffer(Model model) {
        return "redirect:/offer/added";
    }

    @GetMapping("/offer/added")
    public String addedOffer(Model model) {
        model.addAttribute("title", "Oferta añadida");
        model.addAttribute("message", "Oferta añadida correctamente. ¡Gracias por confiar en nosotros!");
        model.addAttribute("back", "/");
        return "message";
    }

    // -------------------------------------- PROFILE --------------------------------------
    /*@GetMapping("/profile")
    public String profile(Model model) {
        Person person = DataBase.getPerson(0);
        model.addAttribute("user", person);
        return "profile";
    }
*/
    @GetMapping("/login")
    public String sign(Model model) {
        return "login";
    }

   /*  @PostMapping("/login")
    public String login(Model model) {
        return "redirect:/loged";
    }
*/
    @GetMapping("/loged")
    public String loged(Model model) {
        model.addAttribute("title", "Sesión iniciada");
        model.addAttribute("message", "Has iniciado sesión correctamente");
        model.addAttribute("back", "/profile");
        return "message";
    }

    @GetMapping("/user/form")
    public String loadNewUser(Model model) {
        return "new_user";
    }

    //FALTARIA METER LA COMPROBACION DE QUE NO ESTE REPETIDO EL MAIL
    public String checkForm(String mail, String age, String phone){
        int phoneNum = 0;
        int ageNum = 0;
        String message1 = "";
        String message2 = "";
        String message3 = "";

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

        Optional<Employer> employer = employerRepository.findByMail(mail);
        if (employer.isPresent()){
            message3 = "Correo ya en uso por otro empleado.";
        }
        Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(mail);
        if (lifeguard.isPresent()){
            message3 = "Correo ya en uso por otro socorrista.";
        }

    return message1 + " " + message2 + " " + message3;
    }

    @PostMapping("/user/register")
    public String newUser(HttpServletRequest request, HttpSession session, Model model, Lifeguard lifeguard, Employer employer, String typeUser, boolean reliability,
            boolean effort, boolean communication, boolean attitude, boolean problemsResolution, boolean leadership, MultipartFile photoUserField, MultipartFile photoCompanyField) throws IOException {
        model.addAttribute("title", "Exito");
       String messageForm = checkForm(request.getParameter("mail"),request.getParameter("age"),request.getParameter("phone"));
    //    if (messageForm.equals("  ")){ DESACTIVADO POR AHORA PARA NO TARDAR AL LOGEARTE
            if ("employer".equals(typeUser)) {
                if (!photoCompanyField.isEmpty()) {
			    employer.setPhotoCompany(BlobProxy.generateProxy(photoCompanyField.getInputStream(), photoCompanyField.getSize()));
			    employer.setImageCompany(true);
		        }
                employerRepository.save(employer);
                model.addAttribute("message", "Nuevo empleado creado correctamente");
            } else if ("lifeguard".equals(typeUser)) {
                if (!photoUserField.isEmpty()) {
                    lifeguard.setPhotoUser(BlobProxy.generateProxy(photoUserField.getInputStream(), photoUserField.getSize()));
                    lifeguard.setImageUser(true);
                    }
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
                lifeguardRepository.save(lifeguard);
                model.addAttribute("message", "Nuevo socorrista creado correctamente");
            } else {
                model.addAttribute("title", "Error");
                model.addAttribute("message", "Tienes que seleccionar si eres un socorrista o un empleado");
                model.addAttribute("back", "javascript:history.back()");

                return "message";
            }
        //}
        //model.addAttribute("message", messageForm);
        model.addAttribute("back", "javascript:history.back()");

        return "message";
    }

    @PostMapping("/login")
    public String loginUser(Model model,@RequestParam String mail, @RequestParam String password) {
        model.addAttribute("title", "Error");
        model.addAttribute("back", "javascript:history.back()");
        Optional<Employer> employer = employerRepository.findByMail(mail);
        if (employer.isPresent()){
            Employer employerCast = employer.get();
            if (password.equals(employerCast.getPass())){
                model.addAttribute("message", "Empleado logeado correctamente");
            }else{
                model.addAttribute("message", "E-mail y contraseña incorrectos");
            }

        }else{     
            Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(mail);
            if (lifeguard.isPresent()){
                Lifeguard lifeguardCast = lifeguard.get();
            if (password.equals(lifeguardCast.getPass())){
                model.addAttribute("message", "Socorrista logeado correctamente");
            }else{
                model.addAttribute("message", "E-mail y contraseña incorrectos");
            }
                
            }else{
                model.addAttribute("message","El correo introducido no está registrado");
            }
        }
        
        return "message";
    }
    

    //PREGUNTAR AL PROFE POR QUE NO FUNCIONA SI EL TRUE Y EL FALSE LO HACE BIEN
    @GetMapping("/availableMail")
    public ResponseEntity<?> checkMailAvailability(@RequestParam String mail) {
        boolean available = true;
        Optional<Employer> employer = employerRepository.findByMail(mail);
        if (employer.isPresent()){
            available = false;
        }
        Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(mail);
        if (lifeguard.isPresent()){
            available = false;
        }  
        return ResponseEntity.ok().body(Map.of("available", available));
    }
    
    
 /*    @GetMapping("/user/registered")
    public String registeredUser(HttpSession session, Model model) {
        String message = (String) session.getAttribute("message");

        model.addAttribute("title", "Usuario registrado");
        model.addAttribute("message", message);
        model.addAttribute("back", "/");
        return "message";
    }
*/
    // -------------------------------------- POOL --------------------------------------
    @GetMapping("/pool")
    public String pool(@RequestParam("id") int id, Model model) {
        //Pool pool = DataBase.getPool(id);

        Optional<Pool> pool = poolService.findById(id);
        if(pool.isPresent()){
        model.addAttribute("pool", pool.get());
    }
        return "pool";
    }

    @GetMapping("/pool/message/load")
    public String loadMessages(@RequestParam("id") int id, Model model) {
        //Pool pool = DataBase.getPool(id);
        Pool pool = poolService.findById(id).get();
        List<Message> messages = pool.getMessages();
        // Hay que hacer que los mensajes sean referentes a pool
        //Collection<Message> messagesBD = messageService.findAll();

        model.addAttribute("messages", messages);
        model.addAttribute("poolId", id);
        return "poolMessage";
    }

    @PostMapping("/pool/message/add")
    @ResponseBody
    public void newMessage(@RequestParam("msg") String input, @RequestParam("id") int id, Model model) {
        Message message = new Message("null", input);
     //   Pool pool = DataBase.getPool(id);
        Pool pool = poolService.findById(id).get();
        pool.addMessage(message);
        messageService.save(message);
        poolService.save(pool);
    }

    @PostMapping("/pool/message/delete")
    @ResponseBody
    public void deletePoolMessage(@RequestParam("idP") int idP, @RequestParam("idM") int idM, Model model) {
        // Pool pool = DataBase.getPool(idP);
        Pool pool = poolService.findById(idP).get();
        pool.deleteMessage(idM);
        messageService.deleteById(idM);
        poolService.save(pool);
    }

}