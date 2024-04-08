package es.codeurjc.DTO;

import es.codeurjc.model.Message;

public class MessageDTO {

    private Long id;
    private String author;
    private String body;

    public MessageDTO() {
    }

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.author = message.getAuthor();
        this.body = message.getBody();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
