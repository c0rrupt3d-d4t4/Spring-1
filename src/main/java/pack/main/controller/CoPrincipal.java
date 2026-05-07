package pack.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <--- ASEGÚRATE DE QUE SEA ESTE
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import pack.main.model.Mensaje;
import pack.main.repository.MensajeRepository;

@Controller
public class CoPrincipal {
	@Autowired
	private MensajeRepository repositorio;

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
}