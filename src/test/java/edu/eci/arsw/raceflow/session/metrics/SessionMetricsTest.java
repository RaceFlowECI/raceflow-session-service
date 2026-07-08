package edu.eci.arsw.raceflow.session.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class SessionMetricsTest {

    private MeterRegistry registry;
    private SessionMetrics metrics;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        metrics = new SessionMetrics(registry);
    }

    @Test
    void recordSessionPersistedIncrementsCounter() {
        metrics.recordSessionPersisted();
        metrics.recordSessionPersisted();

        assertThat(registry.get("raceflow.sessions.persisted").counter().count())
                .isEqualTo(2.0);
    }

    @Test
    void persistenceLagTimerRecordsDuration() {
        metrics.getPersistenceLag().record(Duration.ofMillis(150));

        assertThat(registry.get("raceflow.sessions.persistence.lag").timer().count())
                .isEqualTo(1);
        assertThat(registry.get("raceflow.sessions.persistence.lag").timer().totalTime(TimeUnit.MILLISECONDS))
                .isGreaterThanOrEqualTo(150.0);
    }
}
