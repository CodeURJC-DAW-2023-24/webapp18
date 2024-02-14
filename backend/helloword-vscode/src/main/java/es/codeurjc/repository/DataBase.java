package es.codeurjc.repository;


import es.codeurjc.model.Message;
import es.codeurjc.model.Person;
import es.codeurjc.model.pool;

public class DataBase {
    pool p;
    public DataBase(){
        startBD();
    }
        public void startBD(){
            this.p =  new pool("Misco Jones", 10, 11, 20, "Una piscina chill para bajarte a jugar a las cartas", "No se como pasarle una foto por string","Calle Timanfaya");
            Message m = new Message("Paco", "Piscina bien");
            p.addMessage(m);
            m = new Message("Juan", "Piscina Mal");
            p.addMessage(m);
        }
        public pool getPool(int offerID){
            
            return p;
        }
        public Person getPerson(int offerID){
            return null;
        }
        public Message getMessage(int id){
            return p.messages.get(id);
        }
        public pool addMessage(Message message){
            p.addMessage(message);
            return p;
        }
        public void deleteMsg(int id){
           p.messages.remove(id);
        }

}


