# Troubleshooting

## `mvn` Not Recognized

Use full Maven path:

```powershell
& 'C:\ProgramData\chocolatey\lib\maven\apache-maven-3.9.12\bin\mvn.cmd' -v
```

Or add Maven `bin` to PATH.

## Permission Error Writing `%USERPROFILE%\.m2`

Run with local repository:

```powershell
mvn "-Dmaven.repo.local=.m2" clean test
```

## JavaFX Cache Warnings

If you see:

`Can not create cache at C:\Users\...\ .openjfx\cache\...`

The app may still run normally. If needed, ensure write access to user home directory.

## App Starts But Saved Task Seems Missing

The UI now auto-selects saved tasks and clears restrictive filters/search if needed.  
If behavior still looks wrong:

1. Switch filter to `All`
2. Clear search box
3. Click `Refresh`

## Integration Test Fails with `Toolkit not initialized`

Ensure `MainView` remains `@Lazy` so JavaFX controls are not instantiated during Spring test startup.

## Database Reset

To start fresh:

1. Stop app
2. Delete `data/`
3. Relaunch app
