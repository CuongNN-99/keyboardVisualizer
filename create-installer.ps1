# KeyboardShadow Installer Creator
# Run this in PowerShell from project root directory

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  KeyboardShadow Installer Creator" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Build JAR
Write-Host "[Step 1/3] Building JAR with Maven..." -ForegroundColor Yellow
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}

Write-Host "JAR built successfully!" -ForegroundColor Green
Write-Host ""

# Step 2: Create EXE/MSI with jpackage
Write-Host "[Step 2/3] Creating Windows installer (.exe)..." -ForegroundColor Yellow

# Get JDK path from JAVA_HOME
$javaHome = $env:JAVA_HOME
if (-not $javaHome) {
    $jpackagePath = (Get-Command jpackage).Source
    $javaHome = $jpackagePath.Replace("\bin\jpackage.exe", "")
}

Write-Host "Using JDK from: $javaHome" -ForegroundColor Gray

# Create installer directory
$installerDir = "target\installer"
if (Test-Path $installerDir) {
    Remove-Item $installerDir -Recurse -Force
}
New-Item -ItemType Directory -Path $installerDir -Force | Out-Null

# Icon path
$iconPath = "src\main\resources\icons\KeyboardDisplay.ico"
$iconExists = Test-Path $iconPath

# Build arguments array for EXE installer
$jpackageArgs = @(
    "--type", "exe",
    "--name", "KeyboardShadow",
    "--app-version", "1.0.0",
    "--vendor", "NguyenNhatCuong",
    "--description", "Real-time keyboard display for streaming and content creation",
    "--input", "target",
    "--main-jar", "KeyboardShadow-1.0.0.jar",
    "--main-class", "com.keyboarddisplay.AppLauncher",
    "--dest", $installerDir,
    "--win-shortcut",
    "--win-menu",
    "--win-dir-chooser",
    "--win-console",
    "--verbose",
    "--runtime-image", $javaHome
)

# Add icon if exists
if ($iconExists) {
    $jpackageArgs += "--icon"
    $jpackageArgs += $iconPath
    Write-Host "Using icon: $iconPath" -ForegroundColor Cyan
}

Write-Host "Running jpackage..." -ForegroundColor Gray
Write-Host "Note: Bundling full JRE, the installer size should be > 150MB..." -ForegroundColor Yellow
Write-Host ""

# Execute jpackage
& jpackage @jpackageArgs

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✓ EXE installer created successfully!" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "FAILED! Error code: $LASTEXITCODE" -ForegroundColor Red
    exit 1
}

# Step 3: Create ZIP portable version
Write-Host ""
Write-Host "[Step 3/3] Creating Portable ZIP version..." -ForegroundColor Yellow
mvn assembly:single -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host "  SUCCESS!" -ForegroundColor Green
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host ""

    # Create build-output directory
    $outputDir = "build-output"
    if (!(Test-Path $outputDir)) {
        New-Item -ItemType Directory -Path $outputDir | Out-Null
    }

    # Copy files to build-output
    Write-Host "Copying files to build-output..." -ForegroundColor Cyan

    # Copy EXE installer
    $exeFile = Get-ChildItem $installerDir -Filter "*.exe" | Select-Object -First 1
    if ($exeFile) {
        Copy-Item $exeFile.FullName "$outputDir/$($exeFile.Name)" -Force
        Write-Host "✓ EXE Installer: $outputDir/$($exeFile.Name)" -ForegroundColor Green
    }

    # Copy ZIP portable
    $zipFile = Get-ChildItem "target" -Filter "KeyboardShadow-*.zip" | Select-Object -First 1
    if ($zipFile) {
        Copy-Item $zipFile.FullName "$outputDir/KeyboardShadow-1.0.0-portable.zip" -Force
        Write-Host "✓ Portable ZIP: $outputDir/KeyboardShadow-1.0.0-portable.zip" -ForegroundColor Green
    }
    
    Write-Host ""
    Write-Host "Application Details:" -ForegroundColor Cyan
    Write-Host "  Name: KeyboardShadow" -ForegroundColor White
    Write-Host "  Developer: NguyenNhatCuong" -ForegroundColor White
    Write-Host "  Version: 1.0.0" -ForegroundColor White
    Write-Host ""
    Write-Host "Output folder: $outputDir" -ForegroundColor Yellow

    # Open output folder
    Start-Process explorer.exe $outputDir

} else {
    Write-Host ""
    Write-Host "FAILED! Error code: $LASTEXITCODE" -ForegroundColor Red
}