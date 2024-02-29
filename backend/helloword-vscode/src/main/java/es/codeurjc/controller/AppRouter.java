package es.codeurjc.controller;

import org.springframework.ui.Model;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import es.codeurjc.service.OfferService;
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
    private OfferService offerService;

	@Autowired 
	private EmployerRepository employerRepository;

	@Autowired
	private LifeguardRepository lifeguardRepository;

        
    @Autowired
	private PasswordEncoder passwordEncoder;

    // -------------------------------------- MAIN --------------------------------------
    @GetMapping("/")
    public String initial(Model model,HttpServletRequest request) {
        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        return "index";
    }

    // -------------------------------------- OFFERS --------------------------------------
    @GetMapping("/loadOffers")
    public String loadOffers(HttpServletRequest request, Model model) {
        int from = Integer.parseInt(request.getParameter("from"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        Collection<Offer> offers2 = offerService.findAll();
        List<Offer> offersL = new ArrayList(offers2);
        List<Offer> offerSubL = offersL.subList(from, amount);
        Offer[] offers = DataBase.getOffers(from, amount);
        model.addAttribute("offers", offerSubL);
        model.addAttribute("alternative", "No hay ofertas aún");
        return "offers";
    }

    @GetMapping("/allOffersLoaded")
    public ResponseEntity<?> allOffersLoaded() {
        boolean value = DataBase.allOffersLoaded();
        return ResponseEntity.ok().body(Map.of("value", value));
    }

    @GetMapping("/offer")
    public String offer(@RequestParam("id") int id, Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        Offer offer = offerService.findById(id).get();
        model.addAttribute("offer", offer);
        model.addAttribute("id", offer.getId());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        model.addAttribute("lifeguard", request.isUserInRole("LIFE") && !offer.isOffered(request.getUserPrincipal().getName()) && ! request.getUserPrincipal().getName().equals("admin"));
        model.addAttribute("employer", request.isUserInRole("EMP"));
        return "offer";
    }

    @GetMapping("/offer/form")
    public String newOffer(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }        

        return "new_offer";
    }

    @PostMapping("/offer/add")
    public String addOffer(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }        

        return "redirect:/offer/added";
    }
    


    @GetMapping("/offer/added")
    public String addedOffer(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        model.addAttribute("title", "Oferta añadida");
        model.addAttribute("message", "Oferta añadida correctamente. ¡Gracias por confiar en nosotros!");
        model.addAttribute("back", "/");
        return "message";
    }


    @GetMapping("/offer/offered/load")
    public String loadOffered(@RequestParam("id") int id, Model model,HttpServletRequest request) {
        Offer offer = offerService.findById(id).get();
        List<Lifeguard> lifeguards = offer.getLifeguards();
        // Hay que hacer que los mensajes sean referentes a pool
        //Collection<Message> messagesBD = messageService.findAll();

        if(offer.getLifeguard()!=null){
            for (Lifeguard lifeguard: lifeguards){
                if(offer.getLifeguard().getMail().equals(lifeguard.getMail())){
                    lifeguard.setofferAssigned(true);
                    break;
                }
            }
        }
        System.out.println("load "+id);

        model.addAttribute("offer", offer);
        model.addAttribute("lifeguards",lifeguards);
        model.addAttribute("id", id);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        model.addAttribute("employer", request.isUserInRole("EMP"));
        return "offered";
    }

    @PostMapping("/offer/offered/new")
    public String newOffered(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        //   Pool pool = DataBase.getPool(id);
        Offer offer = offerService.findById(id).get();
        Lifeguard lifeguard = userService.findLifeguardByEmail(request.getUserPrincipal().getName()).get();
        offer.addOffered(lifeguard);
        lifeguard.addOffer(offer);
        offerService.save(offer);
        userService.saveLifeguard(lifeguard);

        System.out.println("new "+id);

        
        model.addAttribute("offer", offer);
        model.addAttribute("id", offer.getId());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        model.addAttribute("lifeguard", false);
        model.addAttribute("employer", request.isUserInRole("EMP"));
        return "offer";
    }
    @PostMapping("/offer/offered/set")
    public String LifeguardSetted(@RequestParam("ido") int id,@RequestParam("lg") String lg, Model model, HttpServletRequest request) {
        //   Pool pool = DataBase.getPool(id);
        Offer offer = offerService.findById(id).get();
        Lifeguard lifeguard = userService.findLifeguardByEmail(lg).get();
        offer.setLifeguard(lifeguard);
        lifeguard.addOfferAccepted(offer);
        offerService.save(offer);
        userService.saveLifeguard(lifeguard);
        System.out.println("set "+id);
        
        return "redirect:/offer?id="+id;
    }

    @PostMapping("/offer/offered/delete")
    public String LifeguardDeleted(@RequestParam("ido") int id,@RequestParam("lg") String lg, Model model, HttpServletRequest request) {
        //   Pool pool = DataBase.getPool(id);
        Offer offer = offerService.findById(id).get();
        Lifeguard lifeguard = userService.findLifeguardByEmail(lg).get();
        offer.setLifeguard(null);
        offerService.save(offer);
        lifeguard.deleteOfferAccepted(offer);
        userService.saveLifeguard(lifeguard);
        
        return "redirect:/offer?id="+id;
    }


    // -------------------------------------- PROFILE --------------------------------------
    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request, @RequestParam("type") int type, @RequestParam("mail") String m) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        //Aqui se le debe pasar el id de la pesona con sesion iniciada al publicar el mensaje refereniado
        if(type==1){
            String mail = request.getUserPrincipal().getName();
            
            model.addAttribute("me",true);

        Optional<Employer> employer = employerRepository.findByMail(mail);
        Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(mail);
        Collection<Offer> offers = offerService.findAll();
        if (employer.isPresent()){
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

        }else if(lifeguard.isPresent()){   
            Lifeguard lifeguardCast = lifeguard.get();  
            model.addAttribute("user", lifeguardCast);     
            model.addAttribute("lifeguard", request.isUserInRole("USER"));      
            List<Offer> offersLifeguard = new ArrayList<Offer>();
            for (Offer offer : offers) {
                if (offer.getLifeguard() != null && offer.getLifeguard().getMail().equals(lifeguardCast.getMail())) {
                    offersLifeguard.add(offer);
                }
            }
            model.addAttribute("offersLifeguard",offersLifeguard);
        }
        }
        else if(type==0){
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            Optional<Employer> employer = employerRepository.findByMail(m);
            Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(m);
            model.addAttribute("me",false);
            if (employer.isPresent()){
                Employer employerCast = employer.get();
                model.addAttribute("user", employerCast);

            }else if(lifeguard.isPresent()){   
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
    /*@GetMapping("/login")
    public String sign(Model model) {
        return "login";
    }*/

   /*  @PostMapping("/login")
    public String login(Model model) {
        return "redirect:/loged";
    }
*/
    @GetMapping("/loged")
    public String loged(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }        

        model.addAttribute("title", "Sesión iniciada");
        model.addAttribute("message", "Has iniciado sesión correctamente");
        model.addAttribute("back", "/profile");
        return "message";
    }

    @GetMapping("/user/form")
    public String loadNewUser(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }
        
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

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }
    
        model.addAttribute("title", "Exito");
        String messageForm = checkForm(request.getParameter("mail"),request.getParameter("age"),request.getParameter("phone"));
    //    if (messageForm.equals("  ")){ DESACTIVADO POR AHORA PARA NO TARDAR AL LOGEARTE
            if ("employer".equals(typeUser)) {
                if (!photoCompanyField.isEmpty()) {
			    employer.setPhotoCompany(BlobProxy.generateProxy(photoCompanyField.getInputStream(), photoCompanyField.getSize()));
			    employer.setImageCompany(true);
		        }
                employer.setPass(passwordEncoder.encode(request.getParameter("pass")));
                employer.setRoles("USER","EMP");
                employerRepository.save(employer);
                model.addAttribute("message", "Nuevo empleado creado correctamente");
                model.addAttribute("back", "/");
            } else if ("lifeguard".equals(typeUser)) {
                if (!photoUserField.isEmpty()) {
                    lifeguard.setPhotoUser(BlobProxy.generateProxy(photoUserField.getInputStream(), photoUserField.getSize()));
                    lifeguard.setImageUser(true);
                    }
                lifeguard.setPass(passwordEncoder.encode(request.getParameter("pass")));
                System.out.println("LOG PASS" + passwordEncoder.encode(request.getParameter("pass")));
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
                lifeguard.setRoles("USER","LIFE");
                lifeguardRepository.save(lifeguard);
                model.addAttribute("message", "Nuevo socorrista creado correctamente");
                model.addAttribute("back", "/");
            } else {
                model.addAttribute("title", "Error");
                model.addAttribute("message", "Tienes que seleccionar si eres un socorrista o un empleado");
                model.addAttribute("back", "javascript:history.back()");

                return "message";
            }
        //}
        //model.addAttribute("message", messageForm);

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

		}else if(lifeguard.isPresent() && lifeguard.get().getPhotoUser() != null) {

			Resource file = new InputStreamResource(lifeguard.get().getPhotoUser().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(lifeguard.get().getPhotoUser().length()).body(file);
		} 
        else {
			return ResponseEntity.notFound().build();
		}
	}


   /* @PostMapping("/login")
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
    */

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
    public String pool(@RequestParam("id") int id, Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        //Pool pool = DataBase.getPool(id);

        Optional<Pool> pool = poolService.findById(id);
        if(pool.isPresent()){
        model.addAttribute("pool", pool.get());
        
    }
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        return "pool";
    }

    @PostMapping("pool/delete")
    public String deletePool(@RequestParam("id") int id, Model model,HttpServletRequest request) {
        poolService.deleteById(id);
        return "index";
    }

    @GetMapping("/pool/message/load")
    public String loadMessages(@RequestParam("id") int id, Model model,HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        //Pool pool = DataBase.getPool(id);
        Pool pool = poolService.findById(id).get();
        List<Message> messages = pool.getMessages();
        // Hay que hacer que los mensajes sean referentes a pool
        //Collection<Message> messagesBD = messageService.findAll();

        model.addAttribute("messages", messages);
        model.addAttribute("poolId", id);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        return "poolMessage";
    }

    @PostMapping("/pool/message/add")
    public String newMessage(@RequestParam("commentInput") String input, @RequestParam("id") int id, Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        String mail = request.getUserPrincipal().getName();
        Message message = new Message(mail, input);
     //   Pool pool = DataBase.getPool(id);
        Pool pool = poolService.findById(id).get();
        pool.addMessage(message);
        messageService.save(message);
        poolService.save(pool);
        model.addAttribute("pool", pool);
        return "pool";
    }

    @PostMapping("/pool/message/delete")
    public String deletePoolMessage(@RequestParam("id") int id, Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        // Pool pool = DataBase.getPool(idP);
        System.out.println("Se va a borrar el mensaje "+id+" de la piscina");
        Message msg = messageService.findById(id).get();
        Pool pool = msg.getPool();
        System.out.println("ID:"+id);
        System.out.println("Correspondiente al id de mensaje: "+id);
        messageService.deleteById(id); //Esto puede ser que sobre su ponemos el borrado en cascada
        model.addAttribute("pool", pool);
        return "pool";

    } 
    @PostMapping("/pool/add")
    public String addPool(HttpServletRequest request, HttpSession session, Model model,
                    @RequestParam("name") String name,
                    @RequestParam("dir") String dir,
                    @RequestParam("description") String description,
                    @RequestParam("aforo") int aforo,
                    @RequestParam("start") LocalTime start,
                    @RequestParam("close") LocalTime close,
                    MultipartFile photoPool) {


        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        Pool pool = new Pool.Builder()
                        .name(name)
                        .photo("/images/default-image.jpg")
                        .direction(dir)
                        .capacity(10)
                        .scheduleStart(start)
                        .scheduleEnd(close)
                        .company("Null")
                        .description(description)
                        .build();
        //poner la foto en la pool
        poolService.save(pool);
        return "index";
    } 

    @GetMapping("/pool/form")
    public String newPool(Model model,HttpServletRequest request){

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        return "new_pool";
    }

    // -------------------------------------- MAPS --------------------------------------
    @GetMapping("/maps")
    public String maps(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        return "maps";
    }
}