# Creating a Windows Installer

This guide explains how to create a single-file Windows installer (.exe) for Clide.

## Prerequisites

### Install WiX Toolset

WiX is required to create Windows installers with jpackage.

**Option 1: Using winget (Recommended)**
```powershell
winget install --id WiXToolset.WiX
```

**Option 2: Manual Download**
1. Go to https://wixtoolset.org/releases/
2. Download WiX Toolset (v3.14 or later recommended)
3. Install and add to PATH
4. Verify installation:
   ```powershell
   where.exe candle
   where.exe light
   ```

**Option 3: Using Chocolatey**
```powershell
choco install wixtoolset
```

After installation, **restart your terminal** to refresh PATH.

## Build Configuration

### 1. Update pom.xml

Change the jpackage type from `APP_IMAGE` to `EXE`:

```xml
<configuration>
    <name>Clide</name>
    <appVersion>1.0.0</appVersion>
    <vendor>Clide</vendor>
    <description>A powerful desktop task manager and productivity suite</description>
    <destination>app</destination>
    <type>EXE</type>  <!-- Changed from APP_IMAGE -->
    <input>target/libs</input>
    <mainJar>${project.artifactId}-${project.version}.jar</mainJar>
    <mainClass>com.example.taskmanager.ApplicationLauncher</mainClass>
    <icon>src/main/resources/icons/clide.ico</icon>
    <javaOptions>
        <option>-Dfile.encoding=UTF-8</option>
        <option>--add-opens</option>
        <option>javafx.graphics/javafx.css=ALL-UNNAMED</option>
    </javaOptions>
    <!-- Add Windows installer options: -->
    <winMenu>true</winMenu>
    <winDirChooser>true</winDirChooser>
    <winShortcut>true</winShortcut>
    <winShortcutPrompt>true</winShortcutPrompt>
    <winMenuGroup>Clide</winMenuGroup>
    <winPerUserInstall>false</winPerUserInstall>
    <winUpgradeUuid>a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d</winUpgradeUuid>
</configuration>
```

### 2. Run the Build

```powershell
.\build-exe.ps1
```

This will create:
- `app\Clide-1.0.0.exe` - Single-file installer

## What the Installer Does

When users run `Clide-1.0.0.exe`, they get:
- **Directory chooser** to select installation location
- **Desktop shortcut** option
- **Start Menu integration** (appears in Windows Start Menu)
- **Automatic JRE bundling** (no Java required on target system)
- **Uninstaller** via Windows Settings → Apps

## Installer vs Portable App

| Feature | Installer (.exe) | Portable App (folder) |
|---------|------------------|----------------------|
| **Distribution** | Single file | Folder to zip |
| **Installation** | Wizard with options | Just extract/copy |
| **Shortcuts** | Auto-created | Manual |
| **Uninstall** | Windows Settings | Delete folder |
| **Updates** | Can upgrade | Replace folder |
| **Size** | ~80-120 MB | ~100-150 MB |

## Troubleshooting

### "Cannot find WiX tools" Error

**Problem:** jpackage can't find candle.exe/light.exe

**Solution:**
1. Verify WiX is in PATH:
   ```powershell
   $env:PATH -split ';' | Select-String -Pattern 'wix'
   ```
2. Add manually if needed:
   ```powershell
   $env:PATH += ";C:\Program Files (x86)\WiX Toolset v3.14\bin"
   ```
3. Restart terminal
4. Try build again

### "Invalid version" Error

**Problem:** Version contains SNAPSHOT or invalid characters

**Solution:** Already fixed in pom.xml (uses hardcoded 1.0.0)

### Build Fails After WiX Install

**Problem:** PATH not refreshed

**Solution:** Close and reopen terminal, then rebuild

## Distribution

After building the installer:

1. **Test it locally:**
   ```powershell
   .\app\Clide-1.0.0.exe
   ```

2. **Share the file:**
   - Upload to GitHub Releases
   - Share via cloud storage
   - Distribute on USB drives

3. **Version management:**
   - Update version in pom.xml
   - Update winUpgradeUuid if breaking changes
   - Rebuild for new version

## Reverting to Portable App

To go back to portable app mode:

1. Change `<type>EXE</type>` back to `<type>APP_IMAGE</type>`
2. Remove Windows installer options (winMenu, etc.)
3. Rebuild with `.\build-exe.ps1`

This creates the folder-based app at `app\Clide\Clide.exe`
