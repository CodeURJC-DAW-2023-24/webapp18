package es.codeurjc.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.model.Offer;
import es.codeurjc.model.Pool;
import es.codeurjc.repository.OfferRepository;
import jakarta.annotation.PostConstruct;

@Service
public class OfferService {

    private OfferRepository offers;

    @Autowired
    public OfferService(OfferRepository offers) {
        this.offers = offers;
    }

    @PostConstruct
    public void init() {
        Collection<Pool> defaultPools = PoolService.findAll();

        Offer[] defaultOffersData = {
            new Offer.Builder()
                .pool(defaultPools.iterator().next())
                .salary(1100)
                .start("01/03/2024")
                .description("Descripcion 1")
                .build(),
            new Offer.Builder()
                .pool(defaultPools.iterator().next())
                .salary(1500)
                .start("Inicio 2")
                .description("Descripción 2")
                .build(),
            new Offer.Builder()
                .pool(defaultPools.iterator().next())
                .salary(1200)
                .start("Inicio 3")
                .description("Descripción 3")
                .build(),
            new Offer.Builder()
                .pool(defaultPools.iterator().next())
                .salary(2000)
                .start("Inicio 4")
                .description("Descripción 4")
                .build(),
            new Offer.Builder()
                .pool(defaultPools.iterator().next())
                .salary(1800)
                .start("Inicio 5")
                .description("Descripción 5")
                .build()
        };

        for (Offer offer : defaultOffersData) {
            save(offer);
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