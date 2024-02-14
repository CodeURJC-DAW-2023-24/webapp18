package es.codeurjc.hellowordvscode;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import es.codeurjc.dataClasses.Employer;
import es.codeurjc.dataClasses.Lifeguard;

@Service
public class UserService {
    private final ConcurrentMap<Long, Lifeguard> lifeguards = new ConcurrentHashMap<>();
	private final AtomicLong nextId1 = new AtomicLong();

    private final ConcurrentMap<Long, Employer> employers = new ConcurrentHashMap<>();
	private final AtomicLong nextId2 = new AtomicLong();

    public void saveLifeguard(Lifeguard lifeguard) {

		long id = nextId1.getAndIncrement();

		lifeguard.setId(id);

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

		employer.setId(id);

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

}
