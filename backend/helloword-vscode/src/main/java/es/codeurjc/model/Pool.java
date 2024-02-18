package es.codeurjc.model;

import java.time.LocalTime;
import java.util.ArrayList;

public class Pool{
    private String name;
    private String photo;
    private String direction;
    private int capacity;
    private LocalTime scheduleStart;
    private LocalTime scheduleEnd;
    private String company;
    private String description;
    private ArrayList<Message> messages;
    private int id;
    private static int idCounter = 0;

    public Pool(Builder builder){
        this.name = builder.name;
        this.photo = builder.photo;
        this.direction = builder.direction;
        this.capacity = builder.capacity;
        this.scheduleStart = builder.scheduleStart;
        this.scheduleEnd = builder.scheduleEnd;
        this.company = builder.company;
        this.description = builder.description;
        this.messages = builder.messages;
        this.id = idCounter++;
    }

    public void addMessage(Message m){
        this.messages.add(m);
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
    public Message[] getMessages(){
        return messages.toArray(new Message[messages.size()]);
    }
    public Message getMessage(int index){
        return messages.get(index);
    }
    public int getId(){
        return this.id;
    }

    // Método para actualizar los datos de la piscina
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
        if (builder.id != null) {
            this.id = builder.id;
        }
    }

    // Clase Builder para construir objetos de tipo Pool
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
        private Integer id;

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

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Pool build() {
            if (this.messages == null) {
                this.messages = new ArrayList<>();
            }
            return new Pool(this);
        }
    }
}
