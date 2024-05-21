package es.codeurjc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.model.Employer;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Optional<Employer> findByMail(String mail);
    Optional<Employer> findById(Long id);
}