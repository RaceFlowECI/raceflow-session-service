package edu.eci.arsw.raceflow.session.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

/** Contadores/temporizadores de Micrometer para session-service, expuestos en {@code /actuator/prometheus}. */
@Component
public class SessionMetrics {

    private final Counter sessionsPersisted;
    private final Timer persistenceLag;

    /** @param registry el registro de Micrometer al cual asociar estas métricas */
    public SessionMetrics(MeterRegistry registry) {
        this.sessionsPersisted = Counter.builder("raceflow.sessions.persisted")
                .description("Total sessions persisted to database")
                .register(registry);

        // Objective < 2s
        this.persistenceLag = Timer.builder("raceflow.sessions.persistence.lag")
                .description("Lag between session end event and DB persistence")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }

    /** Incrementa el contador total de sesiones persistidas. */
    public void recordSessionPersisted() { sessionsPersisted.increment(); }
    /** @return el temporizador usado para medir el retraso entre el fin de sesión y la persistencia en BD (objetivo &lt; 2s) */
    public Timer getPersistenceLag() { return persistenceLag; }
}
