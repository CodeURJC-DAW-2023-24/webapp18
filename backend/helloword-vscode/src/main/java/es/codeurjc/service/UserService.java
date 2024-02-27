package es.codeurjc.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

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
/*    private final ConcurrentMap<Long, Lifeguard> lifeguards = new ConcurrentHashMap<>();
	private final AtomicLong nextId1 = new AtomicLong();

    private final ConcurrentMap<Long, Employer> employers = new ConcurrentHashMap<>();
	private final AtomicLong nextId2 = new AtomicLong();

    public void saveLifeguard(Lifeguard lifeguard) {

		long id = nextId1.getAndIncrement();

		//lifeguard.setId(id);

		this.lifeguards.put(id, lifeguard);
	}

    public Collection<Lifeguard> findAllLifeguards() {
		return lifeguards.values();
	}

	public Lifeguard findLifeguardById(long id) {
		return lifeguards.get(id);
	}

    public void deleteLifeguardById(long id) {
		this.lifeguards.remove(id);
	}

    public void saveEmployer(Employer employer) {

		long id = nextId2.getAndIncrement();

		//employer.setId(id);

		this.employers.put(id, employer);
	}

    public Collection<Employer> findAllEmployers() {
		return employers.values();
	}

	public Employer findEmployerById(long id) {
		return employers.get(id);
	}

    public void deleteEmployerById(long id) {
		this.employers.remove(id);
	}
*/

}
