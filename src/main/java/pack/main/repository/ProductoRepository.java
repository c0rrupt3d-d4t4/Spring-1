package pack.main.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pack.main.model.Producto;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {

}