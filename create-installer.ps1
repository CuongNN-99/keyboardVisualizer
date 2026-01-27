# Simple Installer Creator for Keyboard Display
# Run this in PowerShell from project root directory

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  Keyboard Display Installer" -ForegroundColor Cyan  
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Build JAR
Write-Host "[Step 1/2] Building JAR with Maven..." -ForegroundColor Yellow
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}

Write-Host "JAR built successfully!" -ForegroundColor Green
Write-Host ""

# Step 2: Create EXE with jpackage
Write-Host "[Step 2/2] Creating Windows installer..." -ForegroundColor Yellow

# Lấy đường dẫn JDK từ JAVA_HOME để đảm bảo có Runtime
$javaHome = $env:JAVA_HOME
if (-not $javaHome) {
    # Nếu không có biến môi trường, thử tìm qua lệnh jpackage
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

# Build arguments array
$jpackageArgs = @(
    "--type", "exe",
    "--name", "KeyboardDisplay",
    "--app-version", "1.0.0",
    "--vendor", "Keyboard Display",
    "--description", "Real-time keyboard display for streaming and tutorials",
    "--input", "target", # Thư mục chứa file jar đã build
    "--main-jar", "keyboard-display-1.0.0.jar",
    "--main-class", "com.keyboarddisplay.AppLauncher",
    "--dest", $installerDir,
    "--win-shortcut",
    "--win-menu",
    "--win-dir-chooser",
    
    "--verbose",                # Bật log chi tiết để debug
    "--runtime-image", $javaHome # QUAN TRỌNG: Đóng gói toàn bộ JRE vào installer
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
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host "  SUCCESS!" -ForegroundColor Green
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Installer created at: $installerDir" -ForegroundColor Cyan
    
    # List created files
    $files = Get-ChildItem $installerDir -File
    foreach ($file in $files) {
        Write-Host "  - $($file.Name) ($([math]::Round($file.Length/1MB, 2)) MB)" -ForegroundColor White
    }
    
    # Mở thư mục để cài đặt thử
    Start-Process explorer.exe $installerDir
    
} else {
    Write-Host ""
    Write-Host "FAILED! Error code: $LASTEXITCODE" -ForegroundColor Red
}