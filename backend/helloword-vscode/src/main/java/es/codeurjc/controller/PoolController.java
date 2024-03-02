package es.codeurjc.controller;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.model.Message;
import es.codeurjc.model.Pool;
import es.codeurjc.service.MessageService;
import es.codeurjc.service.PoolService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PoolController {
    @Autowired
	private MessageService messageService;
    
    @Autowired
    private PoolService poolService;

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
        Message msg = messageService.findById(id).get();
        Pool pool = msg.getPool();
        messageService.deleteById(id);
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

}
