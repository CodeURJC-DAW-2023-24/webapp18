package es.codeurjc.hellowordvscode;


import es.codeurjc.dataClasses.*;

public class DataBase {
    public DataBase(){
        startBD();
    }
        public void startBD(){
            //aqui meter offeers con pools y cosas 
        }
        public pool getPool(int offerID){
            return new pool("Misco Jones", 10, 11, 20, "Una piscina chill para bajarte a jugar a las cartas", "No se como pasarle una foto por string");
        }
        public Person getPerson(int offerID){
            return null;
        }
        public Message getMessage(int id){
            pool p  = new pool("Misco Jones", 10, 11, 20, "Una piscina chill para bajarte a jugar a las cartas", "No se como pasarle una foto por string");
            Message m = new Message("Paco", "Piscina bien");
            p.addMessage(m);
            m = new Message("Juan", "Piscina Mal");
            p.addMessage(m);
            return p.messages.get(id);
        }

}


