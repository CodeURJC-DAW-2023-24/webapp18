package es.codeurjc.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.model.Pool;


public interface PoolRepository extends JpaRepository<Pool, Long> {

}