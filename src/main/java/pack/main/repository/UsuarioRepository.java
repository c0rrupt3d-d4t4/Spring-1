package pack.main.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pack.main.model.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	/*
	 * Esta extension que usamos da metodos que directamente hacen:
	 *  save(usuario): insertar o actualizar 
	 * findAll(): un SELECT de todos los usuarios
	 * deleteById(id): DELETE
	 */
	// metodo para el login, te devuelve un Usuario o sino null
	Usuario findByNombreUsuarioAndPassword(String nombreUsuario, String password);

	// busca si existe ese nombre de usuario 
	Usuario findByNombreUsuario(String nombreUsuario);
}