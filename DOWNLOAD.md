# Download & Run Clide

## Easiest Way to Use Clide

No installation, no Java required, no setup - just download and run!

### Steps:

1. **Download the app folder**
   - Clone this repository OR
   - Download as ZIP and extract

2. **Navigate to the app**
   - Go to the `app/Clide/` folder

3. **Run the application**
   - Double-click `Clide.exe` OR
   - From terminal: `.\app\Clide\Clide.exe`

4. **Start using Clide!**
   - Your data saves automatically in `data/taskdb.mv.db`
   - Everything works offline

### What's Included

- ✅ **Java Runtime** - Bundled, no separate install needed
- ✅ **All Dependencies** - JavaFX, Spring Boot, H2 database
- ✅ **Custom Icon** - Professional blue gradient design
- ✅ **4 Productivity Tools**:
  - Tasks with due dates and search
  - Calendar view of your tasks
  - Multiple TODO lists
  - Notes & journaling

### System Requirements

- **OS**: Windows 10 or later
- **Disk Space**: ~150MB
- **RAM**: 256MB minimum
- **No Internet Required** - Works completely offline

### Moving or Sharing

**Portable Installation:**
- Copy the entire `app/Clide/` folder anywhere
- Copy to USB drive for portable use
- Share the folder with others (zip it first)

**Important Files:**
- `Clide.exe` - Main application launcher
- `bin/` - Java runtime files
- `lib/` - Application libraries
- `app/` - Application JAR files

### Data Location

When you run Clide, it creates:
- `data/taskdb.mv.db` - Your tasks, lists, and notes

**To backup your data:**
```powershell
# Copy the database file
copy data\taskdb.mv.db backup\taskdb-backup.mv.db
```

**To reset/start fresh:**
```powershell
# Delete the database (app will create new on next run)
del data\taskdb.mv.db
```

### Troubleshooting

**"Windows protected your PC" message:**
- Click "More info"
- Click "Run anyway"
- This happens because the app isn't digitally signed (yet)

**App won't start:**
- Make sure you extracted the ENTIRE `app/Clide/` folder
- Don't move just `Clide.exe` alone - it needs the other folders

**Missing icon:**
- Icon may take a moment to appear in Windows
- Try creating a shortcut (right-click Clide.exe → Create shortcut)

### For Developers

Want to build from source or create an installer? See:
- [README.md](README.md) - Full documentation
- [CLAUDE.md](CLAUDE.md) - Development guide
- [docs/INSTALLER.md](docs/INSTALLER.md) - Create Windows installer

---

**That's it!** Enjoy using Clide for all your productivity needs.
