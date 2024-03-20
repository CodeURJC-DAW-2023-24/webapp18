package es.codeurjc.DTO;
import java.sql.Blob;
import java.util.List;

import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.model.Pool;


public class LifeguardDTO{
    private Long id;
    private String name;
    private String surname;
    private String description;
    private String dni;
    private String mail;
    private String age;
    private String pass;
    private String phone;
    private String country;
    private String locality;
    private String province;
    private String direction;

    private Blob photo;
	private boolean imageUser;
    private String document;
    private List<String> skills;
    private List<Pool> pools;
    private List<Offer> offers_accepted;
    private List<Offer> offers;
    private Boolean offerAssigned;

    public LifeguardDTO(){

    }
    public LifeguardDTO(Lifeguard lifeguard){
        super();
        this.id = lifeguard.getId();
        this.name = lifeguard.getName();
        this.surname = lifeguard.getSurname();
        this.description = lifeguard.getDescription();
        this.dni = lifeguard.getDni();
        this.mail = lifeguard.getMail();
        this.age = lifeguard.getAge();
        this.pass = lifeguard.getPass();
        this.phone = lifeguard.getPhone();
        this.country = lifeguard.getCountry();
        this.locality = lifeguard.getLocality();
        this.province = lifeguard.getProvince();
        this.direction = lifeguard.getDirection();
        this.photo = lifeguard.getPhotoUser();
        this.imageUser = lifeguard.getImageUser();
        this.document = lifeguard.getDocument();
        this.skills = lifeguard.getSkills();
        this.pools = lifeguard.getPools();
        this.offers_accepted = lifeguard.getOffersAccepted();
        this.offers = lifeguard.getOffers();
        this.offerAssigned = lifeguard.getofferAssigned();
    }

     // Getters
     public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getDni() {
        return dni;
    }
    
    public String getMail() {
        return mail;
    }
    
    public String getAge() {
        return age;
    }
    
    public String getPass() {
        return pass;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getCountry() {
        return country;
    }
    
    public String getLocality() {
        return locality;
    }
    
    public String getProvince() {
        return province;
    }
    
    public String getDirection() {
        return direction;
    }

    public Blob getPhoto() {
        return photo;
    }

    public boolean isImageUser() {
        return imageUser;
    }

    public String getDocument() {
        return document;
    }

    public List<String> getSkills() {
        return skills;
    }

    public List<Pool> getPools() {
        return pools;
    }

    public List<Offer> getOffers_accepted() {
        return offers_accepted;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public Boolean isOfferAssigned() {
        return offerAssigned;
    }
}
