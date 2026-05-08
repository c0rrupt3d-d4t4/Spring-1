package pack.main.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pack.main.model.Mensaje;

@Repository
public interface MensajeRepository extends MongoRepository<Mensaje, String> {
	
	// Al heredar de MongoRepository ya tienes métodos como .save() y .findAll()
}