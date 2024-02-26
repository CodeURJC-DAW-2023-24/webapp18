package es.codeurjc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.model.Employer;
import es.codeurjc.model.Lifeguard;

public interface LifeguardRepository extends JpaRepository<Lifeguard, Long> {
    Optional<Lifeguard> findByMail(String mail);
    Optional<Lifeguard> findById(Long id);
}