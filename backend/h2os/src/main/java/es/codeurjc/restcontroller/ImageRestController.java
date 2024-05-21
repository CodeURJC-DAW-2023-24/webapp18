package es.codeurjc.restcontroller;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.DTO.EmployerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/images")
public class ImageRestController {

	@Operation(summary = "Get logo image.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Logo found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = EmployerDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Logo not found", content = @Content)
	})
	@GetMapping("/logo")
	public ResponseEntity<byte[]> getLogo() {
        try {
            ClassPathResource imgFile = new ClassPathResource("static/images/h2oslogo.png");
            byte[] imageBytes = Files.readAllBytes(imgFile.getFile().toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
		catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get default user image.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Image found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = EmployerDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Image not found", content = @Content)
	})
	@GetMapping("/users/default")
	public ResponseEntity<byte[]> getDefaultUserImage() {
        try {
            ClassPathResource imgFile = new ClassPathResource("static/images/noPhotoUser.jpg");
            byte[] imageBytes = Files.readAllBytes(imgFile.getFile().toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
		catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
