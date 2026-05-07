package pack.main.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "registro_clicks") // Nombre de la tabla en Mongo
public class Mensaje {

	@Id
	private String id; // Mongo usará esto para el ID autogenerado
	private String contenido;

	// Constructor vacío (obligatorio para Spring)
	public Mensaje() {
	}

	// Constructor para facilitar el uso
	public Mensaje(String contenido) {
		this.contenido = contenido;
	}

	// Getters y Setters (imprescindibles si no usas Lombok)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
}