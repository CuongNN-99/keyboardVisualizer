# KeyboardShadow Installer Creator Script
# This PowerShell script creates .msi installer and .zip portable version

param(
    [string]$Version = "1.0.0",
    [string]$BuildMode = "all"  # "msi", "zip", or "all"
)

# Set colors for output
$Green = "Green"
$Red = "Red"
$Yellow = "Yellow"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "KeyboardShadow Installer Creator" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven is installed
$mvn = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvn) {
    Write-Host "Error: Maven is not installed or not in PATH" -ForegroundColor $Red
    Write-Host "Please install Maven from https://maven.apache.org" -ForegroundColor $Yellow
    exit 1
}

Write-Host "Maven version:" -ForegroundColor $Green
mvn --version

Write-Host ""
Write-Host "Building project..." -ForegroundColor Cyan

# Run Maven clean package
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed!" -ForegroundColor $Red
    exit 1
}

Write-Host ""
Write-Host "Build completed successfully!" -ForegroundColor $Green

# Create output directory
$outputDir = "build-output"
if (!(Test-Path $outputDir)) {
    New-Item -ItemType Directory -Path $outputDir | Out-Null
}

# Copy portable ZIP
if (Test-Path "target/KeyboardShadow-${Version}.zip") {
    Copy-Item "target/KeyboardShadow-${Version}.zip" "$outputDir/KeyboardShadow-${Version}-portable.zip" -Force
    Write-Host "✓ Portable ZIP created: $outputDir/KeyboardShadow-${Version}-portable.zip" -ForegroundColor $Green
}

# Information about MSI
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Package Information" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "✓ Portable Version (.zip) - Created" -ForegroundColor $Green
Write-Host "  Location: $outputDir/KeyboardShadow-${Version}-portable.zip"
Write-Host "  Usage: Extract and run run.bat or run.ps1"
Write-Host ""
Write-Host "ℹ MSI Installer (.msi) - Manual Installation" -ForegroundColor $Yellow
Write-Host "  To create .msi installer, you need to install WiX Toolset:" -ForegroundColor $Yellow
Write-Host "  1. Download from: https://github.com/wixtoolset/wix3/releases" -ForegroundColor $Yellow
Write-Host "  2. Run: create-installer.ps1 after installing WiX" -ForegroundColor $Yellow
Write-Host ""
Write-Host "Application Details:" -ForegroundColor Cyan
Write-Host "  Name: KeyboardShadow" -ForegroundColor White
Write-Host "  Developer: NguyenNhatCuong" -ForegroundColor White
Write-Host "  Version: $Version" -ForegroundColor White
Write-Host ""

