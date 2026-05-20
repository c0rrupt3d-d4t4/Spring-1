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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	@GetMapping("/admin/permisos")
	public String permisosUsuarios(Model model, HttpSession session) {

		String usuarioActivo = (String) session.getAttribute("usuarioLogueado");

		if (usuarioActivo == null) {
			return "redirect:/index";
		}

		// Traemos todos los usuarios
		List<Usuario> usuarios = usuarioRepository.findAll();

		// Filtramos: quitar "admin" y el usuario actual
		usuarios.removeIf(u -> u.getNombreUsuario().equals("admin") || u.getNombreUsuario().equals(usuarioActivo));

		model.addAttribute("usuarios", usuarios);
		model.addAttribute("usuarioActivo", usuarioActivo);

		return "permisosAdminUser";
	}

	@PostMapping("/admin/toggle-admin")
	@ResponseBody
	public String cambiarAdmin(@RequestParam String nombre) {

		Usuario usuario = usuarioRepository.findByNombreUsuario(nombre);

		if (usuario != null) {
			usuario.setAdmin(!usuario.isAdmin());
			usuarioRepository.save(usuario);
		}

		return "ok";
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
			listarTodosLosPedidos(usuario, model, session);
			return "redirect:/admin/listadoPedidosTotales";
		}

		cargarVistaUsuario(model, session);
		return "panelUser";
	}

	// LISTADO DE PEDIDOS DEL ADMINISTRADOR CON FILTRO DE BÚSQUEDA
	@GetMapping("/admin/listadoPedidosTotales")
	public String listarTodosLosPedidos(@RequestParam(name = "textoBusqueda", required = false) String textoBusqueda,
			Model model, HttpSession session) {

		List<Pedido> todosLosPedidos = pedidoRepository.findAll();

		if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
			final String textoALower = textoBusqueda.toLowerCase().trim();

			todosLosPedidos = todosLosPedidos.stream()
					.filter(pedido -> String.valueOf(pedido.getId()).contains(textoALower) ||

							(pedido.getNombreUsuario() != null
									&& pedido.getNombreUsuario().toLowerCase().contains(textoALower))
							||

							pedido.getItems().stream()
									.anyMatch(item -> item.getNombre() != null
											&& item.getNombre().toLowerCase().contains(textoALower)))
					.collect(Collectors.toList());
		}

		model.addAttribute("pedidos", todosLosPedidos);

		model.addAttribute("textoBusqueda", textoBusqueda);

		return "panelAdmin";
	}
	// HACER PEDIDO

	@PostMapping("/hacer-pedido")
	public String hacerPedido(Model model, HttpSession session) {
		String nombre = (String) session.getAttribute("usuarioLogueado");

		if (nombre == null)
			return "redirect:/index";

		double total = calcularTotal();
		LocalDate fecha = LocalDate.now();
				
		Pedido pedido = new Pedido(nombre, new ArrayList<>(carrito), total, fecha);
		pedidoRepository.save(pedido);

		carrito.clear(); // Limpiamos el carrito

		model.addAttribute("mensajePedido", "Pedido realizado correctamente");

		cargarVistaUsuario(model, session);
		return "redirect:/panelUser";
	}

	// REGISTRO
	@PostMapping("/registro")
	public String registro(@RequestParam String usuario, @RequestParam String password, Model model) {

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