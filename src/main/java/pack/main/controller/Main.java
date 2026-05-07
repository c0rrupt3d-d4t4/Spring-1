package pack.main.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@ComponentScan(basePackages = "pack.main") // Obliga a escanear todo pack.main
@EnableMongoRepositories(basePackages = "pack.main.repository") // Obliga a buscar repositorios aquí
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

	}

}