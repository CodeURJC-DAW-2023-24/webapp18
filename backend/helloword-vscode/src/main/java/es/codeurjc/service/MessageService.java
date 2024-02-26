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

	private MessageRepository messages;

	@Autowired
	public MessageService(MessageRepository messages) {
		this.messages = messages;
	}

	@PostConstruct
	public void init() {
		/*
		Message[] defaultMessages = {
			new Message("Paco", "Mensaje 1.1"),
			new Message("Juan", "Mensaje 1.2"),
			new Message("Antonio", "Mensaje 2.1"),
			new Message("Jose", "Mensaje 2.2")
		};

		for (Message message : defaultMessages) {
			messages.save(message);
		}
		*/
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

	public void save(Message message) {
		messages.save(message);
	}

	public void deleteById(long id) {
		this.messages.deleteById(id);
	}

}