# Application Icon

## Quick Start

1. **Use the template SVG:**
   - Open `clide-template.svg` in your browser or image editor
   - Customize colors/design if desired
   - Export/save as PNG (256x256 or larger)

2. **Convert to .ico:**
   - Go to https://convertio.co/png-ico/ or https://www.icoconverter.com/
   - Upload your PNG
   - Download as `clide.ico`
   - Place it in this directory

3. **Enable in build:**
   - Open `pom.xml`
   - Find the jpackage-maven-plugin section
   - Uncomment the line: `<icon>src/main/resources/icons/clide.ico</icon>`
   - Rebuild: `.\build-exe.ps1`

## What the icon is used for:
- Application executable
- Desktop shortcuts
- Taskbar/Start Menu
- Windows app image

**Note:** Without a custom icon, jpackage uses the default Java coffee cup icon.
