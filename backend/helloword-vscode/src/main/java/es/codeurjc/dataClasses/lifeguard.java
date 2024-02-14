package es.codeurjc.dataClasses;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class Lifeguard extends Person{
    private String photoUser;
    private MultipartFile document;
    private List<String> skills;

    public Lifeguard(String name, String surname, String description, String dni, String mail, String pass, int phone, String country, String locality, String province, String street, String photoUser, MultipartFile document){
        super(name, surname, description, dni, mail, pass, phone, country, locality, province, street);
        this.photoUser = photoUser;
        this.document = document;
    }

    public MultipartFile getDocument() {
        return document;
    }

    public void setDocument(MultipartFile document) {
        this.document = document;
    }

    public String photoUser(){
        return photoUser;
    }

    public void setPhotoUser(String photoUser){
        this.photoUser = photoUser;
    }

       public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
