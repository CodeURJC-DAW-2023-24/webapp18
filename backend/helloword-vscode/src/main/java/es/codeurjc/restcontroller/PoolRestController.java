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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Operation(summary = "Get paged pools.")
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

    @Operation(summary = "Get all messages of a pool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Pool not found, probably invalid id supplied", content = @Content)
    })
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size) {

        Optional<Pool> pool = poolService.findById(id);
        if (!pool.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Message> messages = messageService.findByPoolId(id, pageable);
        List<MessageDTO> messagesDTO = new ArrayList<>();
        for (Message message : messages) {
            messagesDTO.add(new MessageDTO(message));
        }

        return ResponseEntity.status(HttpStatus.OK).body(messagesDTO);
    }

    // ----------------------------------------------- POST -----------------------------------------------
    @Operation(summary = "Create a new pool.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pool created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PoolDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data in the body", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the admin", content = @Content),
    })
    @PostMapping
    public ResponseEntity<PoolDTO> createPool(@RequestBody PoolDTO poolDTO, Principal principal) throws SQLException {
        if (!userService.isAuthorized(principal, null))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (!checkPoolDTO(poolDTO))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Pool pool = poolFromDTO(poolDTO);
        poolService.save(pool);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pool.getId())
                .toUri();

        return ResponseEntity.created(location).body(new PoolDTO(pool));
    }

    @Operation(summary = "Add a new message to a pool.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message added", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data in the body", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pool not found, probably invalid id supplied", content = @Content)
    })
    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageDTO> addMessage(@PathVariable Long id, @RequestBody MessageDTO messageDTO, Principal principal) throws SQLException {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Pool> poolOptional = poolService.findById(id);
        if (!poolOptional.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Pool pool = poolOptional.get();

        if (messageDTO.getBody() == null || messageDTO.getBody().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Message message = new Message(principal.getName(), messageDTO.getBody());
        pool.addMessage(message);
        messageService.save(message);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{messageId}")
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Pool pool = poolOptional.get();

        if (!userService.isAuthorized(principal, null))  // Pool must be an element of a employer
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            checkPoolDTO(poolDTO);
        } catch (IllegalArgumentException exception) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        }

        updatePoolFromDTO(pool, poolDTO);
        poolService.save(pool);
        return ResponseEntity.status(HttpStatus.OK).body(new PoolDTO(pool));
    }

    @Operation(summary = "Edit a message from a pool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message edited", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PoolDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data in changes", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the owner or the admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "Message not found, probably invalid id supplied", content = @Content)
    })
    @PutMapping("/{poolId}/messages/{messageId}")
    public ResponseEntity<MessageDTO> editMessage(@PathVariable Long messageId, @RequestBody MessageDTO messageDTO, Principal principal) throws SQLException {
        Optional<Message> messageOptional = messageService.findById(messageId);
        if (!messageOptional.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Message message = messageOptional.get();

        if (!userService.isAuthorized(principal, message))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        updateMessageFromDTO(message, messageDTO);
        messageService.save(message);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO(message));
    }

    // ----------------------------------------------- DELETE -----------------------------------------------
    @Operation(summary = "Delete a pool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pool deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pool not found, probably invalid id supplied", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePool(@PathVariable Long id, Principal principal) throws SQLException {
        Optional<Pool> poolOptional = poolService.findById(id);
        if (!poolOptional.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if (!userService.isAuthorized(principal, null))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        poolService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete a message from a pool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Message deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authorized, you are not the owner or the admin", content = @Content),
            @ApiResponse(responseCode = "404", description = "Message not found, probably invalid id supplied", content = @Content)
    })
    @DeleteMapping("/{poolId}/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId, Principal principal) throws SQLException {
        Optional<Message> messageOptional = messageService.findById(messageId);
        if (!messageOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Message message = messageOptional.get();

        if (!userService.isAuthorized(principal, message))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        messageService.deleteById(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

    public Message updateMessageFromDTO(Message message, MessageDTO messageDTO) {
        message.setBody(messageDTO.getBody());
        return message;
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