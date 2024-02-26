package es.codeurjc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DataBaseInitializer {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private LifeguardRepository lifeguardRepository;
    
    @Autowired
	private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initDatabase() {

    	lifeguardRepository.save(new Lifeguard("socorrista", "1", "Socorrista inicializado", "54152452L","s1","20",passwordEncoder.encode("s1"),"624578423","Spain","Madrid","Madrid","Calle Amargura","USER"));
		employerRepository.save(new Employer("Admin", "Jorge", "ADMIN inicializado", "99150252L","admin","20",passwordEncoder.encode("admin"),"614206895","Spain","Madrid","Madrid","Calle Amargura","admin","USER", "ADMIN"));
    }
}

