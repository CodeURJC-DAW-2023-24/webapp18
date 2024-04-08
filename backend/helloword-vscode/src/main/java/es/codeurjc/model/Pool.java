package es.codeurjc.model;

import java.sql.Blob;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pools")
public class Pool{
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL)
    private List<Offer> offers;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL)
    private List<Message> messages;
    public boolean photoCheck;
    private String name;
    private String photo;
    private Blob photoA;
    private String direction;
    private int capacity;
    private LocalTime scheduleStart;
    private LocalTime scheduleEnd;
    private String company;
    private String description;

    public Pool(){}

    public Pool(Builder builder){
        super();
        this.name = builder.name;
        this.photo = builder.photo;
        this.direction = builder.direction;
        this.capacity = builder.capacity;
        this.scheduleStart = builder.scheduleStart;
        this.scheduleEnd = builder.scheduleEnd;
        this.company = builder.company;
        this.description = builder.description;
        this.messages = builder.messages;
        photoCheck=false;
        this.offers = new ArrayList<>();
    }

    public void addMessage(Message message){
        this.messages.add(message);
        message.setPool(this);
    }

    public void deleteMessage(int index){
        this.messages.remove(index);
    }

    // Getters
    public String getName(){
        return this.name;
    }
    public String getPhoto(){
        return this.photo;
    }
    public String getDirection(){
        return this.direction;
    }
    public int getCapacity(){
        return this.capacity;
    }
    public LocalTime getScheduleStart(){
        return this.scheduleStart;
    }
    public LocalTime getScheduleEnd(){
        return this.scheduleEnd;
    }
    public String getCompany(){
        return this.company;
    }
    public String getDescription(){
        return this.description;
    }
    public List<Message> getMessages(){
        return this.messages;
    }
    public Message getMessage(int index){
        return messages.get(index);
    }
    public long getId(){
        return this.id;
    }
    public void setPhotoUser(Blob photo) {
		this.photoA = photo;
	}
    public Blob getPhotoUser() {
		return photoA;
	}

    public void setName(String name){
        this.name = name;
    }
    public void setDirection(String d){
        this.direction = d;
    }
    public void setDescription(String d){
        this.description = d;
    }
    public void setCapacity(int c){
        this.capacity = c;
    }
    public void setStart(LocalTime l){
        this.scheduleStart = l;
    }
    public void setEnd(LocalTime l){
        this.scheduleEnd = l;
    }
    public void setCompany(String c){
        this.company = c;
    }
    public void addOffer(Offer o){
        this.offers.add(o);
    }
    // Method to update pool data
    public void update(Builder builder) {
        if (builder.name != null) {
            this.name = builder.name;
        }
        if (builder.photo != null) {
            this.photo = builder.photo;
        }
        if (builder.direction != null) {
            this.direction = builder.direction;
        }
        if (builder.capacity != null) {
            this.capacity = builder.capacity;
        }
        if (builder.scheduleStart != null) {
            this.scheduleStart = builder.scheduleStart;
        }
        if (builder.scheduleEnd != null) {
            this.scheduleEnd = builder.scheduleEnd;
        }
        if (builder.company != null) {
            this.company = builder.company;
        }
        if (builder.description != null) {
            this.description = builder.description;
        }
        if (builder.messages != null) {
            this.messages = builder.messages;
        }

    }

    // Builder class to build Pool type objects
    public static class Builder {
        private String name;
        private String photo;
        private String direction;
        private Integer capacity;
        private LocalTime scheduleStart;
        private LocalTime scheduleEnd;
        private String company;
        private String description;
        private ArrayList<Message> messages;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder photo(String photo) {
            this.photo = photo;
            return this;
        }

        public Builder direction(String direction) {
            this.direction = direction;
            return this;
        }

        public Builder capacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder scheduleStart(LocalTime scheduleStart) {
            this.scheduleStart = scheduleStart;
            return this;
        }

        public Builder scheduleEnd(LocalTime scheduleEnd) {
            this.scheduleEnd = scheduleEnd;
            return this;
        }

        public Builder company(String company) {
            this.company = company;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder messages(ArrayList<Message> messages) {
            this.messages = messages;
            return this;
        }

        public Pool build() {
            if (this.messages == null) {
                this.messages = new ArrayList<>();
            }
            return new Pool(this);
        }
    }

    public void setPhoto(String string) {
        this.photo=string;
    }
    public void setOffersEmpty() {
        this.offers = new ArrayList<Offer>();
    }
}