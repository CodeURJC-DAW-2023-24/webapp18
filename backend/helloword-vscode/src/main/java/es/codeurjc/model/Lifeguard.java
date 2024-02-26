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
    private ArrayList<String> skills;

    public Lifeguard(){}

    public Lifeguard(LifeguardBuilder builder){
        super(builder);
        this.photoUser = builder.photoUser;
        this.imageUser = builder.imageUser;
        this.skills = new ArrayList<>();
    }

    public void addSkill(String skill) {
        this.skills.add(skill);
    }

    @Override
    public String getType() {
        return "Lifeguard";
    }

    /*public MultipartFile getDocument() {
        return document;
    }

    public void setDocument(MultipartFile document) {
        this.document = document;
    }
*/
    public Blob getPhotoUser(){
        return photoUser;
    }

    public boolean getImageUser(){
		return this.imageUser;
	}

    public ArrayList<String> getSkills() {
        return skills;
    }

    // Método para actualizar los datos de la persona
    public void update(LifeguardBuilder builder) {
        super.update(builder);
        if (builder.photoUser != null) {
            this.photoUser = builder.photoUser;
        }
    }

    public static class LifeguardBuilder extends Person.Builder {
        private Blob photoUser;
        private boolean imageUser;

        public LifeguardBuilder photoUser(Blob photoUser) {
            this.photoUser = photoUser;
            return this;
        }

        public LifeguardBuilder imageUser(boolean imageUser) {
            this.imageUser = imageUser;
            return this;
        }

        @Override
        public Lifeguard build() {
            return new Lifeguard(this);
        }
    }
}