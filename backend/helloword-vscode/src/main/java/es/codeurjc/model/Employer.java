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

    public Employer(EmployerBuilder builder) {
        super(builder);
        this.photoCompany = builder.photoCompany;
        this.imageCompany = builder.imageCompany;
        this.position = builder.position;
    }

    // Getters
    @Override
    public String getType() {
        return "Employer";
    }

    public Blob getPhotoCompany(){
        return photoCompany;
    }

    public boolean getImageCompany(){
        return imageCompany;
    }

    public String getPosition(){
        return position;
    }

    // Método para actualizar los datos de la persona
    public void update(EmployerBuilder builder) {
        super.update(builder);
        if (builder.photoCompany != null) {
            this.photoCompany = builder.photoCompany;
        }
        if (builder.position != null) {
            this.position = builder.position;
        }
    }

    public static class EmployerBuilder extends Person.Builder {
        @Lob
        private Blob photoCompany;
        private boolean imageCompany;
        private String position;

        public EmployerBuilder photoCompany(Blob photoCompany) {
            this.photoCompany = photoCompany;
            return this;
        }

        public EmployerBuilder imageCompany(boolean imageCompany) {
            this.imageCompany = imageCompany;
            return this;
        }

        public EmployerBuilder position(String position) {
            this.position = position;
            return this;
        }

        @Override
        public Employer build() {
            return new Employer(this);
        }
    }
}