package ort.da.obligatorio339182;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Obligatorio339182Application {

	public static void main(String[] args) {
		SpringApplication.run(Obligatorio339182Application.class, args);
	}

	@Bean
	public CommandLineRunner init(DatosPrecargados datosPrecargados) {
		return args -> {
			datosPrecargados.cargarDatos();
		};
	}

}
