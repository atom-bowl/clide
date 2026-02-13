# Development Guide

## Useful Commands

```powershell
mvn clean test
mvn test
mvn javafx:run
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

1. Update `Task.java`
2. Update `TaskDto`, create/update commands
3. Update service mapping in `TaskServiceImpl`
4. Update UI form and table
5. Add tests for create/update/filter impacts
