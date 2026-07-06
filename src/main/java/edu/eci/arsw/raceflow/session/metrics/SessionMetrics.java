package edu.eci.arsw.raceflow.session.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SessionMetrics {

    private final Counter sessionsPersisted;
    private final Timer persistenceLag;

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

    public void recordSessionPersisted() { sessionsPersisted.increment(); }
    public Timer getPersistenceLag() { return persistenceLag; }
}
