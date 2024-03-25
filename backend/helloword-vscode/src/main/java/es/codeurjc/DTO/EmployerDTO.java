package es.codeurjc.DTO;

import java.sql.Blob;
import java.util.List;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Offer;

public class EmployerDTO {
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
	private boolean imageCompany;
    private String company;

    public EmployerDTO(){

    }

    public EmployerDTO(Employer employer){
        super();
        this.id = employer.getId();
        this.name = employer.getName();
        this.surname = employer.getSurname();
        this.description = employer.getDescription();
        this.dni = employer.getDni();
        this.mail = employer.getMail();
        this.age = employer.getAge();
        this.pass = employer.getPass();
        this.phone = employer.getPhone();
        this.country = employer.getCountry();
        this.locality = employer.getLocality();
        this.province = employer.getProvince();
        this.direction = employer.getDirection();
        this.photo = employer.getPhotoCompany();
        this.imageCompany = employer.getImageCompany();
        this.company = employer.getCompany();

    }

    public Employer toEmployer() {
        Employer employer = new Employer();
        employer.setName(this.name);
        employer.setSurname(this.surname);
        employer.setDescription(this.description);
        employer.setDni(this.dni);
        employer.setMail(this.mail);
        employer.setAge(this.age);
        employer.setPass(this.pass);
        employer.setPhone(this.phone);
        employer.setCountry(this.country);
        employer.setLocality(this.locality);
        employer.setProvince(this.province);
        employer.setDirection(this.direction);
        employer.setPhotoCompany(this.photo);
        employer.setImageCompany(this.imageCompany);
        employer.setCompany(this.company);
        return employer;
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

    public boolean isImageCompany() {
        return imageCompany;
    }

    public String getCompany() {
        return company;
    }

}
