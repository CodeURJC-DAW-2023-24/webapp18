package es.codeurjc.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
    private Employer employer;
    
    @ManyToOne
    private Lifeguard lifeguard;
    
    @ManyToMany
    private List<Lifeguard> lifeguards;


    private String salary;
    private String start;
    private String type;
    private String description;
    private Boolean acceptedByProfileUser;

    public Offer(){}

    private Offer(Builder builder) {
        this.pool = builder.pool;
        this.salary = builder.salary;
        this.start = builder.start;
        this.type = builder.type;
        this.description = builder.description;
        this.acceptedByProfileUser = false;
        this.lifeguards = new ArrayList<>();
    }

    public boolean isAccepted() {
        return this.lifeguard != null;
    }

    public boolean isAcceptedByProfileUser() {
        return this.acceptedByProfileUser;
    }

    public void setAcceptedByProfileUser(Boolean b) {
        this.acceptedByProfileUser = b;
    }

    public void resetLifeguards() {
        this.lifeguards = new ArrayList<>();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Pool getPool() {
        return pool;
    }

    public String getSalary() {
        return salary;
    }

    public String getStart() {
        return start;
    }

    public String getType() {
        return type;
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

    public Lifeguard getLifeguard(){
        return lifeguard;
    }
    public Employer getEmployer(){
        return this.employer;
    }
    public void addEmployer(Employer e){
        this.employer = e;
    }

    public void deleteOffered(Lifeguard l){
        this.lifeguards.remove(l);
    }

    // Method to update offer data
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
        if (builder.type != null) {
            this.type = builder.type;
        }
        if (builder.description != null) {
            this.description = builder.description;
        }
    }

    // Builder class to build objects of type Offer
    public static class Builder {
        private Pool pool;
        private String salary;
        private String start;
        private String type;
        private String description;

        public Builder pool (Pool pool) {
            this.pool = pool;
            return this;
        }

        public Builder salary(String salary) {
            this.salary = salary;
            return this;
        }

        public Builder start(String start) {
            this.start = start;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
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
