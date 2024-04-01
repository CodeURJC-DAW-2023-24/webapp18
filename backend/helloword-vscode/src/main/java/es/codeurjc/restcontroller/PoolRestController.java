package es.codeurjc.restcontroller;

import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.DTO.PoolDTO;
import es.codeurjc.model.Pool;
import es.codeurjc.service.PoolService;
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

    // ----------------------------------------------- GET -----------------------------------------------
    @Operation(summary = "Get all pools.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pools found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PoolDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Pools not found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PoolDTO>> getPools() {
        List<Pool> pools = poolService.findAll();
        List<PoolDTO> poolDTOs = new ArrayList<>();

        for (Pool pool : pools) {
            poolDTOs.add(new PoolDTO(pool));
        }

        if (poolDTOs.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(poolDTOs);
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

    // ----------------------------------------------- PUT -----------------------------------------------
    @Operation(summary = "Update a pool by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pool updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PoolDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data in changes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pool not found, probably invalid id supplied", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PoolDTO> updatePool(@PathVariable Long id, @RequestBody PoolDTO poolDTO) {
        Optional<Pool> poolOptional = poolService.findById(id);
        if (!poolOptional.isPresent())
            return ResponseEntity.notFound().build();
        Pool pool = poolOptional.get();

        // Actualizar la pool con los datos del DTO
        pool.setName(poolDTO.getName());
        pool.setPhoto(poolDTO.getPhoto());
        pool.setDirection(poolDTO.getDirection());
        pool.setCapacity(poolDTO.getCapacity());
        // Actualizar otros campos...

        poolService.save(pool);
        return ResponseEntity.ok().body(new PoolDTO(pool));
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
}
