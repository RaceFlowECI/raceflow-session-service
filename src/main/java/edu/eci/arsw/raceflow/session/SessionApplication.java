package edu.eci.arsw.raceflow.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del esqueleto de Session Service. Reservado para persistir sesiones
 * de entrenamiento completadas en almacenamiento durable; se mantiene como un
 * microservicio placeholder con su andamiaje de métricas en su lugar.
 */
@SpringBootApplication
public class SessionApplication {
    /** @param args argumentos de línea de comandos pasados a Spring Boot */
    public static void main(String[] args) {
        SpringApplication.run(SessionApplication.class, args);
    }
}
