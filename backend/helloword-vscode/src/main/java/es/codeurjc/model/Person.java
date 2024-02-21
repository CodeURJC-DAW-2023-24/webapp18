package es.codeurjc.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;



@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
public abstract class Person {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
    private String street;

    public Person(){
        super();
    }
    public Person(String name, String surname, String description, String dni, String mail, String age, String pass, String phone, String country, String locality, String province, String street){
        super();
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.dni = dni;
        this.mail = mail;
        this.pass = pass;
        this.phone = phone;
        this.country = country;
        this.locality = locality;
        this.province = province;
        this.street = street;
    }

    public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAge(){
        return age;
    }

    public void setAge(String age){
        this.age = age;
    }
    
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}