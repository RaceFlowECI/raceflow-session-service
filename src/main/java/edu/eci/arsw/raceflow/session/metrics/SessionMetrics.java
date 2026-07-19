package edu.eci.arsw.raceflow.session.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

/** Micrometer counters/timers for session-service, exposed at {@code /actuator/prometheus}. */
@Component
public class SessionMetrics {

    private final Counter sessionsPersisted;
    private final Timer persistenceLag;

    /** @param registry the Micrometer registry to bind these meters to */
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

    /** Increments the total sessions-persisted counter. */
    public void recordSessionPersisted() { sessionsPersisted.increment(); }
    /** @return the timer used to measure lag between session end and DB persistence (objective &lt; 2s) */
    public Timer getPersistenceLag() { return persistenceLag; }
}
