package es.codeurjc.service;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.model.Message;
import es.codeurjc.model.Pool;
import es.codeurjc.repository.MessageRepository;
import es.codeurjc.repository.PoolRepository;
import jakarta.annotation.PostConstruct;

@Service
public class PoolService {

    @Autowired
	private PoolRepository pools;

    @Autowired
    private MessageRepository messages;
    
    @PostConstruct
	public void init() { // metemos un par de pools para tener

		
		
	}
   
   
    public Collection<Pool> findAll() {
		return pools.findAll();
	}
	
	public Page<Pool> findAll(Pageable pageable) {
		return pools.findAll(pageable);
	}

	public Optional<Pool> findById(long id) {
		return pools.findById(id);
	}

	public void save(Pool p) {

		pools.save(p);
	}

	public void deleteById(long id) {
		this.pools.deleteById(id);
	}
}
