package pack.main.repository;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pack.main.model.Pedido;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, String> {
	
	ArrayList<Pedido> findByNombreUsuario(String usuario);
}