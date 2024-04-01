package es.codeurjc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;



@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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

    @JsonIgnore
    private String pass;

    private String phone;
    private String country;
    private String locality;
    private String province;
    private String direction;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;


    public Person(){
        super();
    }
    public Person(String name, String surname, String description, String dni, String mail, String age, String pass, String phone, String country, String locality, String province, String direction, String... roles){
        super();
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.dni = dni;
        this.mail = mail;
        this.age = age;
        this.pass = pass;
        this.phone = phone;
        this.country = country;
        this.locality = locality;
        this.province = province;
        this.direction = direction;
        this.roles = List.of(roles);
    }

    public boolean isAdmin(){
        return hasRole("ADMIN");
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public abstract String getType();

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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

	public List<String> getRoles() {
		return roles;
	}

    public void setRoles(String... roles) {
		this.roles = List.of(roles);
	}
}