package es.codeurjc.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;



@Service
public class RepositoryUserDetailsService implements UserDetailsService {

	@Autowired
	private EmployerRepository employerRepository;

	@Autowired
	private LifeguardRepository lifeguardRepository;

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		
		List<GrantedAuthority> roles = new ArrayList<>();
		Optional<Employer> employerOptional = employerRepository.findByMail(mail);
    	if (employerOptional.isPresent()) {
        	Employer employer = employerOptional.get();
			for (String role : employer.getRoles()) {
				roles.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
        	return new org.springframework.security.core.userdetails.User(employer.getMail(),employer.getPass(),roles);
    	}

		Optional<Lifeguard> lifeguardOptional = lifeguardRepository.findByMail(mail);

    	if (lifeguardOptional.isPresent()) {			
        	Lifeguard lifeguard = lifeguardOptional.get();
			for (String role : lifeguard.getRoles()) {
				roles.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
        	return new org.springframework.security.core.userdetails.User(lifeguard.getMail(),lifeguard.getPass(),roles);
    	}
		else 		throw new UsernameNotFoundException("User not found");

	}
}
