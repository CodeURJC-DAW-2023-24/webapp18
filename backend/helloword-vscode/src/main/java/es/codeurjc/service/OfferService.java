package es.codeurjc.service;
import java.util.Collection;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.model.Offer;
import es.codeurjc.model.Pool;
import es.codeurjc.repository.DataBase;
import es.codeurjc.repository.OfferRepository;
import jakarta.annotation.PostConstruct;

@Service
public class OfferService {

	@Autowired
	private OfferRepository offers;

	@PostConstruct
	public void init() {
		Pool pool = DataBase.getPool(0);
        Offer[] defaultOffersData = {
            new Offer.Builder()
                .pool(pool)
                .salary(1100)
                .start("01/03/2024")
                .description("¡Únete a nuestro equipo como socorrista de piscina! Estamos buscando a un profesional comprometido y capacitado para garantizar la seguridad de nuestros clientes en el área de la piscina. Responsabilidades incluyen la supervisión constante, la capacidad de respuesta rápida ante emergencias y la aplicación de protocolos de salvamento. Si posees certificación de socorrista, habilidades de comunicación efectivas y un enfoque proactivo para mantener un entorno acuático seguro, ¡esperamos recibir tu solicitud!")
                .build(),
            new Offer.Builder()
                .pool(pool)
                .salary(1500)
                .start("Inicio 2")
                .description("Descripción 2")
                .build(),
            new Offer.Builder()
                .pool(pool)
                .salary(1200)
                .start("Inicio 3")
                .description("Descripción 3")
                .build(),
            new Offer.Builder()
                .pool(pool)
                .salary(2000)
                .start("Inicio 4")
                .description("Descripción 4")
                .build(),
            new Offer.Builder()
                .pool(pool)
                .salary(1800)
                .start("Inicio 5")
                .description("Descripción 5")
                .build()
        };

        for (Offer offer : defaultOffersData) {
            save(offer);
        }

        Optional<Offer> offer = findById(1);
        Pool pool2 = DataBase.getPool(1);
        if (offer.isPresent()) {
            offer.get().update(new Offer.Builder().pool(pool2));
        }
	}

	public Collection<Offer> findAll() {
		return offers.findAll();
	}
	
	public Page<Offer> findAll(Pageable pageable) {
		return offers.findAll(pageable);
	}

	public Optional<Offer> findById(long id) {
		return offers.findById(id);
	}

	public void save(Offer offer) {
		offers.save(offer);
	}

	public void deleteById(long id) {
		this.offers.deleteById(id);
	}

}