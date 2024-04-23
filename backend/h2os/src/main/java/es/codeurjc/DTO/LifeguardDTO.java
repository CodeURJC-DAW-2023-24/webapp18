package es.codeurjc.DTO;
import java.util.List;

import es.codeurjc.model.Lifeguard;

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
    private String type;
    private String document;
    private List<String> skills;

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
        this.document = lifeguard.getDocument();
        this.skills = lifeguard.getSkills();
        this.type = "lg";
    }

    public Lifeguard toLifeguard() {
        Lifeguard lifeguard = new Lifeguard();
        lifeguard.setName(this.name);
        lifeguard.setSurname(this.surname);
        lifeguard.setDescription(this.description);
        lifeguard.setDni(this.dni);
        lifeguard.setMail(this.mail);
        lifeguard.setAge(this.age);
        lifeguard.setPass(this.pass);
        lifeguard.setPhone(this.phone);
        lifeguard.setCountry(this.country);
        lifeguard.setLocality(this.locality);
        lifeguard.setProvince(this.province);
        lifeguard.setDirection(this.direction);

        lifeguard.setDocument(this.document);
        lifeguard.setSkills(this.skills);
        // lifeguard.setPools(this.pools);
        // lifeguard.setOffers_accepted(this.offers_accepted);
        // lifeguard.setOffers(this.offers);
        return lifeguard;
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

    public String getDocument() {
        return document;
    }
    public String getType() {
        return type;
    }

    public List<String> getSkills() {
        return skills;
    }

}
