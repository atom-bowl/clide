# Setup Guide

## Prerequisites

- Java 21 or newer
- Maven 3.9+
- Windows, macOS, or Linux with JavaFX dependencies available through Maven

## Verify Tools

```powershell
java -version
mvn -v
```

## Build and Test

```powershell
mvn clean test
```

## Run the App

```powershell
mvn javafx:run
```

If Maven is not in PATH, use its full path:

```powershell
& 'C:\ProgramData\chocolatey\lib\maven\apache-maven-3.9.12\bin\mvn.cmd' javafx:run
```

## Data Location

- Local DB file: `data/taskdb.mv.db`
- Delete `data/` to reset local app data

## Optional: Local Maven Cache in Repo

If you cannot write to `%USERPROFILE%\.m2`, run with:

```powershell
mvn "-Dmaven.repo.local=.m2" clean test
```
