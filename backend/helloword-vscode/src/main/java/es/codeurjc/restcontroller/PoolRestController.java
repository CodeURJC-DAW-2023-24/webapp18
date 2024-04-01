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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.model.Message;
import es.codeurjc.model.Pool;
import es.codeurjc.service.MessageService;
import es.codeurjc.service.PoolService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class PoolRestController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private PoolService poolService;

// BUSCAR TODAS LAS PISCINAS
 @GetMapping("/pools")
    public ResponseEntity<List<Pool>> getAllPools() {
        List<Pool> pools = poolService.findAll();
        if (!pools.isEmpty()) {
            return ResponseEntity.ok(pools);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
// BUSCAR PISCINAS ESPECÍFICAS POR ID
    @GetMapping("/pool/{id}")
    public ResponseEntity<Pool> getPool(@PathVariable int id, HttpServletRequest request) {
        Optional<Pool> pool = poolService.findById(id);
        if (pool.isPresent()) {
            return ResponseEntity.ok(pool.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/pool")
    public ResponseEntity<Pool> addPool(HttpServletRequest request, HttpSession session,
            @RequestParam("name") String name, @RequestParam("dir") String dir,
            @RequestParam("description") String description, @RequestParam("aforo") int aforo,
            @RequestParam("start") LocalTime start, @RequestParam("close") LocalTime close,
            MultipartFile photoField) throws IOException {

        Pool pool = new Pool.Builder().name(name).photo("/images/default-image.jpg").direction(dir).capacity(10)
                .scheduleStart(start).scheduleEnd(close).company("Null").description(description).build();
        if (!photoField.isEmpty()) {
            pool.setPhotoUser(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
            pool.photoCheck = true;
        }
        poolService.save(pool);
        return ResponseEntity.ok(pool);
    }

    @DeleteMapping("/pool/{id}")
    public ResponseEntity<Void> deletePool(@PathVariable int id, HttpServletRequest request) {
        poolService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pool/{id}/message")
    public ResponseEntity<Message> newMessage(@RequestBody String input, @PathVariable int id,
            HttpServletRequest request) {

        String mail = request.getUserPrincipal().getName();
        Message message = new Message(mail, input);
        Pool pool = poolService.findById(id).get();
        pool.addMessage(message);
        messageService.save(message);
        poolService.save(pool);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/pool/{id}/message/{messageId}")
    public ResponseEntity<Void> deletePoolMessage(@PathVariable int id, @PathVariable int messageId,
            HttpServletRequest request) {

        Message msg = messageService.findById(messageId).get();
        Pool pool = msg.getPool();
        messageService.deleteById(messageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pool/{id}/image")
    public ResponseEntity<Resource> downloadImage(@PathVariable int id) throws SQLException {
        Optional<Pool> pool = poolService.findById(id);
        if (pool.isPresent() && pool.get().getPhotoUser() != null) {
            Resource file = new InputStreamResource(pool.get().getPhotoUser().getBinaryStream());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(pool.get().getPhotoUser().length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
