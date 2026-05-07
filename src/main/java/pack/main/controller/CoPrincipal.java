package pack.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <--- ASEGÚRATE DE QUE SEA ESTE
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pack.main.model.Mensaje;
import pack.main.repository.MensajeRepository;
import pack.main.model.Usuario;
import pack.main.repository.UsuarioRepository;

@Controller
public class CoPrincipal {
	@Autowired
	private MensajeRepository repositorio;
	@Autowired
	private UsuarioRepository usuarioRepository;

    // Carga la página por primera vez (GET)
    @GetMapping(value = {"/index", "/"})
    public String idx() {
        return "index";
    }

    // Maneja el clic del botón (POST)
    @PostMapping("/pulsarBoton")
    public String procesarClic(Model model) {
        // 1. Creamos el objeto con el texto que queramos
        Mensaje m = new Mensaje("Alguien pulsó el botón");

        // 2. LO GUARDAMOS EN MONGODB
        repositorio.save(m);

        // 3. Pasamos el ID a la web para confirmar que funcionó
        model.addAttribute("resultado", "Guardado en Mongo con ID: " + m.getId());
        
        return "index"; 
    }
    
    // HE CAMBIADO ESTO DEL LOGIN
    
    @PostMapping("/inicio-sesion")
    public String inicioSesion(
            @RequestParam String usuario,
            @RequestParam String password,
            Model model) {

        Usuario usuarioEncontrado =
                usuarioRepository.findByNombreUsuarioAndPassword(usuario, password);

        if (usuarioEncontrado == null) {

            model.addAttribute("mensajeLogin",
                    "Los datos introducidos no son correctos");

            return "index";
        }

        model.addAttribute("nombreUsuario",
                usuarioEncontrado.getNombreUsuario());

        model.addAttribute("admin",
                usuarioEncontrado.isAdmin());

        // TE DEVUELVE PANEL QUE SERIA EL SEGUNDO HTML
        return "panel";
    }
    
    // HE CAMBIADO ESTO NUEVO BOTON DE REGISTRO
    
    @PostMapping("/registro")
    public String registro(
            @RequestParam String usuario,
            @RequestParam String password,
            Model model) {

        Usuario existe =
                usuarioRepository.findByNombreUsuario(usuario);

        if (existe != null) {

            model.addAttribute("mensajeRegistro",
                    "Ese usuario ya existe");

            return "index";
        }

        Usuario nuevoUsuario =
                new Usuario(usuario, password, false);

        usuarioRepository.save(nuevoUsuario);

        model.addAttribute("mensajeRegistro",
                "Se ha registrado correctamente");

        return "index";
    }
}