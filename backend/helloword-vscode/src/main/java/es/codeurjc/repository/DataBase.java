package es.codeurjc.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.codeurjc.model.Message;
import es.codeurjc.model.Offer;
import es.codeurjc.model.Person;
import es.codeurjc.model.Pool;

public class DataBase {
    private Pool p;
    private static Map<Integer, Offer> offers = new HashMap<>();;
    private static int loadedUntil = 0;

    public DataBase() {
        startBD();
        initializeDefaultOffers();
    }

    private void startBD() {
        this.p = new Pool("Misco Jones", 10, 11, 20, "Una piscina chill para bajarte a jugar a las cartas", "default-image.jpg", "Calle Timanfaya");
        Message m = new Message("Paco", "Piscina bien");
        p.addMessage(m);
        m = new Message("Juan", "Piscina Mal");
        p.addMessage(m);
    }

    private void initializeDefaultOffers() {
        String[][] defaultOffersData = {
                { "Dirección 1", "Empresa 1", "Tiempo 1", "Inicio 1", "1000", "/images/default-image.jpg", "Descripción 1" },
                { "Dirección 2", "Empresa 2", "Tiempo 2", "Inicio 2", "1500", "/images/default-image.jpg", "Descripción 2" },
                { "Dirección 3", "Empresa 3", "Tiempo 3", "Inicio 3", "1200", "/images/default-image.jpg", "Descripción 3" },
                { "Dirección 4", "Empresa 4", "Tiempo 4", "Inicio 4", "2000", "/images/default-image.jpg", "Descripción 4" },
                { "Dirección 5", "Empresa 5", "Tiempo 5", "Inicio 5", "1800", "/images/default-image.jpg", "Descripción 5" }
        };

        for (int i = 0; i < defaultOffersData.length; i++) {
            Offer offer = new Offer(defaultOffersData[i]);
            DataBase.offers.put(offer.getId(), offer);
        }
    }

    public static Offer getOffer(int id) {
        return offers.get(id);
    }

    public static Offer[] getOffers(int from, int amount) {
        Collection<Offer> values = offers.values();
        int to = from + amount;

        if (to > values.size()) {
            to = values.size();
        }
        loadedUntil = to;

        List<Offer> sublist = new ArrayList<>(values).subList(from, to);

        return sublist.toArray(new Offer[0]); // la convierto en array para poder aplicar moustache
    }

    public static boolean allOffersLoaded() {
        return (loadedUntil == offers.size());
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