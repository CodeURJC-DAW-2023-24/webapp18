package es.codeurjc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @ManyToOne
    private Pool pool;

    private String author;
    private String body;
    private boolean hasOwner;

    public Message(){
    }

    public Message(String author, String body){
        this.author = author;
        this.body = body;
    }

    public long getId(){
        return this.id;
    }

    public Pool getPool(){
        return this.pool;
    }

    public String getAuthor(){
        return this.author;
    }

    public String getBody(){
        return this.body;
    }

    public boolean getOwner(){
        return hasOwner;
    }

    public void setPool(Pool pool){
        this.pool = pool;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setBody(String body){
        this.body = body;
    }

    public void setOwner(boolean o){
        hasOwner = o;
    }
}
