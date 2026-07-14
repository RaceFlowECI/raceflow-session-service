package edu.eci.arsw.raceflow.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Session Service skeleton. Reserved for persisting completed
 * training sessions to durable storage; kept as a placeholder microservice with
 * its metrics scaffolding in place.
 */
@SpringBootApplication
public class SessionApplication {
    /** @param args command-line arguments passed to Spring Boot */
    public static void main(String[] args) {
        SpringApplication.run(SessionApplication.class, args);
    }
}
