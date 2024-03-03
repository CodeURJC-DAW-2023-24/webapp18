package es.codeurjc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}