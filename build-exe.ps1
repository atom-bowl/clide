#!/usr/bin/env pwsh
# Build script to create Clide Windows application

Write-Host "Building Clide Windows Application..." -ForegroundColor Green

[xml]$pomXml = Get-Content pom.xml
$projectVersion = $pomXml.project.version

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
Write-Host "Application artifact: app\Clide-$projectVersion.exe" -ForegroundColor Cyan
Write-Host ""
Write-Host "Run the installer to install and launch Clide." -ForegroundColor White
