package es.codeurjc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Message {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    String author;
    String body;

    public Message(){
    }

    public Message(String a, String b){
        super();
        author = a;
        body = b;
    }
}