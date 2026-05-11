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
                    "https://images.unsplash.com/photo-1567306226416-28f0efdc88ce"));

            productoRepository.save(new Producto(
                    "Pan", 0.80,
                    "https://images.unsplash.com/photo-1608198093002-ad4e005484ec"));

            productoRepository.save(new Producto(
                    "Leche", 1.10,
                    "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b"));

            productoRepository.save(new Producto(
                    "Queso", 2.50,
                    "https://images.unsplash.com/photo-1585238342024-78d387f4a707"));

            productoRepository.save(new Producto(
                    "Huevos", 2.00,
                    "https://images.unsplash.com/photo-1517959105821-eaf2591984ca"));

            System.out.println("PRODUCTOS CREADOS");
        }
    }
}