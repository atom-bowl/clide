# Simple PowerShell script to download a basic icon
# Uses a free icon from a public source

$iconUrl = "https://raw.githubusercontent.com/microsoft/fluentui-emoji/main/assets/Card%20index/3D/card_index_3d.png"
$outputPath = "clide-temp.png"

Write-Host "Downloading temporary icon..." -ForegroundColor Yellow
try {
    Invoke-WebRequest -Uri $iconUrl -OutFile $outputPath -ErrorAction Stop
    Write-Host "Downloaded icon to: $outputPath" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Cyan
    Write-Host "1. Convert PNG to ICO at: https://convertio.co/png-ico/"
    Write-Host "2. Save as clide.ico in this directory"
    Write-Host "3. Delete $outputPath"
    Write-Host "4. Uncomment icon line in pom.xml"
} catch {
    Write-Host "Download failed. Please create icon manually:" -ForegroundColor Red
    Write-Host "1. Create a 256x256 image with 'C' or your design"
    Write-Host "2. Convert to .ico at https://convertio.co/png-ico/"
    Write-Host "3. Save as clide.ico in this directory"
}
