# Architecture

## Overview

The app is a desktop JavaFX client with Spring Boot running in-process for DI, service management, and persistence wiring.

No HTTP server is exposed.

## Layers

- `ui/`: JavaFX views and UI controller
- `service/`: task business logic and validation
- `repo/`: Spring Data JPA repository
- `domain/`: JPA entities and enums
- `dto/`: service boundary request/response models
- `search/` (Kotlin): search filtering and ranking utility

## Startup Flow

1. `Main` launches `ApplicationLauncher`
2. `ApplicationLauncher` starts Spring context
3. Spring creates `MainView` on demand
4. JavaFX scene is shown with table + form

## Data Flow

1. User interacts with JavaFX controls
2. `MainView` calls `MainController`
3. `MainController` invokes `TaskService`
4. `TaskServiceImpl` validates and persists via `TaskRepository`
5. Search queries are ranked via `TaskSearch.kt`
6. DTOs return to UI for rendering

## Persistence

- H2 file-backed DB
- Config: `src/main/resources/application.yml`
- URL: `jdbc:h2:file:./data/taskdb`

## Testing Strategy

- Unit tests for service logic
- Integration test with Spring context and in-memory H2
- Controller-level tests for UI orchestration behavior
