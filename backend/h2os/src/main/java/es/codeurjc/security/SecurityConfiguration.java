package es.codeurjc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.codeurjc.security.jwt.JwtRequestFilter;
import es.codeurjc.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public RepositoryUserDetailsService userDetailService;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

    @Bean
    @Order(1)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());

		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

		http
			.authorizeHttpRequests(authorize -> authorize
					// PRIVATE ENDPOINTS

					// PUBLIC ENDPOINTS
					.anyRequest().permitAll()
			);

		// Disable Form login Authentication
		http.formLogin(formLogin -> formLogin.disable());

		// Disable CSRF protection (it is difficult to implement in REST APIs)
		http.csrf(csrf -> csrf.disable());

		// Disable Basic Authentication
		http.httpBasic(httpBasic -> httpBasic.disable());

		// Stateless session
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	@Order(2)
	SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());

		http
			.authorizeHttpRequests(authorize -> authorize
					// PUBLIC PAGES
					.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
					.requestMatchers("/").permitAll()
					.requestMatchers("/error").permitAll()
					.requestMatchers("/message").permitAll()

					.requestMatchers("/login").permitAll()
					.requestMatchers("/loginerror").permitAll()
					.requestMatchers("/profile").permitAll()

					.requestMatchers("/user/form").permitAll()
					.requestMatchers("/user/register").permitAll()
					.requestMatchers("/user/register").permitAll()
					.requestMatchers("/availableMail").permitAll()

					.requestMatchers("/offers").permitAll()
					.requestMatchers("/offers/load").permitAll()
					.requestMatchers("/offer").permitAll()

					.requestMatchers("/pools").permitAll()
					.requestMatchers("/pools/load").permitAll()
					.requestMatchers("/pool").permitAll()
					.requestMatchers("/pool/*/image").permitAll()
					.requestMatchers("/pool/message/add").permitAll()
					.requestMatchers("/pool/message/load").permitAll()
					.requestMatchers("/pool/message/new").permitAll()

					.requestMatchers("/maps").permitAll()
					.requestMatchers("/maps/offers").permitAll()
					.requestMatchers("/piechart").permitAll()

                    // OpenAPI documentation
                    .requestMatchers("/v3/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
					
					// Frontend petitions
					.requestMatchers("/new/**").permitAll()

					// PRIVATE PAGES
					.anyRequest().authenticated()
			)
			.formLogin(formLogin -> formLogin
					.loginPage("/login")
					.failureUrl("/login/error")
					.defaultSuccessUrl("/login")
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login")
					.permitAll()
			);

		return http.build();
	}
}