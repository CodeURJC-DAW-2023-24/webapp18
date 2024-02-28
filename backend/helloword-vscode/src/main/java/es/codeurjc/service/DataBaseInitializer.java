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
        Lifeguard l = new Lifeguard("socorrista", "1", "Socorrista inicializado", "54152452L","s1","20",passwordEncoder.encode("s1"),"624578423","Spain","Madrid","Madrid","Calle Amargura","Título de socorrismo A1","USER","LIFE");
        l.addSkill("Confianza");
        l.addSkill("Esfuerzo");
        Employer e = new Employer("empleador", "1", " empleador inicializado","56327548K","e1","42",passwordEncoder.encode("e1"),"123456788","España","Madrid","Madrid","Calle Parque Bujaruelo","Empresaurio","USER","EMP");
    	lifeguardRepository.save(l);
        employerRepository.save(e);
		employerRepository.save(new Employer("Admin", "Jorge", "ADMIN inicializado", "99150252L","admin","20",passwordEncoder.encode("admin"),"614206895","Spain","Madrid","Madrid","Calle Amargura","Marcos Friki","USER", "ADMIN", "EMP", "LIFE"));
    }
}

