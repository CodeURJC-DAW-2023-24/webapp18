package es.codeurjc.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "offers")
public class Offer {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Pool pool;
    
    @ManyToOne
    private Lifeguard lifeguard;
    
    @ManyToMany
    private List<Lifeguard> lifeguards;


    private int salary;
    private String start;
    private String description;

    public Offer(){}

    private Offer(Builder builder) {
        this.pool = builder.pool;
        this.salary = builder.salary;
        this.start = builder.start;
        this.description = builder.description;
        this.lifeguards = new ArrayList<>();
    }
    
    // Getters
    public Long getId() {
        return id;
    }

    public Pool getPool() {
        return pool;
    }

    public int getSalary() {
        return salary;
    }

    public String getStart() {
        return start;
    }

    public void addOffered(Lifeguard lifeguard) {
        this.lifeguards.add(lifeguard);
    }

    public String getDescription() {
        return description;
    }
    public List<Lifeguard> getLifeguards(){
        return this.lifeguards;
    }

    public boolean isOffered(String mail){
        for (Lifeguard lifeguard : lifeguards) {
            if (lifeguard.getMail().equals(mail)) return true;
        }
        return false;
    }

    public void setLifeguard(Lifeguard l){
        this.lifeguard = l;
    }
    // Método para actualizar los datos de la oferta
    public void update(Builder builder) {
        if (builder.pool != null) {
            this.pool = builder.pool;
        }
        if (builder.salary != null) {
            this.salary = builder.salary;
        }
        if (builder.start != null) {
            this.start = builder.start;
        }
        if (builder.description != null) {
            this.description = builder.description;
        }
    }

    // Clase Builder para construir objetos de tipo Offer
    public static class Builder {
        private Pool pool;
        private Integer salary;
        private String start;
        private String description;

        public Builder pool (Pool pool) {
            this.pool = pool;
            return this;
        }

        public Builder salary(int salary) {
            this.salary = salary;
            return this;
        }

        public Builder start(String start) {
            this.start = start;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Offer build() {
            return new Offer(this);
        }
    }

}
