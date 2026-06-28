# raceflow-session-service
Microservicio de historial de RaceFlow. Persiste sesiones finalizadas y permite consultar el historial del atleta.

**Stack:** Java 21 · Spring Boot · PostgreSQL (Session DB) · RabbitMQ (consumidor)
**Responsabilidad:** Consumir evento sesión_finalizada de RabbitMQ · Persistir sesión · Exponer historial
**Escala:** ×1
