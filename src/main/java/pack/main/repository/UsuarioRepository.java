package pack.main.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pack.main.model.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Usuario findByNombreUsuarioAndPassword(String nombreUsuario, String password);

    Usuario findByNombreUsuario(String nombreUsuario);
}