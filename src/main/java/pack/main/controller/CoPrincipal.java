package pack.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pack.main.model.Mensaje;
import pack.main.model.Usuario;
import pack.main.model.Producto;
import pack.main.model.ItemPedido;
import pack.main.model.Pedido;

import pack.main.repository.MensajeRepository;
import pack.main.repository.UsuarioRepository;
import pack.main.repository.ProductoRepository;
import pack.main.repository.PedidoRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CoPrincipal {

    @Autowired
    private MensajeRepository repositorio;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    // 🛒 CARRITO (simple, global)
    private List<ItemPedido> carrito = new ArrayList<>();
    private String usuarioActual = "";

    // INDEX
    @GetMapping(value = {"/index", "/", "/index.html"})
    public String idx() {
        return "index";
    }
    
  //Modificar producto
    @GetMapping("/admin/modificarProducto")
    public String modificarFormularioProducto() {
        return "modificarProducto";
    }
    

    //Añadir producto
    @GetMapping("/admin/anadirProducto")
    public String mostrarFormularioProducto() {
        return "anadirProducto";
    }
    @PostMapping("/admin/guardar-producto")
    public String guardarProducto(@RequestParam String nombre,
                                  @RequestParam double precio,
                                  @RequestParam String imagenUrl,
                                  @RequestParam(required = false) String disponible,
                                  Model model) {

        if (nombre.isEmpty() || imagenUrl.isEmpty()) {
            model.addAttribute("mensaje", "Todos los campos son obligatorios");
            return "anadirProducto";
        }

        boolean disponibleBool = (disponible != null);

        Producto producto = new Producto(nombre, precio, imagenUrl, disponibleBool);

        productoRepository.save(producto);

        model.addAttribute("mensaje", "Producto añadido correctamente");

        return "anadirProducto";
    }

    // LOGIN
    @PostMapping("/inicio-sesion")
    public String inicioSesion(@RequestParam String usuario,
                               @RequestParam String password,
                               Model model) {

        Usuario usuarioEncontrado =
                usuarioRepository.findByNombreUsuarioAndPassword(usuario, password);

        if (usuarioEncontrado == null) {

            model.addAttribute("mensajeLogin", "Los datos introducidos no son correctos");
            return "index";
        }

        usuarioActual = usuarioEncontrado.getNombreUsuario();

        model.addAttribute("nombreUsuario", usuarioActual);

        if (usuarioEncontrado.isAdmin()) {
            return "panelAdmin";
        }

        cargarVistaUsuario(model);
        return "panelUser";
    }

    // REGISTRO
    @PostMapping("/registro")
    public String registro(@RequestParam String usuario,
                           @RequestParam String password,
                           Model model) {

        if (usuario.isEmpty() || password.isEmpty()) {
            model.addAttribute("mensajeRegistro", "Está vacío el usuario o contraseña");
            return "index";
        }

        Usuario existe = usuarioRepository.findByNombreUsuario(usuario);

        if (existe != null) {
            model.addAttribute("mensajeRegistro", "Ese usuario ya existe");
            return "index";
        }

        Usuario nuevo = new Usuario(usuario, password, false);
        usuarioRepository.save(nuevo);

        model.addAttribute("mensajeRegistro", "Se ha registrado correctamente");

        return "index";
    }

    // AÑADIR AL CARRITO
    @PostMapping("/add-carrito")
    @ResponseBody
    public String addCarrito(@RequestParam String nombre,
                             @RequestParam double precio) {

        boolean encontrado = false;

        for (ItemPedido item : carrito) {
            if (item.getNombre().equals(nombre)) {
                item.setCantidad(item.getCantidad() + 1);
                encontrado = true;
            }
        }

        if (!encontrado) {
            carrito.add(new ItemPedido(nombre, precio, 1));
        }

        System.out.println("Añadido: " + nombre);
        System.out.println("Carrito size: " + carrito.size());

        return "ok";
    }

    // ➕ SUMAR
    @PostMapping("/sumar")
    @ResponseBody
    public String sumar(@RequestParam String nombre) {

        for (ItemPedido item : carrito) {
            if (item.getNombre().equals(nombre)) {
                item.setCantidad(item.getCantidad() + 1);
            }
        }

        return "ok";
    }

    // RESTAR
    @PostMapping("/restar")
    @ResponseBody
    public String restar(@RequestParam String nombre) {

        carrito.removeIf(item -> {
            if (item.getNombre().equals(nombre)) {
                item.setCantidad(item.getCantidad() - 1);
                return item.getCantidad() <= 0;
            }
            return false;
        });

        return "ok";
    }

    // HACER PEDIDO
    @PostMapping("/hacer-pedido")
    public String hacerPedido(Model model) {

        double total = calcularTotal();

        Pedido pedido = new Pedido(usuarioActual, carrito, total);
        pedidoRepository.save(pedido);

        carrito = new ArrayList<>();

        model.addAttribute("mensajePedido", "Pedido realizado correctamente");

        cargarVistaUsuario(model);
        return "panelUser";
    }

    // CARGAR VISTA USUARIO
    private void cargarVistaUsuario(Model model) {

        List<Producto> productos = productoRepository.findAll();

        model.addAttribute("productos", productos);
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", calcularTotal());
        model.addAttribute("nombreUsuario", usuarioActual);
    }

    // CALCULAR TOTAL
    private double calcularTotal() {

        double total = 0;

        for (ItemPedido item : carrito) {
            total += item.getPrecio() * item.getCantidad();
        }

        return total;
    }
}