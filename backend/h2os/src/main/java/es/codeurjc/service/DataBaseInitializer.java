package es.codeurjc.service;

import java.io.IOException;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Message;
import es.codeurjc.model.Offer;
import es.codeurjc.model.Pool;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.LifeguardRepository;
import es.codeurjc.repository.OfferRepository;
import es.codeurjc.repository.PoolRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DataBaseInitializer {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private LifeguardRepository lifeguardRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private PoolRepository poolRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initDatabase() throws IOException {
        initUsers();
        initPools();
        initOffers();
    }

    private void initUsers() {
        Lifeguard lifeguard = new Lifeguard(
            "socorrista",
            "1",
            "Socorrista inicializado",
            "54152452L",
            "s1",
            "20",
            passwordEncoder.encode("s1"),
            "624578423",
            "Spain",
            "Madrid",
            "Madrid",
            "Calle Amargura",
            "Título de socorrismo A1",
            "USER","LIFE");
        lifeguard.addSkill("Confianza");
        lifeguard.addSkill("Esfuerzo");

        Employer employer = new Employer("empleador",
            "1",
            "empleador inicializado",
            "56327548K",
            "e1",
            "42",
            passwordEncoder.encode("e1"),
            "123456788",
            "España",
            "Madrid",
            "Madrid",
            "Calle Parque Bujaruelo",
            "Empresaurio",
            "USER","EMP");

        Employer admin = new Employer(
            "Admin",
            "Jorge",
            "ADMIN inicializado",
            "99150252L",
            "admin",
            "20",
            passwordEncoder.encode("admin"),
            "614206895",
            "Spain",
            "Madrid",
            "Madrid",
            "Calle Amargura",
            "Marcos Friki",
            "USER", "ADMIN", "EMP");

    	lifeguardRepository.save(lifeguard);
        employerRepository.save(employer);
		employerRepository.save(admin);
    }

    public void initPools() throws IOException {
		Pool[] defaultPools = {
			new Pool.Builder()
				.name("Misco Jones")
				.direction("Calle Timanfaya (Alcorcón)")
				.capacity(10)
				.scheduleStart(LocalTime.of(12, 30))
				.scheduleEnd(LocalTime.of(21, 30))
				.company("Marcos Friki")
				.description("Una piscina chill para bajarte a jugar a las cartas")
				.build(),
			new Pool.Builder()
				.name("Piscina municipal Las Cumbres")
				.photo("static/images/las-cumbres-image.jpg")
				.direction("C/Rio Duero, 1 (Móstoles)")
				.capacity(18)
				.scheduleStart(LocalTime.of(11, 0))
				.scheduleEnd(LocalTime.of(20, 0))
				.company("Ayuntamiento de Móstoles")
				.description("La piscina municipal de Móstoles con instalaciones tanto para los más mayores como para los más pequeños")
				.build()
		};

		Message[] defaultMessages = {
			new Message("s1", "Mensaje 1.1"),
			new Message("e1", "Mensaje 1.2"),
			new Message("e1", "Mensaje 2.1"),
			new Message("s1", "Mensaje 2.2")
		};

		for (int i = 0; i < defaultMessages.length; i++) {
            Message message = defaultMessages[i];
            defaultPools[i/2].addMessage(message);
		}

		for (Pool pool : defaultPools) {
			poolRepository.save(pool);
		}
	}

    public void initOffers() {
        Pool pool = poolRepository.findById((long)1).get();
        pool.setOffersEmpty();
        Pool pool2 = poolRepository.findById((long)2).get();
        pool2.setOffersEmpty();

        Offer[] defaultOffersData = {
            new Offer.Builder()
                .pool(pool)
                .salary("1100")
                .start("01/03/2024")
                .type("Jornada completa")
                .description("Descripcion 1")
                .build(),
            new Offer.Builder()
                .pool(pool2)
                .salary("1500")
                .start("Inicio 2")
                .type("Fines de semana")
                .description("Descripción 2")
                .build(),
            new Offer.Builder()
                .pool(pool)
                .salary("1200")
                .start("Inicio 3")
                .type("Media jornada")
                .description("Descripción 3")
                .build(),
            new Offer.Builder()
                .pool(pool2)
                .salary("2000")
                .start("Inicio 4")
                .type("Correturnos")
                .description("Descripción 4")
                .build(),
            new Offer.Builder()
                .pool(pool)
                .salary("1800")
                .start("Inicio 5")
                .type("Jornada completa")
                .description("Descripción 5")
                .build()
        };

        Employer employer = employerRepository.findByMail("e1").get();
        employer.setOffersEmpty();

        for (Offer offer : defaultOffersData) {
            offer.addEmployer(employer);
            employer.addOffer(offer);

            offerRepository.save(offer);
            employerRepository.save(employer);

            Pool poolOffer = offer.getPool();
            poolOffer.addOffer(offer);
            poolRepository.save(poolOffer);
        }

        Lifeguard lifeguard = lifeguardRepository.findByMail("s1").get();
        lifeguard.setOffersEmpty();

        for (Offer offer : defaultOffersData) {
            offer.addOffered(lifeguard);
            lifeguard.addOffer(offer);

            lifeguardRepository.save(lifeguard);
            offerRepository.save(offer);
        }
    }
}

