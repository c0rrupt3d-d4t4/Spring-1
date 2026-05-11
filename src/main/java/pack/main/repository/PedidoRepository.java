package pack.main.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pack.main.model.Pedido;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, String> {
}