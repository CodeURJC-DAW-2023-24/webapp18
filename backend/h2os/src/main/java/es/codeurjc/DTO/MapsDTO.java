package es.codeurjc.DTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import es.codeurjc.model.Pool;
import es.codeurjc.model.Offer;

public class MapsDTO {
    private List<MapsOffer> offersDTO;
    private String center;

    public MapsDTO() {
    }

    public MapsDTO(Collection<Offer> offers, String address) {
        this.offersDTO = new ArrayList<>();
        for (Offer offer : offers)
            offersDTO.add(new MapsOffer(offer));

        if (address != null && !address.isEmpty())
            this.center = address;
    }

    public List<MapsOffer> getOffers() {
        return offersDTO;
    }
    public String getCenter(){
        return center;
    }
}

class MapsOffer {
    private Long id;
    private String address;
    private String poolName;
    private String type;
    private String salary;
    private String start;

    public MapsOffer() {
    }

    public MapsOffer(Offer offer) {
        Pool pool = offer.getPool();

        this.id = offer.getId();
        this.address = pool.getDirection();
        this.poolName = pool.getName();
        this.type = offer.getType();
        this.salary = offer.getSalary();
        this.start = offer.getStart();
    }

    public Long getId() {
        return id;
    }
    public String getAddress() {
        return address;
    }
    public String getPoolName() {
        return poolName;
    }
    public String getType() {
        return type;
    }
    public String getSalary() {
        return salary;
    }
    public String getStart() {
        return this.start;
    }
}