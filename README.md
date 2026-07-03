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

---

## Estructura del proyecto

---

## Configuracion local

---

## Endpoints REST

---

## Pruebas y calidad

---

## CI/CD

