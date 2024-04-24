package es.codeurjc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalTime;

import org.hibernate.engine.jdbc.BlobProxy;
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
    private void initDatabase() throws FileNotFoundException {
        Lifeguard l = new Lifeguard(
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
        l.addSkill("Confianza");
        l.addSkill("Esfuerzo");
        Employer e = new Employer("empleador",
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
    	lifeguardRepository.save(l);
        employerRepository.save(e);
		employerRepository.save(new Employer(
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
            "USER", "ADMIN", "EMP")); 
            System.out.println("USUARIOS INICIALIZADOS CORRECTAMENTE");
        init2();
        init3();
       
  
        }

        public void init2() throws FileNotFoundException {
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
			new Message("s1", "Mensaje 1.1"),
			new Message("e1", "Mensaje 1.2"),
			new Message("e1", "Mensaje 2.1"),
			new Message("s1", "Mensaje 2.2")
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
            String route = "./backend/h2os/src/main/resources/static/images/default-image.jpg";
            System.out.println("PRAPARANDO LA CARGA DE IMAGENES POR DEFECTO");
            File file = new File(route);


            FileInputStream fis = new FileInputStream(file);
            System.out.println("FICHERO ENCONTRADO");
            pool.setDefaultPhoto(BlobProxy.generateProxy(fis, file.length()));
			poolRepository.save(pool);
		}
	}


        public void init3() {
            Pool pool = poolRepository.findById((long)1).get();
            pool.setOffersEmpty();
            Employer employer = employerRepository.findByMail("e1").get();
            employer.setOffersEmpty();
        Offer[] defaultOffersData = {
            new Offer.Builder()
                .pool(pool)
                .salary("1100")
                .start("01/03/2024")
                .type("Jornada completa")
                .description("Descripcion 1")
                .build(),
            new Offer.Builder()
                .pool(pool)
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
                .pool(pool)
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
        for (Offer offer : defaultOffersData) {
            offer.addEmployer(employer);
            employer.addOffer(offer);
            System.out.println(employer.getOffers().size());
            pool.addOffer(offer);
            offerRepository.save(offer);
            poolRepository.save(pool);
            employerRepository.save(employer);
        }
    }
}

