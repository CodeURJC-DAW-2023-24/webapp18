package es.codeurjc.model;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "lifeguard")
public class Lifeguard extends Person{
    @Lob
	private Blob photoUser;
	private boolean imageUser;
    private String document;
    private List<String> skills;

    public Lifeguard(){
        
    }

    public Lifeguard(String name, String surname, String description, String dni, String mail, String age,String pass, String phone, String country, String locality, String province, String street){
        super(name, surname, description, dni, mail, age, pass, phone, country, locality, province, street);
        this.skills = new ArrayList<>();
    }

    /*public MultipartFile getDocument() {
        return document;
    }

    public void setDocument(MultipartFile document) {
        this.document = document;
    }
*/

       public List<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public void addSkill(String skill) {
        this.skills.add(skill);
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