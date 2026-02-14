# Clide: Quick Working Notes

## What this app is
- Desktop task manager built with JavaFX + Spring Boot.
- No HTTP API; Spring runs in-process for DI/services/persistence.
- Data is stored in local H2 file DB at `data/taskdb.mv.db`.

## Tech stack
- Java 21+ (project tested on newer runtimes too)
- Maven
- Spring Boot 3.4.x
- Spring Data JPA + H2
- Kotlin helper for search ranking

## Run and test
```powershell
mvn clean test
mvn javafx:run
```

If local Maven cache permissions are restricted:
```powershell
mvn "-Dmaven.repo.local=.m2" clean test
```

## Build Windows application
```powershell
.\build-exe.ps1
```

This creates a portable Windows app at `app/Clide/Clide.exe` that bundles the JRE and all dependencies.

Run directly: `.\app\Clide\Clide.exe`

### To create a Windows installer (.exe) instead:
See `docs/INSTALLER.md` for complete guide. Quick steps:
1. Install WiX: `winget install --id WiXToolset.WiX`
2. Configure pom.xml (type=EXE, add Windows options)
3. Run: `.\build-installer.ps1`

This creates a professional installer at `app\Clide-1.0.0.exe`

## Main code map
- `src/main/java/com/example/taskmanager/ui` - JavaFX view/controller wiring
- `src/main/java/com/example/taskmanager/service` - business logic
- `src/main/java/com/example/taskmanager/repo` - JPA repository
- `src/main/java/com/example/taskmanager/domain` - entities/enums
- `src/main/java/com/example/taskmanager/dto` - service boundary DTOs
- `src/main/kotlin/com/example/taskmanager/search/TaskSearch.kt` - filtering/ranking

## Typical change flow
1. Update `domain` and `dto` when shape changes.
2. Implement logic/mapping in `TaskServiceImpl`.
3. Adjust repository query methods as needed.
4. Wire UI behavior in `MainController` / `MainView`.
5. Add or update tests.

## Test locations
- `src/test/java/com/example/taskmanager/service/TaskServiceImplTest`
- `src/test/java/com/example/taskmanager/integration/TaskServiceIntegrationTest`
- `src/test/java/com/example/taskmanager/ui/MainControllerTest`
- `src/test/java/com/example/taskmanager/search/TaskSearchTest`

## Completed Features
- ✓ Desktop .exe app (jpackage configured, use `build-exe.ps1`)
- ✓ Replace all Task Manager references with Clide (UI + branding complete)
- ✓ Top navigation tabs for different sections
- ✓ Calendar view for tasks
- ✓ Integrated TODO lists
- ✓ Self-chat/notes feature
- ✓ Custom application icon

## Future Enhancements
- Advanced task filtering and sorting
- Data export/import
- Task categories and tags
- Dark mode theme
- Keyboard shortcuts
- Task reminders/notifications