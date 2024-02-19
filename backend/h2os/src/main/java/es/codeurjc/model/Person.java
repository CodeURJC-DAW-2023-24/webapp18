package es.codeurjc.model;

import java.util.ArrayList;

public abstract class Person {
    private String name;
    private String surname;
    private String description;
    private String dni;
    private String mail;
    private String pass;
    private int phone;
    private String country;
    private String locality;
    private String province;
    private String direction;
    private int id;
    private static int idCounter = 0;

    public Person(Builder builder) {
        this.name = builder.name;
        this.surname = builder.surname;
        this.description = builder.description;
        this.dni = builder.dni;
        this.mail = builder.mail;
        this.pass = builder.pass;
        this.phone = builder.phone;
        this.country = builder.country;
        this.locality = builder.locality;
        this.province = builder.province;
        this.direction = builder.direction;
        this.id = idCounter++;
    }

    // Getters
    public int getId() {
		return id;
	}

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDescription() {
        return description;
    }

    public String getDni() {
        return dni;
    }

    public String getMail() {
        return mail;
    }

    public String getPass() {
        return pass;
    }

    public int getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getLocality() {
        return locality;
    }

    public String getProvince() {
        return province;
    }

    public String getDirection() {
        return direction;
    }

    // Método para actualizar los datos de la persona
    public void update(Builder builder) {
        if (builder.name != null) {
            this.name = builder.name;
        }
        if (builder.surname != null) {
            this.surname = builder.surname;
        }
        if (builder.description != null) {
            this.description = builder.description;
        }
        if (builder.dni != null) {
            this.dni = builder.dni;
        }
        if (builder.mail != null) {
            this.mail = builder.mail;
        }
        if (builder.pass != null) {
            this.pass = builder.pass;
        }
        if (builder.phone != 0) {
            this.phone = builder.phone;
        }
        if (builder.country != null) {
            this.country = builder.country;
        }
        if (builder.locality != null) {
            this.locality = builder.locality;
        }
        if (builder.province != null) {
            this.province = builder.province;
        }
        if (builder.direction != null) {
            this.direction = builder.direction;
        }
    }

    // Clase Builder para construir objetos de tipo Person
    public static abstract class Builder {
        private String name;
        private String surname;
        private String description;
        private String dni;
        private String mail;
        private String pass;
        private int phone;
        private String country;
        private String locality;
        private String province;
        private String direction;

        public Builder(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder dni(String dni) {
            this.dni = dni;
            return this;
        }

        public Builder mail(String mail) {
            this.mail = mail;
            return this;
        }

        public Builder pass(String pass) {
            this.pass = pass;
            return this;
        }

        public Builder phone(int phone) {
            this.phone = phone;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder locality(String locality) {
            this.locality = locality;
            return this;
        }

        public Builder province(String province) {
            this.province = province;
            return this;
        }

        public Builder direction(String direction) {
            this.direction = direction;
            return this;
        }

        public abstract Person build();
    }
}
