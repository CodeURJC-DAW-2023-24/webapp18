package es.codeurjc.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Offer;
import es.codeurjc.model.Pool;
import es.codeurjc.repository.EmployerRepository;
import es.codeurjc.repository.OfferRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
@Service
public class OfferService {

    @Autowired
    private OfferRepository offers;
    

    public Collection<Offer> findAll() {
        return offers.findAll();
    }

    public Page<Offer> findAll(Pageable pageable) {
        return offers.findAll(pageable);
    }

    public Optional<Offer> findById(long id) {
        return offers.findById(id);
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

    public Boolean checkOffer(HttpServletRequest request) throws Exception {
        String direction = request.getParameter("direction");
        String type = request.getParameter("type");
        String start = request.getParameter("start");
        String description = request.getParameter("description");
        String salary = request.getParameter("salary");
        String pool = request.getParameter("pool-id");
        int salaryPrice;
        int poolId;

        if (direction.equals(""))
            throw new Exception("La dirección no puede estar vacía.");
        if (type.equals(""))
            throw new Exception("El tipo de contrato no puede estar vacío.");
        if (start.equals(""))
            throw new Exception("La fecha de inicio no puede estar vacía.");
        if (salary.equals(""))
            throw new Exception("El salario no puede estar vacío.");
        if (pool.equals(""))
            throw new Exception("La piscina no puede estar vacía.");

        try {
            salaryPrice = Integer.parseInt(salary);
        } catch (NumberFormatException e) {
            throw new Exception("El salario debe ser un número. ");
        }
        if (salaryPrice < 1100)
            throw new Exception("El salario debe ser superior al sueldo mínimo.");

        if (description.length() > 255)
            throw new Exception("La descripción no puede superar los 255 caracteres.");

        try {
            poolId = Integer.parseInt(pool);
        } catch (NumberFormatException e) {
            throw new Exception("No has seleccionado una piscina válida.");
        }
        if (poolId == 0)
            throw new Exception("Tienes que seleccionar una piscina");

        LocalDate startDate = LocalDate.parse(start);
        if (startDate.isBefore(LocalDate.now())) throw new Exception("La fecha de inicio no puede ser anterior a la fecha actual.");

        return true;
    }

    public String showError(Model model, String errorMessage) {
        model.addAttribute("title", "Error");
        model.addAttribute("message", errorMessage);
        model.addAttribute("back", "javascript:history.back()");
        return "message";
    }
}