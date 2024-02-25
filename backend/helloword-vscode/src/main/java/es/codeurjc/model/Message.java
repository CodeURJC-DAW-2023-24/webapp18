package es.codeurjc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Message {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    //@ManyToOne Aqui el mensaje debera apuntar a la person asociada al autor
    String author;
    
    String body;

    public Message(){
    }

    public Message(String a, String b){
        super();
        author = a;
        body = b;
    }
    public long getID(){
        return this.id;
    }
}