package es.codeurjc.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import es.codeurjc.model.Lifeguard;
import es.codeurjc.model.Offer;
import es.codeurjc.model.Pool;
import es.codeurjc.repository.OfferRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class OfferService {
    private static final Set<String> offerTypes = new HashSet<>();

    static {
        offerTypes.add("Puntual");
        offerTypes.add("Media jornada");
        offerTypes.add("Jornada completa");
        offerTypes.add("Fines de semana");
        offerTypes.add("Correturnos");
    }

    @Autowired
    private OfferRepository offers;

    @Autowired
    private UserService userService;

    public Collection<Offer> findAll() {
        return offers.findAll();
    }

    public Page<Offer> findAll(Pageable pageable) {
        return offers.findAll(pageable);
    }

    public Optional<Offer> findById(long id) {
        return offers.findById(id);
    }

    public Collection<Offer> findByPoolDirection(String address) {
        return offers.findByPoolDirection(address);
    }

    public void save(Offer offer) {
        offers.save(offer);
    }

    public void deleteById(long id) {
        this.offers.deleteById(id);
    }

    public Offer createOffer(Pool pool, HttpServletRequest request) {
        Offer offer = new Offer.Builder()
                .pool(pool)
                .salary(request.getParameter("salary"))
                .start(request.getParameter("start"))
                .type(request.getParameter("type"))
                .description(request.getParameter("description"))
                .build();

        return offer;
    }

    public Offer updateOffer(Offer offer, HttpServletRequest request) {
        Offer.Builder builder = new Offer.Builder()
                .salary(request.getParameter("salary"))
                .start(request.getParameter("start"))
                .type(request.getParameter("type"))
                .description(request.getParameter("description"));

        offer.update(builder);

        return offer;
    }

    public void resetOffer(Offer offer) {
        Lifeguard lifeguard = offer.getLifeguard();
        if (lifeguard != null) {
            lifeguard.deleteOffer(offer);
            lifeguard.deleteOfferAccepted(offer);
            userService.saveLifeguard(lifeguard);
        }

        offer.resetLifeguards();
        offer.setLifeguard(null);
    }

    public Boolean checkOfferRequest(HttpServletRequest request, boolean checkAll) throws Exception {
        String pool = request.getParameter("pool-id");
        String start = request.getParameter("start");
        LocalDate startDate = LocalDate.parse(start);
        String type = request.getParameter("type");
        String description = request.getParameter("description");
        String salary = request.getParameter("salary");

        if (checkAll) {
            if (pool.equals(""))
                throw new Exception("La piscina no puede estar vacía.");
            try {
                int poolId = Integer.parseInt(pool);
                if (poolId == 0)
                    throw new Exception("Tienes que seleccionar una piscina.");
            } catch (NumberFormatException e) {
                throw new Exception("No has seleccionado una piscina válida.");
            }
        }

        if (type.equals(""))
            throw new Exception("El tipo de contrato no puede estar vacío.");
        if (start.equals(""))
            throw new Exception("La fecha de inicio no puede estar vacía.");
        if (salary.equals(""))
            throw new Exception("El salario no puede estar vacío.");
        if (description.length() > 255)
            throw new Exception("La descripción no puede superar los 255 caracteres.");

        if (!OfferService.offerTypes.contains(type))
            throw new Exception("El tipo de contrato no es válido.");

        if (startDate.isBefore(LocalDate.now()))
            throw new Exception("La fecha de inicio no puede ser anterior a la fecha actual.");

        try {
            int salaryPrice = Integer.parseInt(salary);
            if (salaryPrice < 0 && salaryPrice > 10000)
                throw new Exception(salaryPrice + "€ no es un salario válido.");
            if (salaryPrice < 1100)
                throw new Exception("El salario debe ser superior al sueldo mínimo (1200€): " + salaryPrice + "€.");
        } catch (NumberFormatException e) {
            throw new Exception("El salario debe ser un número.");
        }

        return true;
    }

    public String formattedDate(String originalDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = null;

        try {
            LocalDate parsedDate = LocalDate.parse(originalDate, inputFormatter);

            if (parsedDate.isBefore(LocalDate.now()))
                throw new DateTimeParseException("Fecha anterior al día de hoy", originalDate, 0);

            formattedDate = parsedDate.format(outputFormatter);
        } catch (DateTimeParseException e) {
            formattedDate = LocalDate.now().format(outputFormatter);
        }

        return formattedDate;
    }

    public String showError(Model model, String errorMessage) {
        model.addAttribute("title", "Error");
        model.addAttribute("message", errorMessage);
        model.addAttribute("back", "javascript:history.back()");
        return "feedback";
    }

}