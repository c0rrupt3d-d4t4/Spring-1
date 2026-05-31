package pack.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import pack.main.model.Usuario;
import pack.main.repository.UsuarioRepository;

@Component
public class InicializadorUsuarios implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public void run(String[] args) throws Exception {
		// Crea los usuarios iniciales, uno básico y otro administrador en caso de que no existan

		if (usuarioRepository.findByNombreUsuario("admin") == null) {

			Usuario admin = new Usuario("admin", "admin", true);

			usuarioRepository.save(admin);

		}

		if (usuarioRepository.findByNombreUsuario("usuario") == null) {

			Usuario usuario = new Usuario("usuario", "1234", false);

			usuarioRepository.save(usuario);

		}
	}
}