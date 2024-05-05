package es.codeurjc.DTO;

import es.codeurjc.model.Offer;

public class OfferDTO {

    private Long id;
    private Long poolID;
    private String poolName;
    private String direction;
    private String salary;
    private String type;
    private String start;
    private String description;
    private String employer;
    // private String poolPic;

    public OfferDTO(Offer offer){
        this.id = offer.getId();
        this.poolID = offer.getPool().getId();
        this.poolName = offer.getPool().getName();
        this.direction = offer.getPool().getDirection();
        this.salary = offer.getSalary();
        this.type = offer.getType();
        this.start = offer.getStart();
        this.description = offer.getDescription();
        this.employer = offer.getEmployer().getMail();
        // this.poolPic = offer.getPool().getPhotoUser(); ill end this when the rest is done
    }

    public OfferDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public Long getPoolID() {
        return this.poolID;
    }

    public String getPoolName() {
        return this.poolName;
    }

    public String getDirection() {
        return this.direction;
    }

    public String getSalary() {
        return this.salary;
    }

    public String getType() {
        return this.type;
    }

    public String getStart() {
        return this.start;
    }

    public String getDescription() {
        return this.description;
    }

    public String getEmployer() {
        return this.employer;
    }
}
