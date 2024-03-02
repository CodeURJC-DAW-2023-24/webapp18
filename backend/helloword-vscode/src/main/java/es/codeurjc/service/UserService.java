package es.codeurjc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;

@Service
public class UserService {

	@Autowired 
	private EmployerRepository employerRepository;

	@Autowired
	private LifeguardRepository lifeguardRepository;

	public void saveEmployer(Employer employer){
		employerRepository.save(employer);
	}

	public void saveLifeguard(Lifeguard lifeguard){
		lifeguardRepository.save(lifeguard);
	}

	public List<Lifeguard> findAllLifeguard(){
		return lifeguardRepository.findAll();
	}

	public Optional<Employer> findEmployerByEmail(String mail){
		return employerRepository.findByMail(mail);
	}

	public Optional<Lifeguard> findLifeguardByEmail(String mail){
		return lifeguardRepository.findByMail(mail);
	}
	public void deleteUserByEmail(String mail){
		Optional<Lifeguard> l = lifeguardRepository.findByMail(mail);
		if(l.isPresent()) lifeguardRepository.deleteById(l.get().getId());
		Optional<Employer> e = employerRepository.findByMail(mail);
		if(e.isPresent()) employerRepository.deleteById(e.get().getId());

	}

}
