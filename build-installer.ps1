# KeyboardShadow - Tạo EXE Installer với jpackage
# Chạy script này sau khi đã chạy `mvn clean package -DskipTests`

param(
    [string]$AppVersion = "1.0.0"
)

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  KeyboardShadow - jpackage Installer" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Kiểm tra jpackage
Write-Host "[Step 1] Kiểm tra jpackage..." -ForegroundColor Yellow
$jpackagePath = Get-Command jpackage -ErrorAction SilentlyContinue
if (-not $jpackagePath) {
    Write-Host "❌ Error: jpackage không tìm thấy!" -ForegroundColor Red
    Write-Host "    jpackage cần Java JDK 17+ (không phải JRE)" -ForegroundColor Yellow
    Write-Host "    Hãy cài đặt từ: https://adoptium.net/ hoặc https://www.oracle.com/" -ForegroundColor Yellow
    exit 1
}
Write-Host "✓ jpackage tìm thấy: $($jpackagePath.Source)" -ForegroundColor Green

# Kiểm tra JAR file
Write-Host ""
Write-Host "[Step 2] Kiểm tra JAR file..." -ForegroundColor Yellow
$jarPath = "target\KeyboardShadow-$AppVersion.jar"
if (-not (Test-Path $jarPath)) {
    Write-Host "❌ Error: File $jarPath không tìm thấy!" -ForegroundColor Red
    Write-Host "    Hãy chạy: mvn clean package -DskipTests" -ForegroundColor Yellow
    exit 1
}
Write-Host "✓ JAR file tìm thấy: $jarPath" -ForegroundColor Green
Write-Host "  Dung lượng: $([math]::Round((Get-Item $jarPath).Length/1MB, 2)) MB" -ForegroundColor Gray

# Kiểm tra JAVA_HOME
Write-Host ""
Write-Host "[Step 3] Kiểm tra JAVA_HOME..." -ForegroundColor Yellow
if (-not $env:JAVA_HOME) {
    Write-Host "⚠ JAVA_HOME chưa được set" -ForegroundColor Yellow
    # Thử tìm tự động từ jpackage command path
    $jpackageFullPath = (Get-Command jpackage).Source
    $env:JAVA_HOME = $jpackageFullPath.Replace("\bin\jpackage.exe", "")
    Write-Host "  Tìm thấy JDK: $env:JAVA_HOME" -ForegroundColor Cyan
} else {
    Write-Host "✓ JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Green
}

# Kiểm tra Icon
Write-Host ""
Write-Host "[Step 4] Kiểm tra Icon..." -ForegroundColor Yellow
$iconPath = "src\main\resources\icons\KeyboardDisplay.ico"
if (Test-Path $iconPath) {
    Write-Host "✓ Icon tìm thấy: $iconPath" -ForegroundColor Green
    $useIcon = $true
} else {
    Write-Host "⚠ Icon không tìm thấy (tùy chọn)" -ForegroundColor Yellow
    $useIcon = $false
}

# Tạo thư mục output
Write-Host ""
Write-Host "[Step 5] Chuẩn bị thư mục output..." -ForegroundColor Yellow
$installerDir = "target\installer"
if (Test-Path $installerDir) {
    Remove-Item $installerDir -Recurse -Force
}
New-Item -ItemType Directory -Path $installerDir -Force | Out-Null
Write-Host "✓ Thư mục: $installerDir" -ForegroundColor Green

# Xây dựng lệnh jpackage
Write-Host ""
Write-Host "[Step 6] Chạy jpackage..." -ForegroundColor Yellow

$jpackageArgs = @(
    "--type", "exe",
    "--name", "KeyboardShadow",
    "--app-version", $AppVersion,
    "--vendor", "NguyenNhatCuong",
    "--description", "Real-time keyboard display for streaming and content creation",
    "--input", "target",
    "--main-jar", "KeyboardShadow-$AppVersion.jar",
    "--main-class", "com.keyboarddisplay.AppLauncher",
    "--dest", $installerDir,
    "--win-shortcut",
    "--win-menu",
    "--win-dir-chooser",
    "--win-console"
)

# Thêm icon nếu có
if ($useIcon) {
    $jpackageArgs += "--icon"
    $jpackageArgs += $iconPath
}

Write-Host "Lệnh: jpackage $($jpackageArgs -join ' ')" -ForegroundColor Gray
Write-Host ""

# Chạy jpackage
& jpackage @jpackageArgs

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "  ✅ SUCCESS!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""

    # Liệt kê file được tạo
    Write-Host "📦 File được tạo:" -ForegroundColor Cyan
    $exeFiles = Get-ChildItem $installerDir -Filter "*.exe"
    foreach ($exeFile in $exeFiles) {
        $sizeInMB = [math]::Round($exeFile.Length/1MB, 2)
        Write-Host "  ✓ $($exeFile.Name)" -ForegroundColor Green
        Write-Host "    Dung lượng: $sizeInMB MB" -ForegroundColor Gray
        Write-Host "    Đường dẫn: $($exeFile.FullName)" -ForegroundColor Gray
    }

    Write-Host ""
    Write-Host "📋 Thông tin Ứng dụng:" -ForegroundColor Cyan
    Write-Host "  Name: KeyboardShadow" -ForegroundColor White
    Write-Host "  Developer: NguyenNhatCuong" -ForegroundColor White
    Write-Host "  Version: $AppVersion" -ForegroundColor White

    Write-Host ""
    Write-Host "💾 Các tệp khác:" -ForegroundColor Cyan
    Write-Host "  ZIP Portable: target\KeyboardShadow-$AppVersion.zip" -ForegroundColor White
    Write-Host "  JAR Shaded: target\KeyboardShadow-$AppVersion.jar" -ForegroundColor White

    Write-Host ""
    Write-Host "📂 Mở thư mục output..." -ForegroundColor Yellow
    Start-Process explorer.exe $installerDir

} else {
    Write-Host ""
    Write-Host "❌ FAILED! Error code: $LASTEXITCODE" -ForegroundColor Red
    Write-Host ""
    Write-Host "Khắc phục sự cố:" -ForegroundColor Yellow
    Write-Host "  1. Đảm bảo sử dụng JDK 17+, không phải JRE" -ForegroundColor White
    Write-Host "  2. Đảm bảo JAVA_HOME được set đúng" -ForegroundColor White
    Write-Host "  3. Thử set JAVA_HOME thủ công:" -ForegroundColor White
    Write-Host "     `$env:JAVA_HOME = 'C:\Program Files\jdk-17'" -ForegroundColor Cyan
    Write-Host "  4. Kiểm tra icon file tồn tại" -ForegroundColor White
    exit 1
}

