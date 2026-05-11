package pack.main.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "pedidos")
public class Pedido {

    @Id
    private String id;

    private String nombreUsuario;
    private List<ItemPedido> items;
    private double total;

    public Pedido() {}

    public Pedido(String nombreUsuario, List<ItemPedido> items, double total) {
        this.nombreUsuario = nombreUsuario;
        this.items = items;
        this.total = total;
    }

    public String getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public List<ItemPedido> getItems() { return items; }
    public double getTotal() { return total; }

    public void setId(String id) { this.id = id; }
}