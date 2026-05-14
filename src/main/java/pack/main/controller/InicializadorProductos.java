package pack.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import pack.main.model.Producto;
import pack.main.repository.ProductoRepository;

@Component
public class InicializadorProductos implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {

        if (productoRepository.count() == 0) {

            productoRepository.save(new Producto(
                    "Manzana", 1.20,
                    "https://images.unsplash.com/photo-1567306226416-28f0efdc88ce", true));

            productoRepository.save(new Producto(
                    "Pan", 0.80,
                    "https://images.unsplash.com/photo-1608198093002-ad4e005484ec", true));

            productoRepository.save(new Producto(
                    "Leche", 1.10,
                    "https://img.freepik.com/vector-gratis/cajas-leche-realistas-aisladas_1284-35984.jpg?semt=ais_hybrid&w=740&q=80" , true));

            productoRepository.save(new Producto(
                    "Queso", 2.50,
                    "https://recetasdecocina.elmundo.es/wp-content/uploads/2025/05/queso-camembert.jpg", true));

            productoRepository.save(new Producto(
                    "Huevos", 2.00,
                    "https://gourmet.expob2b.es/uploads/fotos_noticias/2018/02/17206-137822-como-determinamos-la-frescura-y-calidad-de-los-huevos.jpg" , true));

            System.out.println("PRODUCTOS CREADOS");
        }
    }
}