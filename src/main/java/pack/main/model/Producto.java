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

    public Producto() {}

    public Producto(String nombre, double precio, String imagenUrl) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getImagenUrl() { return imagenUrl; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}