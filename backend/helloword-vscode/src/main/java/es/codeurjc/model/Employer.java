package es.codeurjc.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "employer")
public class Employer extends Person{

    @Lob
	private Blob photo;
	private boolean imageCompany;

    private String company;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
    private List<Offer> offers;

    public Employer(){}

    public Employer(String name, String surname, String description, String dni, String mail, String age, String pass, String phone, String country, String locality, String province, String direction, String company, String... roles){
        super(name, surname, description, dni, mail, age, pass, phone, country, locality, province, direction, roles);
        this.company = company;
        this.offers = new ArrayList<Offer>();
    }

    public String getType(){
        return "employer";
    }

    public void setPhotoCompany(Blob photo){
        this.photo = photo;
    }

    public Blob getPhotoCompany(){
        return photo;
    }

    public void setImageCompany(boolean imageCompany){
        this.imageCompany = imageCompany;
    }

    public boolean getImageCompany(){
        return imageCompany;
    }

    public String getCompany(){
        return company;
    }

    public void setCompany(String company){
        this.company = company;
    }

        public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

    public void addOffer(Offer offer) {
        if (this.offers == null) {
            this.offers = new ArrayList<>();
        }
        this.offers.add(offer);
    }
    public void setOffersEmpty() {
        this.offers = new ArrayList<Offer>();
    }
}
