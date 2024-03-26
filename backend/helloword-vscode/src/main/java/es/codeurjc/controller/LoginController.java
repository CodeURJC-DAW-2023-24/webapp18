package es.codeurjc.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.DTO.EmployerDTO;
import es.codeurjc.DTO.LifeguardDTO;
import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;
import es.codeurjc.security.jwt.AuthResponse;
import es.codeurjc.security.jwt.LoginRequest;
import es.codeurjc.security.jwt.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import es.codeurjc.security.jwt.AuthResponse.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    private LifeguardRepository lifeguardService;

    @Autowired
    private EmployerRepository employerService;

	@Autowired
	private UserLoginService userService;

	@Operation(summary = "Login user")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Login successful",
			content = @Content(schema = @Schema(implementation = AuthResponse.class))
		)
	})
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@CookieValue(name = "accessToken", required = false) String accessToken,
			@CookieValue(name = "refreshToken", required = false) String refreshToken,
			@RequestBody LoginRequest loginRequest) {
		
		return userService.login(loginRequest, accessToken, refreshToken);
	}

	@Operation(summary = "Refresh access token")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Refresh token successful",
			content = @Content(schema = @Schema(implementation = AuthResponse.class))
		)
	})
	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(
			@CookieValue(name = "refreshToken", required = false) String refreshToken) {

		return userService.refresh(refreshToken);
	}

	@Operation(summary = "Logout user")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Logout successful",
			content = @Content(schema = @Schema(implementation = AuthResponse.class))
		)
	})
	@PostMapping("/logout")
	public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userService.logout(request, response)));
	}

	@Operation(summary = "Get user profile")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "User profile retrieved successfully",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = EmployerDTO.class)),
				@Content(mediaType = "application/json", schema = @Schema(implementation = LifeguardDTO.class))
			}
		),
		@ApiResponse(
			responseCode = "400",
			description = "Bad Request: Missing or invalid authentication"
		),
		@ApiResponse(
			responseCode = "404",
			description = "Not Found: User not found"
		)
	})
	@GetMapping("/me")
	public ResponseEntity<Object> me(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String mail = authentication.getName();
		if (mail != null){
			Optional<Employer> employer = employerService.findByMail(mail);
			Optional<Lifeguard> lifeguard = lifeguardService.findByMail(mail);
	
			if (employer.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(new EmployerDTO(employer.get()));
	
			} else if(lifeguard.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(new LifeguardDTO(lifeguard.get()));
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		}else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
}
