package es.codeurjc.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.model.Message;
import es.codeurjc.repository.MessageRepository;
import jakarta.annotation.PostConstruct;

@Service
public class MessageService {

	private MessageRepository messages;

	public MessageService(MessageRepository messages) {
		this.messages = messages;
	}

	@PostConstruct
	public void init() {

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

	public Page<Message> findByPoolId(Long poolId, Pageable pageable) {
		return messages.findByPoolId(poolId, pageable);
	}

	public void save(Message message) {
		messages.save(message);
	}

	public void deleteById(long id) {
		this.messages.deleteById(id);
	}

}