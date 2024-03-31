package es.codeurjc.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.sql.SQLException;

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
        //Pool pool = DataBase.getPool(id);

        Optional<Pool> pool = poolService.findById(id);
        if(pool.isPresent()){
        model.addAttribute("pool", pool.get());
        if(pool.get().getPhotoUser()!=null) model.addAttribute("hasPhoto", true);
        else model.addAttribute("hasPhoto", false);
        
    }
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        model.addAttribute("logged", request.isUserInRole("EMP")||request.isUserInRole("LIFE"));
        return "pool";
    }

    @PostMapping("pool/delete")
    public String deletePool(@RequestParam("id") int id, Model model,HttpServletRequest request) {
        poolService.deleteById(id);
        model.addAttribute("employer", request.isUserInRole("EMP"));
        return "index";
    }

    @GetMapping("/pool/message/load")
    public String loadMessages(@RequestParam("id") int id, Model model,HttpServletRequest request) {
        //Pool pool = DataBase.getPool(id);
        Pool pool = poolService.findById(id).get();
        List<Message> messages = pool.getMessages();
        //Collection<Message> messagesBD = messageService.findAll();
        if (request.getUserPrincipal() != null){
            for(Message message: messages){
                message.setOwner(request.isUserInRole("ADMIN")|| message.getAuthor().equals(request.getUserPrincipal().getName()));
            }
    }
        else{
            for(Message message: messages){
                message.setOwner(false);
            }
        }
        model.addAttribute("messages", messages);
        model.addAttribute("poolId", id);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        return "pool_comments";
    }
    @GetMapping("/pool/editB")
    public String editPoolsB(@RequestParam("id") int id, Model model,HttpServletRequest request) {
        //Pool pool = DataBase.getPool(id);
        Pool pool = poolService.findById(id).get();
        model.addAttribute("pool", pool);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        model.addAttribute("edit", true);
        return "pool_form";
    }
    @PostMapping("/pool/message/add")
    public String newMessage(@RequestParam("commentInput") String input, @RequestParam("id") int id, Model model, HttpServletRequest request) {
        String mail = request.getUserPrincipal().getName();
        Message message = new Message(mail, input);
     //   Pool pool = DataBase.getPool(id);
        Pool pool = poolService.findById(id).get();
        pool.addMessage(message);
        messageService.save(message);
        poolService.save(pool);
        model.addAttribute("hasPhoto", pool.photoCheck);
        model.addAttribute("pool", pool);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        return "pool";
    }

    @PostMapping("/pool/message/delete")
    public String deletePoolMessage(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        // Pool pool = DataBase.getPool(idP);
        Message msg = messageService.findById(id).get();
        Pool pool = msg.getPool();
        messageService.deleteById(id);
        
        model.addAttribute("pool", pool);
        model.addAttribute("hasPhoto", pool.photoCheck);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
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
                    MultipartFile photoField) throws IOException {

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
        if (!photoField.isEmpty()) {
                    pool.setPhotoUser(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
                    pool.photoCheck=true;
                }
        poolService.save(pool);
        return "index";
    } 
    @PostMapping("/pool/edit")
    public String editPool(HttpServletRequest request, HttpSession session, Model model,
                    @RequestParam("id") int id,
                    @RequestParam("name") String name,
                    @RequestParam("dir") String dir,
                    @RequestParam("description") String description,
                    @RequestParam("aforo") String aforo,
                    @RequestParam("start") LocalTime start,
                    @RequestParam("close") LocalTime close,
                    MultipartFile photoField) throws IOException {

        Pool pool = poolService.findById(id).get();
        pool.setPhoto("/images/default-image.jpg");
        if (!photoField.isEmpty()) {
            pool.setPhotoUser(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
            pool.photoCheck=true;
        }
        if("".equals(aforo+"")) pool.setCapacity(20);
        else pool.setCapacity(Integer.valueOf(aforo));
        pool.setCompany("");
        pool.setDescription(description);
        pool.setDirection(dir);
        pool.setStart(start);
        pool.setEnd(close);
        pool.setName(name);
        poolService.save(pool);
        model.addAttribute("hasPhoto", pool.photoCheck);
        model.addAttribute("pool", pool);
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        return "pool";
    } 
    @GetMapping("/pool/form")
    public String newPool(Model model,HttpServletRequest request){
        return "pool_form";
    }
    
    @GetMapping("/pool/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Optional<Pool> pool = poolService.findById(id);
        if (pool.isPresent() && pool.get().getPhotoUser() != null) {

            Resource file = new InputStreamResource(pool.get().getPhotoUser().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(pool.get().getPhotoUser().length()).body(file);

        } 
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
