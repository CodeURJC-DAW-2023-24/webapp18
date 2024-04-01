package es.codeurjc.restcontroller;

import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestBody;

import es.codeurjc.DTO.MessageDTO;
import es.codeurjc.DTO.PoolDTO;
import es.codeurjc.model.Employer;
import es.codeurjc.model.Message;
import es.codeurjc.model.Pool;
import es.codeurjc.service.MessageService;
import es.codeurjc.service.PoolService;
import es.codeurjc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/pools")
public class PoolRestController {
    @Autowired
    private PoolService poolService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    // ----------------------------------------------- GET -----------------------------------------------
    @Operation(summary = "Get paginated pools.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pools found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PoolDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Pools not found, probably high page number supplied", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PoolDTO>> getPools(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Pool> pools = poolService.findAll(PageRequest.of(page, size));
        List<PoolDTO> poolsDTO = new ArrayList<>();

        for (Pool pool : pools) {
            poolsDTO.add(new PoolDTO(pool));
        }

        if (poolsDTO.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(poolsDTO);
    }

    @Operation(summary = "Get a pool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pool found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PoolDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Pool not found, probably invalid id supplied", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PoolDTO> getPool(@PathVariable Long id) {
        Optional<Pool> pool = poolService.findById(id);
        if (pool.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(new PoolDTO(pool.get()));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

     // GET messages

     @Operation(summary = "Get all messages of a pool by its ID.")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Messages found", content = {
                     @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)) }),
             @ApiResponse(responseCode = "404", description = "Pool not found, probably invalid id supplied", content = @Content)
     })
     @GetMapping("/{poolId}/messages")
     public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long poolId) {
         Optional<Pool> pool = poolService.findById(poolId);
         if (!pool.isPresent()) {
             return ResponseEntity.notFound().build();
         }
     
         List<Message> messages = pool.get().getMessages();
         List<MessageDTO> messageDTOs = new ArrayList<>();
         for (Message message : messages) {
             messageDTOs.add(new MessageDTO(message));
         }
         return ResponseEntity.ok().body(messageDTOs);
     }
      
     
    // ----------------------------------------------- POST -----------------------------------------------
    @Operation(summary = "Create a new pool.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pool created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PoolDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data in poolDTO", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PoolDTO> createPool(@RequestBody PoolDTO poolDTO) {
        Pool pool = poolFromDTO(poolDTO);
        poolService.save(pool);
        URI location = ServletUriComponentsBuilder.fromHttpUrl("https://localhost:8443")
            .path("/api/pools/{id}")
            .buildAndExpand(pool.getId())
            .toUri();
        PoolDTO returnPoolDTO = new PoolDTO(pool);
        return ResponseEntity.created(location).body(returnPoolDTO);
    }

     // POST messages
    @Operation(summary = "Add a new message to a pool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message added", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data in messageDTO", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pool not found, probably invalid id supplied", content = @Content)
    })
    @PostMapping("/{poolId}/messages")
    public ResponseEntity<MessageDTO> addMessage(@PathVariable Long poolId, @RequestBody MessageDTO messageDTO) {
        Optional<Pool> poolOptional = poolService.findById(poolId);
        if (!poolOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Pool pool = poolOptional.get();
        Message message = new Message(messageDTO.getAuthor(), messageDTO.getBody());
        pool.addMessage(message);
        messageService.save(message);
        poolService.save(pool);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(message.getId())
                .toUri();

        return ResponseEntity.created(location).body(new MessageDTO(message));
    }

    // ----------------------------------------------- PUT -----------------------------------------------
    @Operation(summary = "Edit a pool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pool edited", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PoolDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data in changes", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pool not found, probably invalid id supplied", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PoolDTO> editPool(@PathVariable Long id, @RequestBody PoolDTO poolDTO, Principal principal) throws SQLException {
        Optional<Pool> poolOptional = poolService.findById(id);
        if (!poolOptional.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Pool pool = poolOptional.get();

        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // Not logged in
        String username = principal.getName();

        Optional<Employer> employerOptional = userService.findEmployerByEmail(username);
        if (!employerOptional.isPresent())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // Not an employer
        Employer employer = employerOptional.get();

        if (!employer.isAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // Not an admin

        try {
            checkPoolDTO(poolDTO);
        } catch (IllegalArgumentException exception) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        }

        updatePoolFromDTO(pool, poolDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new PoolDTO(pool));
    }

    // ----------------------------------------------- DELETE -----------------------------------------------
    @Operation(summary = "Delete a pool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pool deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pool not found, probably invalid id supplied", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePool(@PathVariable Long id) {
        Optional<Pool> poolOptional = poolService.findById(id);
        if (!poolOptional.isPresent())
            return ResponseEntity.notFound().build();

        poolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE messages
    @Operation(summary = "Delete a message from a pool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Message deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Message not found, probably invalid id supplied", content = @Content)
    })
    @DeleteMapping("/{poolId}/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long poolId, @PathVariable Long messageId) {
        Optional<Message> messageOptional = messageService.findById(messageId);
        if (!messageOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        messageService.deleteById(messageId);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------- SERVICE --------------------------------------------
    public Pool poolFromDTO(PoolDTO poolDTO) {
        LocalTime scheduleStart = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime scheduleEnd = LocalTime.parse("20:00", DateTimeFormatter.ofPattern("HH:mm"));
        if (poolDTO.getScheduleEnd() != null)
            scheduleEnd = LocalTime.parse(poolDTO.getScheduleEnd(), DateTimeFormatter.ofPattern("HH:mm"));
        if (poolDTO.getScheduleStart() != null)
            scheduleStart = LocalTime.parse(poolDTO.getScheduleStart(), DateTimeFormatter.ofPattern("HH:mm"));

        Pool pool = new Pool.Builder()
                .name(poolDTO.getName())
                .photo(poolDTO.getPhoto())
                .direction(poolDTO.getDirection())
                .capacity(poolDTO.getCapacity())
                .scheduleStart(scheduleStart)
                .scheduleEnd(scheduleEnd)
                .company(poolDTO.getCompany())
                .description(poolDTO.getDescription())
                .build();
        return pool;
    }

    public Pool updatePoolFromDTO(Pool pool, PoolDTO poolDTO) {
        LocalTime scheduleStart = null;
        LocalTime scheduleEnd = null;
        if (poolDTO.getScheduleEnd() != null)
            scheduleEnd = LocalTime.parse(poolDTO.getScheduleEnd(), DateTimeFormatter.ofPattern("HH:mm"));
        if (poolDTO.getScheduleStart() != null)
            scheduleStart = LocalTime.parse(poolDTO.getScheduleStart(), DateTimeFormatter.ofPattern("HH:mm"));

        Pool.Builder poolBuilder = new Pool.Builder();
        poolBuilder
            .name(poolDTO.getName())
            .photo(poolDTO.getPhoto())
            .direction(poolDTO.getDirection())
            .capacity(poolDTO.getCapacity())
            .scheduleStart(scheduleStart)
            .scheduleEnd(scheduleEnd)
            .company(poolDTO.getCompany())
            .description(poolDTO.getDescription());

        pool.update(poolBuilder);
        return pool;
    }

    public boolean checkPoolDTO(PoolDTO poolDTO) {
        try {
            checkHourFormat(poolDTO.getScheduleStart());
            checkHourFormat(poolDTO.getScheduleEnd());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }

        return true;
    }

    public boolean checkHourFormat(String time) {
        try {
            if (time != null)
                LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }
        return true;
    }
}