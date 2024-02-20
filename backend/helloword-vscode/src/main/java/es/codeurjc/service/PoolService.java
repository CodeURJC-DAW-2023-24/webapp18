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

		Pool pool = new Pool.Builder()
                .name("Misco Jones")
                .photo("/images/default-image.jpg")
                .direction("Calle Timanfaya (Alcorcón)")
                .capacity(10)
                .scheduleStart(LocalTime.of(12, 30))
                .scheduleEnd(LocalTime.of(21, 30))
                .company("Marcos Friki")
                .description("Una piscina chill para bajarte a jugar a las cartas")
                .build();

        Message m1 = new Message("Paco", "Mensaje 1.1");
        Message m2 = new Message("Juan", "Mensaje 1.2");
        messages.save(m1);
        messages.save(m2);
        pool.addMessage(m1);
        pool.addMessage(m2);
		pools.save(pool);

        pool = new Pool.Builder()
        .name("Piscina municipal Las Cumbres")
        .photo("https://lh3.googleusercontent.com/p/AF1QipMXXW-IjqZkpx8EfwA_Nw_ALJQZfMAWdjhnT4Xh=s1360-w1360-h1020-rw")
        .direction("C/Rio Duero, 1 (Móstoles)")
        .capacity(18)
        .scheduleStart(LocalTime.of(11, 0))
        .scheduleEnd(LocalTime.of(20, 0))
        .company("Ayuntamiento de Móstoles")
        .description("La piscina municipal de Móstoles con instalaciones tanto para los más mayores como para los más pequeños")
        .build();

        Message m3 = new Message("Antonio", "Mensaje 2.1");
        Message m4 = new Message("Jose", "Mensaje 2.2");
        messages.save(m3);
        messages.save(m4);
        pool.addMessage(m3);
        pool.addMessage(m4);
		pools.save(pool);
		
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
