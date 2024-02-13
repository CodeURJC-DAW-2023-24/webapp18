package es.codeurjc.hellowordvscode;


import es.codeurjc.dataClasses.pool;

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

}


