package es.codeurjc.DTO;

import es.codeurjc.model.Message;

public class MessageDTO {

    private Long id;
    private String author;
    private String body;
    private boolean owner;

    public MessageDTO() {
    }

    public MessageDTO(Message mesage) {
        this.id = id;
        this.author = author;
        this.body = body;
        this.owner = owner;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    
}
