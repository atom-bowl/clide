# Architecture

## Overview

The app is a desktop JavaFX client with Spring Boot running in-process for DI, service management, and persistence wiring.

No HTTP server is exposed.

## Layers

- `ui/`: JavaFX views and UI controllers
  - `MainView`: TabPane orchestrator
  - `TasksView`: Task management interface
  - `CalendarView`: Month calendar with task visualization
  - `TodoListsView`: TODO list management
  - `NotesView`: Notes and journaling interface
  - `MainController`: Tasks tab business logic
- `service/`: Business logic and validation
  - `TaskService`: Task management
  - `TodoListService`: TODO list and item operations
  - `NoteService`: Note CRUD operations
- `repo/`: Spring Data JPA repositories
  - `TaskRepository`, `TodoListRepository`, `TodoItemRepository`, `NoteRepository`
- `domain/`: JPA entities and enums
  - `Task`, `TodoList`, `TodoItem`, `Note`, `TaskStatus`
- `dto/`: Service boundary request/response models
- `search/` (Kotlin): Search filtering and ranking utility

## Startup Flow

1. `Main` launches `ApplicationLauncher`
2. `ApplicationLauncher` starts Spring context
3. Spring creates view components on demand:
   - `MainView` (TabPane container)
   - `TasksView`, `CalendarView`, `TodoListsView`, `NotesView`
4. JavaFX scene is shown with tab-based interface

## Data Flow

### Tasks Tab
1. User interacts with JavaFX controls in `TasksView`
2. `TasksView` calls `MainController`
3. `MainController` invokes `TaskService`
4. `TaskServiceImpl` validates and persists via `TaskRepository`
5. Search queries are ranked via `TaskSearch.kt`
6. DTOs return to UI for rendering

### Calendar Tab
1. `CalendarView` loads all tasks via `MainController`
2. Groups tasks by due date for visualization
3. User selects date to filter tasks

### TODO Lists Tab
1. `TodoListsView` calls `TodoListService` directly
2. Service manages `TodoList` and `TodoItem` entities
3. Changes persist immediately via repositories

### Notes Tab
1. `NotesView` calls `NoteService` directly
2. Service manages `Note` entities
3. Notes are sorted by creation timestamp

## Persistence

- H2 file-backed DB
- Config: `src/main/resources/application.yml`
- URL: `jdbc:h2:file:./data/taskdb`

### Database Tables
- `tasks`: Task entities with title, description, due_date, status
- `todo_lists`: TODO list containers
- `todo_items`: Individual TODO items linked to lists
- `notes`: Timestamped notes with content

All tables use UUID primary keys and track creation/update timestamps.

## Testing Strategy

- Unit tests for service logic
- Integration test with Spring context and in-memory H2
- Controller-level tests for UI orchestration behavior
