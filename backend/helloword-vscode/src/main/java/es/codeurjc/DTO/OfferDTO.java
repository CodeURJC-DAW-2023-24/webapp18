package es.codeurjc.DTO;

import java.util.List;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

public class OfferDTO {

    private Long id;
    private String poolName;
    //private String poolPic;
    //private String employer;
    private String salary;
    private String start;
    private String type;
    private String description;

    public OfferDTO(Offer offer){
        this.description = offer.getDescription();
        this.type = offer.getType();
        this.id = offer.getId();
   //     this.employer = offer.getEmployer().getMail(); when george includes employer in offers
        this.poolName = offer.getPool().getName();
        this.salary = offer.getSalary();
        this.start = offer.getStart();
       // this.poolPic = offer.getPool().getPhotoUser(); ill end this when the rest is done

    } 
    public OfferDTO(){

    }
    public String getStart(){
        return this.start;
    }
    public Long getId() {
        return id;
    }

    public String getPoolName() {
        return poolName;
    }

    public String getSalary() {
        return salary;
    }


    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

}
