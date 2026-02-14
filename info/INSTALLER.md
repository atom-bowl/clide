# Creating a Windows Installer

**NOTE:** The current build uses APP_IMAGE (portable app) instead of EXE installer due to JRE bundling issues with Java 24 and WiX. The portable app works perfectly - users just download the `app/Clide/` folder and run `Clide.exe`.

This guide explains how to create a single-file Windows installer (.exe) for Clide if needed in the future.

## Prerequisites

### Install WiX Toolset

WiX is required to create Windows installers with jpackage. Both WiX v3 and v4/v5/v6 are supported.

**Option 1: Using winget (Recommended)**
```powershell
winget install --id WiXToolset.WiX
```

This installs the latest version (currently v6.x).

**Option 2: Manual Download**
1. Go to https://wixtoolset.org/releases/
2. Download WiX Toolset
   - **WiX v6.x**: Modern version (recommended for new projects)
   - **WiX v3.14**: Legacy version (still widely used)
3. Install and add to PATH
4. Verify installation:
   ```powershell
   # For WiX v4/v5/v6
   where.exe wix

   # For WiX v3
   where.exe candle
   where.exe light
   ```

**Option 3: Using Chocolatey**
```powershell
# Latest version (v6.x)
choco install wixtoolset

# Or specific WiX v3 if needed
choco install wixtoolset --version=3.14
```

**IMPORTANT:** After installation, **restart your terminal** to refresh PATH.

### Version Compatibility

- **WiX v3.x**: Uses `candle.exe` and `light.exe` - fully supported
- **WiX v4/v5/v6**: Uses unified `wix.exe` - fully supported
- jpackage automatically detects and works with both versions

## Build Configuration

### 1. Update pom.xml

Change the jpackage type from `APP_IMAGE` to `EXE`:

```xml
<configuration>
    <name>Clide</name>
    <appVersion>1.1.0</appVersion>
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
- `app\Clide-1.1.0.exe` - Single-file installer

## What the Installer Does

When users run `Clide-1.1.0.exe`, they get:
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

**Problem:** jpackage can't find WiX executables

**Solution:**
1. Verify WiX is in PATH:
   ```powershell
   # Check for any version
   Get-Command wix.exe -ErrorAction SilentlyContinue
   Get-Command candle.exe -ErrorAction SilentlyContinue

   # Or search PATH
   $env:PATH -split ';' | Select-String -Pattern 'wix'
   ```

2. Add manually if needed:
   ```powershell
   # For WiX v6 (typical installation path)
   $env:PATH += ";C:\Program Files\dotnet\tools"

   # For WiX v3 (typical installation path)
   $env:PATH += ";C:\Program Files (x86)\WiX Toolset v3.14\bin"
   ```

3. **Restart your terminal** (most important step!)

4. Try build again:
   ```powershell
   .\build-installer.ps1
   ```

**Note:** WiX v6 installs as a .NET tool, so it may be in `%USERPROFILE%\.dotnet\tools` or `C:\Program Files\dotnet\tools`

### "Invalid version" Error

**Problem:** Version contains SNAPSHOT or invalid characters

**Solution:** Use a numeric semantic version (for example: `1.1.0`)

### Build Fails After WiX Install

**Problem:** PATH not refreshed

**Solution:** Close and reopen terminal, then rebuild

## Distribution

After building the installer:

1. **Test it locally:**
   ```powershell
   .\app\Clide-1.1.0.exe
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
