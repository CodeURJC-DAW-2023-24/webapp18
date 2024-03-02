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
import es.codeurjc.repository.PoolRepository;
import jakarta.annotation.PostConstruct;

@Service
public class PoolService {

	private static PoolRepository pools;

	@Autowired
	public PoolService(PoolRepository pools) {
		this.pools = pools;
	}

	@PostConstruct
	public void init() {
		Pool[] defaultPools = {
			new Pool.Builder()
				.name("Misco Jones")
				.photo("/images/default-image.jpg")
				.direction("Calle Timanfaya (Alcorcón)")
				.capacity(10)
				.scheduleStart(LocalTime.of(12, 30))
				.scheduleEnd(LocalTime.of(21, 30))
				.company("Marcos Friki")
				.description("Una piscina chill para bajarte a jugar a las cartas")
				.build(),
			new Pool.Builder()
				.name("Piscina municipal Las Cumbres")
				.photo("https://lh3.googleusercontent.com/p/AF1QipMXXW-IjqZkpx8EfwA_Nw_ALJQZfMAWdjhnT4Xh=s1360-w1360-h1020-rw")
				.direction("C/Rio Duero, 1 (Móstoles)")
				.capacity(18)
				.scheduleStart(LocalTime.of(11, 0))
				.scheduleEnd(LocalTime.of(20, 0))
				.company("Ayuntamiento de Móstoles")
				.description("La piscina municipal de Móstoles con instalaciones tanto para los más mayores como para los más pequeños")
				.build()
		};

		Message[] defaultMessages = {
			new Message("Paco", "Mensaje 1.1"),
			new Message("Juan", "Mensaje 1.2"),
			new Message("Antonio", "Mensaje 2.1"),
			new Message("Jose", "Mensaje 2.2")
		};


		int i = 0;
		for (Message message : defaultMessages) {
			if (i < defaultMessages.length / 2) {
				defaultPools[0].addMessage(message);
			} else {
				defaultPools[1].addMessage(message);
			}
			i++;
		}

		for (Pool pool : defaultPools) {
			pools.save(pool);
		}
	}

	public static Collection<Pool> findAll() {
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