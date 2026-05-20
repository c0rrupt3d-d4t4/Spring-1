package pack.main.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "pedidos")
public class Pedido {

    @Id
    private String id;

    private String nombreUsuario;
    private List<ItemPedido> items;
    private double total;
    private LocalDate fecha;

    public Pedido() {}

    public Pedido(String nombreUsuario, List<ItemPedido> items, double total, LocalDate fecha) {
        this.nombreUsuario = nombreUsuario;
        this.items = items;
        this.total = total;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public List<ItemPedido> getItems() { return items; }
    public double getTotal() { return total; }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        // Opción de respaldo: si llega vacía desde el formulario, le asigna la fecha de hoy
        if (fecha == null) {
            this.fecha = LocalDate.now();
        } else {
            this.fecha = fecha;
        }
    }
    public void setId(String id) { this.id = id; }
}