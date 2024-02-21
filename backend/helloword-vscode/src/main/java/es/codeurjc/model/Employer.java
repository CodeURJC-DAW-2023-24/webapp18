package es.codeurjc.model;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "employer")
public class Employer extends Person{

    @Lob
	private Blob photoCompany;
	private boolean imageCompany;

    private String position;

    public Employer(){

    }

    public Employer(String name, String surname, String description, String dni, String mail, String age, String pass, String phone, String country, String locality, String province, String street, String position){
        super(name, surname, description, dni, mail, age, pass, phone, country, locality, province, street);
        this.position = position;
    }

    public void setPhotoCompany(Blob photoCompany){
        this.photoCompany = photoCompany;
    }
    public Blob getPhotoCompany(){
        return photoCompany;
    }

    public void setImageCompany(boolean imageCompany){
        this.imageCompany = imageCompany;
    }

    public boolean getImageCompany(){
        return imageCompany;
    }

    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position = position;
    }
}
