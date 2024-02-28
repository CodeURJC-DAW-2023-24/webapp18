package es.codeurjc.model;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "lifeguard")
public class Lifeguard extends Person{
    @Lob
	private Blob photoUser;
	private boolean imageUser;
    private String document;
    private List<String> skills;
    @ManyToMany
    private List<Pool> pools;

    @OneToMany(mappedBy = "lifeguard", cascade = CascadeType.ALL)
    private List<Offer> offers;

    public Lifeguard(){
        
    }

    public Lifeguard(String name, String surname, String description, String dni, String mail, String age,String pass, String phone, String country, String locality, String province, String direction, String document, String... roles){
        super(name, surname, description, dni, mail, age, pass, phone, country, locality, province, direction, roles);
        this.document = document;
        this.skills = new ArrayList<String>();
        this.pools = new ArrayList<Pool>();
        this.offers = new ArrayList<Offer>();
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }


    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public void addSkill(String skill) {
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        this.skills.add(skill);
    }

    public List<Pool> getPools() {
        return pools;
    }

    public void setPools(ArrayList<Pool> pools) {
        this.pools = pools;
    }

    public void addPool(Pool pool) {
        if (this.pools == null) {
            this.pools = new ArrayList<>();
        }
        this.pools.add(pool);
    }
    public void addOffer(Offer offer) {
        if (this.offers == null) {
            this.offers = new ArrayList<>();
        }
        this.offers.add(offer);
    }

	public Blob getPhotoUser() {
		return photoUser;
	}

	public void setPhotoUser(Blob photoUser) {
		this.photoUser = photoUser;
	}

    public boolean getImageUser(){
		return this.imageUser;
	}

	public void setImageUser(boolean imageUser){
		this.imageUser = imageUser;
	}
}