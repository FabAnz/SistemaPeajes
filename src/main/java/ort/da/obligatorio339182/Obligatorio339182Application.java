package ort.da.obligatorio339182;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ort.da.obligatorio339182.services.Fachada;

@SpringBootApplication
public class Obligatorio339182Application {

	public static void main(String[] args) {
		SpringApplication.run(Obligatorio339182Application.class, args);
	}

	@Bean
	public CommandLineRunner init(DatosPrecargados datosPrecargados, Fachada fachada) {
		return args -> {
			// 1. Cargar datos precargados (usuarios, puestos, tarifas, etc.)
			datosPrecargados.cargarDatos();
			
			// 2. Inicializar el sistema (lógica adicional de inicio)
			fachada.iniciarSistema();
		};
	}

}
