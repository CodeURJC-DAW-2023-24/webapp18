package es.codeurjc.service;
import java.util.Collection;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.model.Message;
import es.codeurjc.repository.MessageRepository;
import jakarta.annotation.PostConstruct;

@Service
public class MessageService {

	@Autowired
	private MessageRepository messages;

	@PostConstruct
	public void init() {
		
		messages.save(new Message("Pepe", "Vendo moto"));
		messages.save(new Message("Juan", "Compro coche"));
		
	}

	public Collection<Message> findAll() {
		return messages.findAll();
	}
	
	public Page<Message> findAll(Pageable pageable) {
		return messages.findAll(pageable);
	}

	public Optional<Message> findById(long id) {
		return messages.findById(id);
	}

	public void save(Message msg) {

		messages.save(msg);
	}

	public void deleteById(long id) {
		this.messages.deleteById(id);
	}

}