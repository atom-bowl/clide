# Task Manager Desktop

A JavaFX desktop task manager built with Spring Boot, JPA, H2, and Kotlin-assisted search ranking.

## Features

- Create, update, delete tasks
- Toggle complete/incomplete status
- Filter tasks by `All`, `Active`, and `Completed`
- Search tasks with ranked matching (title prioritized over description)
- Persist data locally in H2 (`data/taskdb`)

## Stack

- Java 21+ (tested on Java 24 runtime)
- Maven
- JavaFX
- Spring Boot 3.4.x
- Spring Data JPA + H2
- Kotlin (search helper module)

## Quick Start

1. Install Java and Maven.
2. From repo root, run:

```powershell
mvn clean test
mvn javafx:run
```

If `mvn` is not on your PATH, use:

```powershell
& 'C:\ProgramData\chocolatey\lib\maven\apache-maven-3.9.12\bin\mvn.cmd' clean test
& 'C:\ProgramData\chocolatey\lib\maven\apache-maven-3.9.12\bin\mvn.cmd' javafx:run
```

## Project Layout

```text
src/main/java/com/example/taskmanager
  ApplicationLauncher.java
  TaskManagerApplication.java
  domain/
  dto/
  repo/
  service/
  ui/
src/main/kotlin/com/example/taskmanager/search
  TaskSearch.kt
src/test/java/com/example/taskmanager
  integration/
  search/
  service/
  ui/
src/main/resources/application.yml
src/test/resources/application-test.yml
```

## Documentation

See `info/README.md` for a full set of guides:

- Setup and prerequisites
- End-user usage
- Development workflow
- Architecture
- Testing
- Troubleshooting

## Notes

- The app stores tasks in `data/taskdb.mv.db`.
- The first run may download dependencies and take longer.
- On some locked-down environments, JavaFX may log cache warnings; app behavior is usually unaffected.
