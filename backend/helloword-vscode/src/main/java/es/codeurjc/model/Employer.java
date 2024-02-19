package es.codeurjc.model;

public class Employer extends Person{

    private String photoCompany;
    private String position;

    public Employer(Builder builder) {
        super(builder);
        this.photoCompany = builder.photoCompany;
        this.position = builder.position;
    }

    // Getters
    @Override
    public String getType() {
        return "Employer";
    }

    public String getPhotoCompany(){
        return photoCompany;
    }

    public String getPosition(){
        return position;
    }

    // Método para actualizar los datos de la persona
    public void update(Builder builder) {
        super.update(builder);
        if (builder.photoCompany != null) {
            this.photoCompany = builder.photoCompany;
        }
        if (builder.position != null) {
            this.position = builder.position;
        }
    }

    public static class Builder extends Person.Builder {
        private String photoCompany;
        private String position;

        public Builder photoCompany(String photoCompany) {
            this.photoCompany = photoCompany;
            return this;
        }

        public Builder position(String position) {
            this.position = position;
            return this;
        }

        @Override
        public Employer build() {
            return new Employer(this);
        }
    }
}
