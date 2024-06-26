package es.codeurjc.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.model.Pool;
import es.codeurjc.repository.PoolRepository;

@Service
public class PoolService {

	@Autowired
	private PoolRepository pools;


	public  Collection<Pool> findAll() {
		return pools.findAll();
	}

	public Page<Pool> findAll(Pageable pageable) {
		return pools.findAll(pageable);
	}

	public Optional<Pool> findById(long id) {
		return pools.findById(id);
	}

	public void save(Pool pool) {
		pools.save(pool);
	}

	public void deleteById(long id) {
		this.pools.deleteById(id);
	}

}