package pack.main.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "productos")
public class Producto {

	@Id
	private String id;

	private String nombre;
	private double precio;
	private String imagenUrl;
	private boolean disponible;

	public Producto() {
	}

	public Producto(String nombre, double precio, String imagenUrl, boolean disponible) {
		this.nombre = nombre;
		this.precio = precio;
		this.imagenUrl = imagenUrl;
		this.disponible = disponible;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public String getImagenUrl() {
		return imagenUrl;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public void setImagenUrl(String imagenUrl) {
		this.imagenUrl = imagenUrl;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

}