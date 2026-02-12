# 📦 KeyboardShadow - Hướng dẫn Đóng gói & Cài đặt

## 📋 Yêu cầu hệ thống

- **Java Development Kit (JDK)**: Phiên bản 17 hoặc cao hơn
  - Download: https://adoptium.net/ hoặc https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
  
- **Maven**: Phiên bản 3.6.0 hoặc cao hơn
  - Download: https://maven.apache.org/download.cgi
  
- **Biến môi trường**:
  - `JAVA_HOME` phải trỏ đến thư mục cài đặt JDK
  - Maven phải có trong biến môi trường `PATH`

### Kiểm tra cài đặt

```powershell
# Kiểm tra Java
java -version

# Kiểm tra Maven
mvn --version
```

---

## 🚀 Tùy chọn 1: Build & Tạo cả 2 gói (.exe installer + .zip portable)

### Bước nhanh nhất

```powershell
# Mở PowerShell tại thư mục gốc của dự án
cd D:\keyboardVisualizer\keyboardVisualClaude\KeyboardDisplay

# Chạy script tự động
.\create-installer.ps1
```

**Kết quả:**
- `target\installer\KeyboardShadow-1.0.0.exe` - Installer cho Windows (bundled JRE)
- `build-output\KeyboardShadow-1.0.0-portable.zip` - Bản Portable (giải nén dùng ngay)

---

## 🔧 Tùy chọn 2: Build từng bước (chi tiết)

### Step 1: Xóa bản build cũ
```powershell
mvn clean
```

### Step 2: Biên dịch & tạo JAR
```powershell
mvn package -DskipTests
```

**Output:**
- `target\KeyboardShadow-1.0.0.jar` - File JAR chính (shaded, đã gộp dependencies)
- `target\KeyboardShadow-1.0.0.zip` - File ZIP portable

### Step 3: Tạo Installer EXE với jpackage

```powershell
$jpackageArgs = @(
    "--type", "exe",
    "--name", "KeyboardShadow",
    "--app-version", "1.0.0",
    "--vendor", "NguyenNhatCuong",
    "--description", "Real-time keyboard display for streaming",
    "--input", "target",
    "--main-jar", "KeyboardShadow-1.0.0.jar",
    "--main-class", "com.keyboarddisplay.AppLauncher",
    "--dest", "target\installer",
    "--win-shortcut",
    "--win-menu",
    "--win-dir-chooser",
    "--icon", "src\main\resources\icons\KeyboardDisplay.ico",
    "--runtime-image", $env:JAVA_HOME
)

jpackage @jpackageArgs
```

### Step 4: Tổ chức Output

```powershell
# Tạo thư mục output
New-Item -ItemType Directory -Path "build-output" -Force

# Copy Installer
Copy-Item "target\installer\KeyboardShadow-1.0.0.exe" "build-output\" -Force

# Copy Portable ZIP
Copy-Item "target\KeyboardShadow-1.0.0.zip" "build-output\KeyboardShadow-1.0.0-portable.zip" -Force
```

---

## 📦 Cấu trúc các gói

### .EXE Installer (KeyboardShadow-1.0.0.exe)

**Tính năng:**
- ✅ Tự động tạo shortcut trên Desktop
- ✅ Đăng ký trong Control Panel (Add/Remove Programs)
- ✅ Bundled JRE (không cần cài Java riêng)
- ✅ Cài đặt vào `C:\Program Files\KeyboardShadow\`
- ✅ Tạo menu Start
- ✅ Dung lượng: ~150-200 MB

**Cách cài đặt:**
1. Double-click `KeyboardShadow-1.0.0.exe`
2. Chọn đường dẫn cài đặt
3. Hoàn thành installation
4. Chạy từ Desktop shortcut hoặc Start Menu

**Gỡ cài đặt:**
- Control Panel → Programs → Uninstall a Program → KeyboardShadow

---

### .ZIP Portable (KeyboardShadow-1.0.0-portable.zip)

**Tính năng:**
- ✅ Không cần installation
- ✅ Giải nén dùng ngay
- ✅ Có thể chạy từ USB/Cloud
- ✅ File nhỏ (~50-80 MB)

**Cấu trúc bên trong ZIP:**
```
KeyboardShadow-1.0.0-portable/
├── KeyboardShadow-1.0.0.jar      # Main application JAR
├── run.bat                        # Chạy trên CMD
├── run.ps1                        # Chạy trên PowerShell
└── README.md                      # Instructions
```

**Cách chạy:**
```powershell
# Giải nén ZIP

# Option 1: Chạy từ Command Prompt
cd KeyboardShadow-1.0.0-portable
run.bat

# Option 2: Chạy từ PowerShell
.\run.ps1
```

---

## 🔍 Khắc phục sự cố

### Lỗi: "jpackage not found"
- JDK phải có jpackage tool (JDK 17+)
- Kiểm tra: `jpackage --version`
- Nếu không có, cài đặt JDK mới từ adoptium.net

### Lỗi: "Main JAR not found"
- Đảm bảo đã chạy `mvn package` trước
- File `target\KeyboardShadow-1.0.0.jar` phải tồn tại

### Lỗi: "JAVA_HOME not set"
```powershell
# Set JAVA_HOME tạm thời (trong PowerShell session)
$env:JAVA_HOME = "C:\Program Files\jdk-17.0.10"

# Hoặc set vĩnh viễn
[Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\jdk-17.0.10', 'User')
```

### Ứng dụng không chạy trên máy khác
- Máy đó cần cài đặt .EXE installer (có bundled JRE)
- Hoặc cài đặt Java 17+ rồi dùng .ZIP portable

---

## 📊 Thông tin chi tiết

| Attribute | Giá trị |
|-----------|--------|
| **App Name** | KeyboardShadow |
| **Developer** | NguyenNhatCuong |
| **Version** | 1.0.0 |
| **Main Class** | com.keyboarddisplay.AppLauncher |
| **Java Version** | 17+ |
| **Framework** | JavaFX 21.0.1 |

---

## 🎯 Các lệnh Maven hữu ích

```powershell
# Clean build artifacts
mvn clean

# Compile code
mvn compile

# Run tests
mvn test

# Package (JAR + ZIP)
mvn package

# Package with assembly
mvn assembly:single

# Full build
mvn clean package -DskipTests

# Deploy/Install locally
mvn install
```

---

## 📝 Ghi chú

- File `portable.xml` định nghĩa cấu trúc ZIP
- File `pom.xml` chứa tất cả dependencies và plugins
- Icon: `src/main/resources/icons/KeyboardDisplay.ico`
- Scripts: `run.bat` (CMD) và `run.ps1` (PowerShell)

---

**Được tạo bởi:** NguyenNhatCuong  
**Ứng dụng:** KeyboardShadow  
**Phiên bản:** 1.0.0

