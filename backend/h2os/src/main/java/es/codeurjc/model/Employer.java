package es.codeurjc.model;

public class Employer extends Person{

    private String photoCompany;
    private String position;

    public Employer(String name, String surname, String description, String dni, String mail, String pass, String phone, String country, String locality, String province, String street, String photoCompany, String position){
        super(name, surname, description, dni, mail, pass, phone, country, locality, province, street);
        this.photoCompany = photoCompany;
        this.position = position;
    }
    
    public void setPhotoCompany(String photoCompany){
        this.photoCompany = photoCompany;
    }
    public String getPhotoCompany(){
        return photoCompany;
    }
    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position = position;
    }
    
}
