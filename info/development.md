# Development Guide

## Useful Commands

### Run and Test
```powershell
mvn clean test       # Run all tests
mvn test            # Run tests without clean
mvn javafx:run      # Launch the application
```

### Build Windows Application
```powershell
.\build-exe.ps1     # Build portable app at app/Clide/
```

### Icon Generation
```powershell
pip install Pillow  # First time only
python create-icon.py  # Generate clide.ico
```

Use project-local Maven cache if needed:
```powershell
mvn "-Dmaven.repo.local=.m2" clean test
```

## Feature Development Pattern

1. Define/adjust DTO commands in `dto/`
2. Implement logic in `service/`
3. Add/adjust repository methods in `repo/`
4. Wire UI behavior in `ui/`
5. Add tests at unit/integration level

## Kotlin Interop Notes

- Kotlin helper lives in `src/main/kotlin/com/example/taskmanager/search/TaskSearch.kt`
- Java uses it via `TaskSearch.filterAndRank(...)`
- Keep Kotlin utilities focused and side-effect free

## Coding Conventions

- Keep service methods transactional and validation-aware
- Keep UI exception handling user-readable
- Prefer DTOs over passing entities into UI
- Keep repository methods deterministic with explicit ordering

## Adding a New Field to Task

1. Update `Task.java` entity
2. Update `TaskDto`, create/update commands
3. Update service mapping in `TaskServiceImpl`
4. Update UI form and table in `TasksView`
5. Add tests for create/update/filter impacts

## Adding a New Tab

1. Create new view component in `ui/` (e.g., `MyFeatureView.java`)
2. Add domain entities if needed in `domain/`
3. Create repository in `repo/`
4. Implement service in `service/`
5. Add tab to `MainView.java`:
   ```java
   Tab myTab = new Tab("My Feature", myFeatureView.getRoot());
   tabPane.getTabs().add(myTab);
   ```
6. Add refresh logic in tab selection listener
7. Update `info/usage.md` with usage instructions
8. Update `info/architecture.md` with architecture details

## Working with Existing Tabs

### Tasks Tab (`TasksView`)
- Main task CRUD operations
- Uses `MainController` for business logic
- Integrates with `TaskSearch.kt` for search ranking

### Calendar Tab (`CalendarView`)
- Read-only view of tasks by due date
- Loads data via `MainController`
- Visual calendar grid generation

### TODO Lists Tab (`TodoListsView`)
- Manages `TodoList` and `TodoItem` entities
- Direct service calls to `TodoListService`
- Bidirectional parent-child relationship

### Notes Tab (`NotesView`)
- Simple CRUD for `Note` entities
- Direct service calls to `NoteService`
- Timestamp-based sorting
