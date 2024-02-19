package es.codeurjc.repository;

import java.time.LocalTime;
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
    private static Map<Integer, Pool> pools = new HashMap<>();
    private static Map<Integer, Offer> offers = new HashMap<>();
    private static Map<Integer, Person> people = new HashMap<>();
    private static int loadedUntil = 0;

    public DataBase() {
        initializeDefaultPools();
        initializeDefaultOffers();
        initializeDefaultPeople();
    }

    private void initializeDefaultPools() {
        Pool[] defaultPoolsData = {
            new Pool.Builder()
                .name("Piscina municipal Las Cumbres")
                .photo("https://lh3.googleusercontent.com/p/AF1QipMXXW-IjqZkpx8EfwA_Nw_ALJQZfMAWdjhnT4Xh=s1360-w1360-h1020-rw")
                .direction("C/Rio Duero, 1 (Móstoles)")
                .capacity(18)
                .scheduleStart(LocalTime.of(11, 0))
                .scheduleEnd(LocalTime.of(20, 0))
                .company("Ayuntamiento de Móstoles")
                .description("La piscina municipal de Móstoles con instalaciones tanto para los más mayores como para los más pequeños")
                .build(),
            new Pool.Builder()
                .name("Misco Jones")
                .photo("/images/default-image.jpg")
                .direction("Calle Timanfaya (Alcorcón)")
                .capacity(10)
                .scheduleStart(LocalTime.of(12, 30))
                .scheduleEnd(LocalTime.of(21, 30))
                .company("Marcos Friki")
                .description("Una piscina chill para bajarte a jugar a las cartas")
                .build()
        };

        for (Pool pool : defaultPoolsData) {
            pools.put(pool.getId(), pool);
        }

        Pool pool = DataBase.getPool(0);
        Message m1 = new Message("Paco", "Piscina bien");
        Message m2 = new Message("Juan", "Piscina Mal");
        pool.addMessage(m1);
        pool.addMessage(m2);
    }

    private void initializeDefaultOffers() {
        Pool pool = DataBase.getPool(0);
        Offer[] defaultOffersData = {
            new Offer.Builder(pool)
                .salary(1100)
                .start("01/03/2024")
                .description("¡Únete a nuestro equipo como socorrista de piscina! Estamos buscando a un profesional comprometido y capacitado para garantizar la seguridad de nuestros clientes en el área de la piscina. Responsabilidades incluyen la supervisión constante, la capacidad de respuesta rápida ante emergencias y la aplicación de protocolos de salvamento. Si posees certificación de socorrista, habilidades de comunicación efectivas y un enfoque proactivo para mantener un entorno acuático seguro, ¡esperamos recibir tu solicitud!")
                .build(),
            new Offer.Builder(pool)
                .salary(1500)
                .start("Inicio 2")
                .description("Descripción 2")
                .build(),
            new Offer.Builder(pool)
                .salary(1200)
                .start("Inicio 3")
                .description("Descripción 3")
                .build(),
            new Offer.Builder(pool)
                .salary(2000)
                .start("Inicio 4")
                .description("Descripción 4")
                .build(),
            new Offer.Builder(pool)
                .salary(1800)
                .start("Inicio 5")
                .description("Descripción 5")
                .build()
        };

        for (Offer offer : defaultOffersData) {
            DataBase.offers.put(offer.getId(), offer);
        }

        Offer offer = DataBase.getOffer(1);
        Pool pool2 = DataBase.getPool(1);
        offer.update(new Offer.Builder(pool2));
    }

    public void initializeDefaultPeople() {
        String description = "Descripción genérica que se puede poner en el perfil un tio que quiere ser socorrista en verano.";
        Person[] defaultPeopleData = {
            new Person.Builder("Paco", "Merla")
                .description(description)
                .dni("12345678A")
                .mail("pacomerla@gmail.com")
                .pass("1234")
                .phone(666666666)
                .country("España")
                .locality("Alcorcón")
                .province("Madrid")
                .direction("Calle de los príncipes")
                .build(),
            new Person.Builder("Juan", "Pérez")
                .description(description)
                .dni("12345678B")
                .mail("klkmanin@gmail.com")
                .pass("1234")
                .phone(666666666)
                .country("España")
                .locality("Alcorcón")
                .province("Madrid")
                .direction("Calle de los príncipes")
                .build(),
            new Person.Builder("Pedro", "García")
                .description(description)
                .dni("12345678C")
                .mail("pedro@gmail.com")
                .pass("1234")
                .phone(666666666)
                .country("España")
                .locality("Alcorcón")
                .province("Madrid")
                .direction("Calle de los príncipes")
                .build()
        };

        for (Person person : defaultPeopleData) {
            DataBase.people.put(person.getId(), person);
        }

        Person person = DataBase.getPerson(0);
        person.addSkill("Natación");
        person.addSkill("Primeros auxilios");
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

    public static Pool getPool(int id) {
        return pools.get(id);
    }

    public static Person getPerson(int id) {
        return people.get(id);
    }
}