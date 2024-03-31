package es.codeurjc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Person;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

	@Autowired 
	private EmployerRepository employerRepository;

	@Autowired
	private LifeguardRepository lifeguardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public Employer createEmployer(HttpServletRequest request) {
        Employer employer = new Employer();

        employer.setMail(request.getParameter("mail"));
        employer.setCompany(request.getParameter("company"));
        updatePerson(employer, request);

        employer.setPass(passwordEncoder.encode(request.getParameter("pass")));
        employer.setRoles("USER", "EMP");

        return employer;
    }

    public Lifeguard createLifeguard(HttpServletRequest request) {
        Lifeguard lifeguard = new Lifeguard();
        lifeguard.initSkills();
        lifeguard.setMail(request.getParameter("mail"));
        lifeguard.setDocument(request.getParameter("document"));
        updatePerson(lifeguard, request);
        updateSkills(lifeguard, request);

        lifeguard.setPass(passwordEncoder.encode(request.getParameter("pass")));
        lifeguard.setRoles("USER", "LIFE");

        return lifeguard;
    }

    public void updatePerson(Person person, HttpServletRequest request) {
        person.setName(request.getParameter("name"));
        person.setSurname(request.getParameter("surname"));
        person.setDescription(request.getParameter("description"));
        person.setDni(request.getParameter("dni"));
        person.setAge(request.getParameter("age"));
        person.setPhone(request.getParameter("phone"));
        person.setCountry(request.getParameter("country"));
        person.setLocality(request.getParameter("locality"));
        person.setProvince(request.getParameter("province"));
        person.setDirection(request.getParameter("direction"));
    }

    public void updateSkills(Lifeguard lifeguard, HttpServletRequest request) {
        Map<String, String> skills = new HashMap<>();

        skills.put("Confianza", request.getParameter("reliability"));
        skills.put("Esfuerzo", request.getParameter("effort"));
        skills.put("Comunicación", request.getParameter("communication"));
        skills.put("Actitud positiva", request.getParameter("attitude"));
        skills.put("Resolución de problemas", request.getParameter("problemsResolution"));
        skills.put("Liderazgo", request.getParameter("leadership"));

        for (Map.Entry<String, String> entry : skills.entrySet()) {
            if (entry.getValue() != null) {
                lifeguard.addSkill(entry.getKey());
            }
        }
    }

    public String checkForm(String mail, String age, String phone) {
        int phoneNum = 0;
        int ageNum = 0;
        String message1 = "";
        String message2 = "";
        String message3 = "";

        if(!phone.equals("")){
            try {
                phoneNum = Integer.parseInt(phone);
            } catch (NumberFormatException e) {
                message1 = "El teléfono debe ser un número. ";
            }
            if (phoneNum < 0) message1 = "El teléfono debe ser un número positivo. ";
            if (String.valueOf(phoneNum).length() != 9) message1 = "El teléfono debe tener 9 cifras. ";
        }
        if(!age.equals("")){
            try {
                ageNum = Integer.parseInt(age);
            } catch (NumberFormatException e) {
                message2 = "La edad debe ser un número. ";
            }
            if (ageNum < 0) message2 = "La edad debe ser un número positivo. ";
            if (ageNum % 1 != 0) message2 = "La edad debe ser un número entero. ";
        }

        Optional<Employer> employer = employerRepository.findByMail(mail);
        if (employer.isPresent()) {
            message3 = "Correo ya en uso por otro empleado. ";
        }
        Optional<Lifeguard> lifeguard = lifeguardRepository.findByMail(mail);
        if (lifeguard.isPresent()) {
            message3 = "Correo ya en uso por otro socorrista. ";
        }

        return message1 + message2 + message3;
    }
}
