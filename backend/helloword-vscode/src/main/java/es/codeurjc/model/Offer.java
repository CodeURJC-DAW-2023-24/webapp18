package es.codeurjc.model;

public class Offer {
    private String direction;
    private String business;
    private String time;
    private String start;
    private int salary;
    private String photo;
    private String description;
    private int id;
    private static int idCounter = 0;

    public Offer(String[] offer) {
        this.direction = offer[0];
        this.business = offer[1];
        this.time = offer[2];
        this.start = offer[3];
        this.salary = Integer.parseInt(offer[4]);
        this.photo = offer[5];
        this.description = offer[6];
        this.id = idCounter;
        idCounter++;
    }

    public Integer getId() {
        return id;
    }
}
