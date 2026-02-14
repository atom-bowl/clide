#!/usr/bin/env pwsh
# Build script to create Clide Windows application

Write-Host "Building Clide Windows Application..." -ForegroundColor Green

# Clean and package the application
Write-Host "Step 1: Building JAR with dependencies..." -ForegroundColor Yellow
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}

# Create the Windows app
Write-Host "Step 2: Creating Windows app with jpackage..." -ForegroundColor Yellow
mvn jpackage:jpackage

if ($LASTEXITCODE -ne 0) {
    Write-Host "jpackage failed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Build complete!" -ForegroundColor Green
Write-Host "Application location: app\Clide\" -ForegroundColor Cyan
Write-Host ""
Write-Host "Run app\Clide\Clide.exe to start the application." -ForegroundColor White
Write-Host "Note: To create an installer (.exe), install WiX Toolset and change type to EXE in pom.xml" -ForegroundColor Yellow
