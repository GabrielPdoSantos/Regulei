# Regulei — MVP de Acompanhamento Menstrual

Aplicação web PWA (mobile-first, pt-BR) para registro individual de menstruações e visualização de calendário mensal com estimativas de ciclo.

## Arquitetura

- **Backend (`backend/`):** Java 21, Spring Boot, Maven, API REST (`/api/v1`).
- **Frontend (`frontend/`):** Angular, TypeScript, PWA.

## Como executar localmente

### 1. Backend
```bash
cd backend
./mvnw spring-boot:run