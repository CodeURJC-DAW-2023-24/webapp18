package es.codeurjc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
    public RepositoryUserDetailsService userDetailService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.authorizeHttpRequests(authorize -> authorize
					// PUBLIC PAGES
					.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
					.requestMatchers("/").permitAll()
					.requestMatchers("/index").permitAll()
					.requestMatchers("/login").permitAll()
					.requestMatchers("/pool").permitAll()
					.requestMatchers("/loadOffers").permitAll()
					.requestMatchers("/allOffersLoaded").permitAll()
					.requestMatchers("/loginerror").permitAll()
					.requestMatchers("/error").permitAll()
					.requestMatchers("/user/form").permitAll()
					.requestMatchers("/message").permitAll()
					.requestMatchers("/pool/message/add").permitAll()
					.requestMatchers("/profile").permitAll()
					.requestMatchers("/pool/message/load").permitAll()
					.requestMatchers("/pool/message/new").permitAll()
					.requestMatchers("/user/register").permitAll()
					.requestMatchers("/user/register").permitAll()
					.requestMatchers("/availableMail").permitAll()
					.requestMatchers("/maps").permitAll()
					.requestMatchers("/offer").permitAll()

					// PRIVATE PAGES
					.anyRequest().authenticated()
					
			)
			.formLogin(formLogin -> formLogin
					.loginPage("/login")
					.failureUrl("/loginerror")
					.defaultSuccessUrl("/")
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			);

		return http.build();
	}

}