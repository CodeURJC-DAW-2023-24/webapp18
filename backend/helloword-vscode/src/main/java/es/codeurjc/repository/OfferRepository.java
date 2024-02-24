package es.codeurjc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.model.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {

}