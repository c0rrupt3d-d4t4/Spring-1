package pack.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import pack.main.model.Usuario;
import pack.main.model.Producto;
import pack.main.model.ItemPedido;
import pack.main.model.Pedido;

import pack.main.repository.UsuarioRepository;
import pack.main.repository.ProductoRepository;
import pack.main.repository.PedidoRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CoPrincipal {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	// El carrito sigue siendo global (se comparte entre todos, ¡ojo con esto más
	// adelante!)
	private ArrayList<ItemPedido> carrito = new ArrayList<>();

	// INDEX
	@GetMapping(value = { "/index", "/", "/index.html" })
	public String idx() {
		return "index";
	}

	// PANEL DE USUARIO (Verifica sesión)
	@GetMapping("/panelUser")
	public String volverAlPanel(Model model, HttpSession session) {
		String nombre = (String) session.getAttribute("usuarioLogueado");

		if (nombre == null) {
			return "redirect:/index";
		}

		cargarVistaUsuario(model, session);
		return "panelUser";
	}

	// LISTAR PEDIDOS (Verifica sesión)
	@GetMapping(value = { "/user/listadoPedidosUser", "/user/listadoPedidosUser.html" })
	public String listarPedidoUser(Model model, HttpSession session) {
		String nombre = (String) session.getAttribute("usuarioLogueado");

		if (nombre == null) {
			return "redirect:/index";
		}

		ArrayList<Pedido> pedidos = pedidoRepository.findByNombreUsuario(nombre);

		model.addAttribute("pedidos", pedidos);
		model.addAttribute("nombreUsuario", nombre);

		return "listadoPedidosUser";
	}

	// LOGIN
	@PostMapping("/inicio-sesion")
	public String inicioSesion(@RequestParam String usuario, @RequestParam String password, HttpSession session,
			Model model) {

		Usuario usuarioEncontrado = usuarioRepository.findByNombreUsuarioAndPassword(usuario, password);

		if (usuarioEncontrado == null) {
			model.addAttribute("mensajeLogin", "Los datos introducidos no son correctos");
			return "index";
		}

		// GUARDAMOS AL USUARIO EN LA SESIÓN
		session.setAttribute("usuarioLogueado", usuarioEncontrado.getNombreUsuario());

		if (usuarioEncontrado.isAdmin()) {
			listarTodosLosPedidos(model, session);
			return "redirect:/admin/listadoPedidosTotales";
		}

		cargarVistaUsuario(model, session);
		return "panelUser";
	}

	@GetMapping("/admin/listadoPedidosTotales")
	public String listarTodosLosPedidos(Model model, HttpSession session) {

		List<Pedido> todosLosPedidos = pedidoRepository.findAll();
		model.addAttribute("pedidos", todosLosPedidos);

		return "panelAdmin"; // Nombre del nuevo HTML
	}

	// HACER PEDIDO
	@PostMapping("/hacer-pedido")
	public String hacerPedido(Model model, HttpSession session) {
		String nombre = (String) session.getAttribute("usuarioLogueado");

		if (nombre == null)
			return "redirect:/index";

		double total = calcularTotal();

		Pedido pedido = new Pedido(nombre, new ArrayList<>(carrito), total);
		pedidoRepository.save(pedido);

		carrito.clear(); // Limpiamos el carrito

		model.addAttribute("mensajePedido", "Pedido realizado correctamente");

		cargarVistaUsuario(model, session);
		return "panelUser";
	}

	// --- MÉTODOS DE APOYO ---

	private void cargarVistaUsuario(Model model, HttpSession session) {
		String nombre = (String) session.getAttribute("usuarioLogueado");
		List<Producto> productos = productoRepository.findAll();

		model.addAttribute("productos", productos);
		model.addAttribute("carrito", carrito);
		model.addAttribute("total", calcularTotal());
		model.addAttribute("nombreUsuario", nombre);
	}

	private double calcularTotal() {
		double total = 0;
		for (ItemPedido item : carrito) {
			total += item.getPrecio() * item.getCantidad();
		}
		return total;
	}

	// MÉTODOS DEL CARRITO (Add, Sumar, Restar...)
	@PostMapping("/add-carrito")
	@ResponseBody
	public String addCarrito(@RequestParam String nombre, @RequestParam double precio) {
		boolean encontrado = false;
		for (ItemPedido item : carrito) {
			if (item.getNombre().equals(nombre)) {
				item.setCantidad(item.getCantidad() + 1);
				encontrado = true;
			}
		}
		if (!encontrado)
			carrito.add(new ItemPedido(nombre, precio, 1));
		return "ok";
	}

	@PostMapping("/sumar")
	@ResponseBody
	public String sumar(@RequestParam String nombre) {
		for (ItemPedido item : carrito) {
			if (item.getNombre().equals(nombre))
				item.setCantidad(item.getCantidad() + 1);
		}
		return "ok";
	}

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

	// ADMINISTRACIÓN (Simplificados para brevedad)
	@GetMapping("/admin/modificarProducto")
	public String mostrarModificarProductos(Model model) {
		model.addAttribute("productos", productoRepository.findAll());
		return "modificarProducto";
	}
	// url modificar producto
	@PostMapping("/admin/modificar-producto")
	public String modificarProducto(@RequestParam String id, @RequestParam String nombre, @RequestParam double precio,
			@RequestParam String imagenUrl, @RequestParam(required = false) String disponible) {

		Producto producto = productoRepository.findById(id).orElse(null);

		if (producto != null) {
			producto.setNombre(nombre);
			producto.setPrecio(precio);
			producto.setImagenUrl(imagenUrl);
			// Si el checkbox no se marca, llega como null
			producto.setDisponible(disponible != null);

			productoRepository.save(producto);
		}

		// Redirige de nuevo a la lista para ver los cambios
		return "redirect:/admin/modificarProducto";
	}

	@PostMapping("/admin/eliminar-producto")
	public String eliminarProducto(@RequestParam String id) {
		productoRepository.deleteById(id);
		return "redirect:/admin/modificarProducto";
	}

	@GetMapping("/admin/anadirProducto")
	public String mostrarFormularioProducto() {
		return "anadirProducto";
	}
}