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

    String author;

    String body;

    boolean owner;

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

    public void setPool(Pool pool){
        this.pool = pool;
    }
    public Pool getPool(){
        return this.pool;
    }
    public void setOwner(boolean o){
        owner = o;
    }
    public boolean getOwner(){
        return owner;
    }
    public String getAuthor(){
        return this.author;
    }
}
