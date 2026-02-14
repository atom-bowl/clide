#!/usr/bin/env pwsh
# Build script to create Clide Windows MSI installer (requires WiX Toolset)

Write-Host "Building Clide Windows MSI Installer..." -ForegroundColor Green
Write-Host ""

[xml]$pomXml = Get-Content pom.xml
$projectVersion = $pomXml.project.version

$wixV3 = Get-Command candle.exe -ErrorAction SilentlyContinue
$wixV4Plus = Get-Command wix.exe -ErrorAction SilentlyContinue

if (-not $wixV3 -and -not $wixV4Plus) {
    Write-Host "ERROR: WiX Toolset not found!" -ForegroundColor Red
    Write-Host "Install WiX first: winget install --id WiXToolset.WiX" -ForegroundColor Yellow
    exit 1
}

Write-Host "Step 1: Building JAR with dependencies..." -ForegroundColor Yellow
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}

Write-Host "Step 2: Creating MSI installer with jpackage..." -ForegroundColor Yellow
mvn "jpackage:jpackage" "-Djpackage.type=MSI"
if ($LASTEXITCODE -ne 0) {
    Write-Host "MSI packaging failed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Build complete!" -ForegroundColor Green
Write-Host "Installer location: app\Clide-$projectVersion.msi" -ForegroundColor Cyan
