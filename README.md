# RACEFLOW — Session Service

> [!IMPORTANT]
> Este repositorio contiene el **Session Service** de RaceFlow: registro y ciclo de vida de sesiones de competicion.

> Para informacion general consulta el [perfil de la organizacion](https://github.com/RaceFlowECI).

---

## Tabla de contenido
- [Descripcion general](#descripcion-general)
- [Stack tecnologico](#stack-tecnologico)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Configuracion local](#configuracion-local)
- [Endpoints REST](#endpoints-rest)
- [Pruebas y calidad](#pruebas-y-calidad)
- [CI/CD](#cicd)
- [Observabilidad](#observabilidad)
- [Logs estructurados](#logs-estructurados)

---

## Descripcion general

> [!NOTE]
> Microservicio de gestion de sesiones de competicion. Registra el inicio y fin de cada participacion, calcula el tiempo total y publica los resultados en RabbitMQ para que Metrics Service los consuma.

### Responsabilidades principales

| Responsabilidad | Descripcion |
|---|---|
| **Inicio** | Registra el momento en que un participante comienza la competicion. |
| **Fin** | Marca la finalizacion y calcula el tiempo total de la sesion. |
| **Historial** | Expone el historial de sesiones por usuario y por sala. |
| **Eventos** | Publica en RabbitMQ los resultados para que sean procesados por Metrics. |

---

## Stack tecnologico

### Backend
![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

### Testing y calidad
![JUnit](https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=java&logoColor=white)
![JaCoCo](https://img.shields.io/badge/JaCoCo-BB0A30?style=for-the-badge)
![SonarQube](https://img.shields.io/badge/SonarQube-4E9BCD?style=for-the-badge&logo=sonarqube&logoColor=white)

### DevOps
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

---

## Estructura del proyecto

```text
raceflow-session-service/
├── .github/workflows/
├── .env.example
├── .gitignore
├── Dockerfile
├── pom.xml
└── src/main/java/edu/eci/arsw/raceflow/session/
    ├── SessionApplication.java
    ├── config/
    │   └── RabbitMQConfig.java
    ├── controller/
    │   └── SessionController.java
    ├── dto/
    │   ├── StartSessionRequest.java
    │   └── SessionResponse.java
    ├── model/
    │   └── Session.java
    ├── repository/
    │   └── SessionRepository.java
    └── service/
        ├── SessionService.java
        └── SessionEventPublisher.java
```

---

## Configuracion local

### 1. Clonar el repositorio
```bash
git clone https://github.com/RaceFlowECI/raceflow-session-service.git
cd raceflow-session-service
```

### 2. Compilar
```bash
mvn clean install
```

### 3. Configurar variables de entorno
```bash
cp .env.example .env
```
```env
DB_HOST=localhost
DB_USER=raceflow
DB_PASSWORD=secret
RABBITMQ_HOST=localhost
```

### 4. Ejecutar
```bash
mvn spring-boot:run
```
> [!TIP]
> El servicio arranca en `http://localhost:8084`. Requiere PostgreSQL y RabbitMQ.

---

## Endpoints REST

| Metodo | Ruta | Auth | Descripcion |
|---|---|---|---|
| `POST` | `/sessions` | JWT | Inicia una nueva sesion de competicion. |
| `POST` | `/sessions/{id}/finish` | JWT | Finaliza la sesion y calcula el tiempo. |
| `GET` | `/sessions/user/{userId}` | JWT | Historial de sesiones de un usuario. |
| `GET` | `/sessions/room/{roomCode}` | JWT | Sesiones de una sala especifica. |

### Ejemplo: iniciar sesion
```bash
curl -X POST http://localhost:8084/sessions \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"roomCode":"ABC123","userId":"user-uuid"}'
```

---

## Pruebas y calidad
```bash
mvn test
mvn clean test jacoco:report
```

---

## CI/CD

| Campo | Valor |
|---|---|
| Puerto | 8084 |
| Plataforma | _por definir_ |
| Ultima version | ![CI](https://github.com/RaceFlowECI/raceflow-session-service/actions/workflows/ci.yml/badge.svg) |

---

## Observabilidad

### Endpoint de métricas
```
GET http://localhost:8084/actuator/prometheus
```
También disponibles: `/actuator/health`, `/actuator/info`, `/actuator/metrics`.

### Métricas de negocio

| Métrica | Tipo | Descripcion |
|---|---|---|
| `raceflow_sessions_persisted_total` | Counter | Sesiones persistidas en base de datos |
| `raceflow_sessions_persistence_lag_seconds` | Timer | Lag entre fin del evento y persistencia (objetivo < 2s) |


### Verificación local
```bash
# Con el servicio corriendo:
curl -s http://localhost:8084/actuator/prometheus | grep raceflow_
```

> [!NOTE]
> Micrometer convierte puntos a guiones bajos: `raceflow.rooms.created` → `raceflow_rooms_created_total` en Prometheus.

---

## Logs estructurados

Los logs se emiten en formato **JSON (Logstash)** tanto a consola como a archivo, lo que permite ingestión directa por Promtail → Loki.

### Archivos generados
```
logs/<nombre-servicio>.log               ← archivo activo
logs/<nombre-servicio>.2026-07-05.log    ← rotado por fecha (retención 7 días)
```

### Estructura de un log entry
```json
{
  "@timestamp": "2026-07-05T10:00:00.000-05:00",
  "@version":   "1",
  "message":    "User registered successfully",
  "logger_name":"edu.eci.arsw.raceflow.auth.service.AuthService",
  "thread_name":"http-nio-8081-exec-1",
  "level":      "INFO",
  "level_value": 20000
}
```

### Consulta en Loki (LogQL)
```logql
{service="raceflow-auth-service"} | json | level="ERROR"
```
