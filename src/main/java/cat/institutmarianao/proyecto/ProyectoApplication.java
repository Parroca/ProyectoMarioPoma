package cat.institutmarianao.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoApplication {
	public static final String DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";

	public static void main(String[] args) {
		SpringApplication.run(ProyectoApplication.class, args);
	}

}
