# Download & Install Clide

## Recommended (Windows Installer)

Use the installer build:

1. Download `app\Clide-1.1.0.exe`
2. Run the installer
3. Launch Clide from Start Menu or desktop shortcut

No separate Java installation is required.

## System Requirements

- Windows 10 or later
- ~200 MB free disk space

## Upgrading

- Run the newer installer (for example `Clide-1.1.0.exe` -> next release)
- Keep the same upgrade UUID in installer config for in-place upgrades

## Troubleshooting

- If Windows SmartScreen appears, click `More info` then `Run anyway`.
- If the installer exits immediately, confirm WiX is installed when building and rebuild with `.\build-installer.ps1`.
- Installer build guide: `info/INSTALLER.md`

## Data Location

Clide stores data locally in `data/taskdb.mv.db`.
