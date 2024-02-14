package es.codeurjc.repository;

import es.codeurjc.model.Message;
import es.codeurjc.model.Person;
import es.codeurjc.model.Pool;

public class DataBase {
    private Pool p;

    public DataBase() {
        startBD();
    }

    public void startBD() {
        this.p = new Pool("Misco Jones", 10, 11, 20, "Una piscina chill para bajarte a jugar a las cartas",
                "No se como pasarle una foto por string", "Calle Timanfaya");
        Message m = new Message("Paco", "Piscina bien");
        p.addMessage(m);
        m = new Message("Juan", "Piscina Mal");
        p.addMessage(m);
    }

    public Pool getPool(int offerID) {
        return p;
    }

    public Person getPerson(int offerID) {
        return null;
    }

    public Message getMessage(int id) {
        return p.messages.get(id);
    }

    public Pool addMessage(Message message) {
        p.addMessage(message);
        return p;
    }

    public void deleteMsg(int id) {
        p.messages.remove(id);
    }
}