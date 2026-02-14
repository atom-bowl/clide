#!/usr/bin/env pwsh
# Build script to create Clide Windows Installer (requires WiX Toolset)

Write-Host "Building Clide Windows Installer..." -ForegroundColor Green
Write-Host ""

# Check if WiX is installed (supports v3 candle.exe or v4/v5/v6 wix.exe)
$wixV3 = Get-Command candle.exe -ErrorAction SilentlyContinue
$wixV4Plus = Get-Command wix.exe -ErrorAction SilentlyContinue

if (-not $wixV3 -and -not $wixV4Plus) {
    Write-Host "ERROR: WiX Toolset not found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install WiX first:" -ForegroundColor Yellow
    Write-Host "  winget install --id WiXToolset.WiX" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Or see docs/INSTALLER.md for other installation methods" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "After installing, restart your terminal to refresh PATH." -ForegroundColor Yellow
    exit 1
}

if ($wixV4Plus) {
    Write-Host "WiX Toolset v4+ found: $($wixV4Plus.Source)" -ForegroundColor Green
} else {
    Write-Host "WiX Toolset v3 found: $($wixV3.Source)" -ForegroundColor Green
}
Write-Host ""

# Verify pom.xml is configured for installer
$pomContent = Get-Content pom.xml -Raw
if ($pomContent -notmatch '<type>EXE</type>') {
    Write-Host "WARNING: pom.xml may not be configured for installer build!" -ForegroundColor Yellow
    Write-Host "Expected: <type>EXE</type>" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Continue anyway? (Y/N)" -ForegroundColor Cyan
    $response = Read-Host
    if ($response -ne 'Y' -and $response -ne 'y') {
        Write-Host "Build cancelled. See docs/INSTALLER.md for configuration." -ForegroundColor Yellow
        exit 0
    }
}

# Clean and package the application
Write-Host "Step 1: Building JAR with dependencies..." -ForegroundColor Yellow
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}

# Create the Windows installer
Write-Host "Step 2: Creating Windows installer with jpackage..." -ForegroundColor Yellow
mvn jpackage:jpackage

if ($LASTEXITCODE -ne 0) {
    Write-Host "jpackage failed!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Common issues:" -ForegroundColor Yellow
    Write-Host "1. WiX not in PATH - restart terminal after installing WiX" -ForegroundColor Cyan
    Write-Host "2. pom.xml not configured - see docs/INSTALLER.md" -ForegroundColor Cyan
    exit 1
}

Write-Host ""
Write-Host "Build complete!" -ForegroundColor Green
Write-Host "Installer location: app\Clide-1.0.0.exe" -ForegroundColor Cyan
Write-Host ""
Write-Host "Open the installer to install Clide." -ForegroundColor White
Write-Host "Users do NOT need Java installed to run Clide!" -ForegroundColor Green
while ($true) {
    Write-Host "Thank you for using Clide!" -ForegroundColor Red
    Start-Sleep -Milliseconds 200

    Write-Host "Thank you for using Clide!" -ForegroundColor Magenta
    Start-Sleep -Milliseconds 200
}
