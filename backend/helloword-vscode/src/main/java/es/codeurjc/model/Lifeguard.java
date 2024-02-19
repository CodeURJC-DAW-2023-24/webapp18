package es.codeurjc.model;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

public class Lifeguard extends Person{
    private String photoUser;
    private String document;
    private ArrayList<String> skills;

    public Lifeguard(Builder builder){
        super(builder);
        this.photoUser = builder.photoUser;
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
    public String getPhotoUser(){
        return photoUser;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    // Método para actualizar los datos de la persona
    public void update(Builder builder) {
        super.update(builder);
        if (builder.photoUser != null) {
            this.photoUser = builder.photoUser;
        }
    }

    public static class Builder extends Person.Builder {
        private String photoUser;

        public Builder photoUser(String photoUser) {
            this.photoUser = photoUser;
            return this;
        }

        @Override
        public Lifeguard build() {
            return new Lifeguard(this);
        }
    }
}