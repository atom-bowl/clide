# Testing Guide

## Test Types

- `service/TaskServiceImplTest`: unit tests for service behavior
- `integration/TaskServiceIntegrationTest`: Spring + JPA integration path
- `ui/MainControllerTest`: UI controller delegation logic
- `search/TaskSearchTest`: Kotlin search ranking behavior

## Run All Tests

```powershell
mvn clean test
```

## Run a Single Test Class

```powershell
mvn -Dtest=TaskServiceImplTest test
```

## Expected Green Baseline

- 10 tests run
- 0 failures
- 0 errors

## Common Test Env Notes

- Integration tests use `src/test/resources/application-test.yml`
- `MainView` is lazy-loaded to avoid JavaFX toolkit errors in Spring tests
- If Maven cannot write to user `.m2`, use:

```powershell
mvn "-Dmaven.repo.local=.m2" test
```
