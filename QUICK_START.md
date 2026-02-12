# 🚀 KeyboardShadow - Quick Start Guide

## 🎯 Lựa chọn nhanh

### Option 1: ⚡ Build Tất Cả (Khuyên dùng)
```powershell
.\build-all.ps1
```
Tạo cả JAR, ZIP portable, và EXE installer.

---

### Option 2: 📦 Chỉ Build Maven (JAR + ZIP)
```powershell
.\build-packages.ps1
```
Hoặc chạy Maven trực tiếp:
```powershell
mvn clean package -DskipTests
```

---

### Option 3: 🔧 Chỉ Tạo EXE Installer
```powershell
.\build-installer.ps1
```
Yêu cầu: Phải chạy `mvn clean package` trước.

---

## 📥 Yêu cầu Tiên Quyết

### Java Development Kit (JDK)
- **Phiên bản**: 17 hoặc cao hơn
- **Loại**: JDK (không phải JRE)
- **Download**: https://adoptium.net/

Kiểm tra:
```powershell
java -version
```

### Maven
- **Phiên bản**: 3.6.0 hoặc cao hơn
- **Download**: https://maven.apache.org/

Kiểm tra:
```powershell
mvn --version
```

---

## 📂 Cấu trúc Thư Mục Output

Sau khi build, bạn sẽ có:

```
target/
├── KeyboardShadow-1.0.0.jar      ← Shaded JAR (tất cả dependencies)
├── KeyboardShadow-1.0.0.zip      ← Portable ZIP
└── installer/
    └── KeyboardShadow-1.0.0.exe  ← Windows Installer
```

---

## 📦 3 Định Dạng Phân Phối

### 1. EXE Installer (Khuyên dùng cho End Users)
- **File**: `KeyboardShadow-1.0.0.exe`
- **Kích thước**: ~150-200 MB (bundled JRE)
- **Cài đặt**: Tương tự như phần mềm Windows thông thường
- **Dỡ cài đặt**: Control Panel → Uninstall
- **Shortcut**: Desktop + Start Menu

**Cách cài đặt:**
```
1. Double-click KeyboardShadow-1.0.0.exe
2. Chọn đường dẫn cài đặt
3. Hoàn thành
4. Tìm trong Start Menu hoặc Desktop
```

---

### 2. ZIP Portable (Cho Power Users/Developers)
- **File**: `KeyboardShadow-1.0.0.zip`
- **Kích thước**: ~50-80 MB
- **Cài đặt**: Không cần, giải nén dùng ngay
- **Chạy từ**: USB, Cloud, bất kỳ đâu

**Cách sử dụng:**
```powershell
# Giải nén ZIP
unzip KeyboardShadow-1.0.0.zip

# Vào thư mục
cd KeyboardShadow-1.0.0-portable

# Chạy ứng dụng (chọn một trong hai)
run.bat              # Command Prompt
.\run.ps1           # PowerShell
```

---

### 3. JAR Shaded (Cho Developers)
- **File**: `KeyboardShadow-1.0.0.jar`
- **Kích thước**: ~50-80 MB
- **Chạy**: `java -jar KeyboardShadow-1.0.0.jar`
- **Yêu cầu**: Java 17+ phải được cài đặt

---

## 🔍 Kiểm Tra Lỗi

### Lỗi: "mvn not found"
```powershell
# Cài đặt Maven hoặc thêm vào PATH
# Download: https://maven.apache.org/download.cgi
```

### Lỗi: "jpackage not found"
```powershell
# Cần JDK 17+, không phải JRE
# Tải từ: https://adoptium.net/
```

### Lỗi: JAR file not found
```powershell
# Chạy Maven build trước:
mvn clean package -DskipTests
```

### Lỗi: JAVA_HOME not set
```powershell
# Set JAVA_HOME tạm thời:
$env:JAVA_HOME = "C:\Program Files\jdk-17"

# Hoặc set vĩnh viễn qua Environment Variables
```

---

## 📊 Thông Tin Chi Tiết

| Tham số | Giá trị |
|---------|--------|
| **App Name** | KeyboardShadow |
| **Developer** | NguyenNhatCuong |
| **Version** | 1.0.0 |
| **Framework** | JavaFX 21.0.1 |
| **Java** | 17+ |
| **OS** | Windows 7+ |

---

## 🎯 Build Workflow

```
┌─────────────────┐
│ Source Code     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Maven Build     │
│ (compile)       │
└────────┬────────┘
         │
    ┌────┴────┬──────────┐
    ▼         ▼          ▼
┌──────┐ ┌──────────┐ ┌─────┐
│ JAR  │ │ ZIP      │ │ EXE │
│Shaded│ │Portable  │ │Inst.│
└──────┘ └──────────┘ └─────┘
    │         │          │
    └─────────┴──────────┘
           │
           ▼
    Ready to Distribute
```

---

## 💡 Mẹo & Khuyên Nghị

1. **Cho End Users**: Dùng `.exe` installer (dễ dàng, không cần setup)
2. **Cho Portable**: Dùng `.zip` (không cài đặt, chạy từ USB)
3. **Cho Developers**: Dùng `.jar` hoặc source code

---

## 🔗 Liên Kết Hữu Ích

- Java JDK: https://adoptium.net/
- Maven: https://maven.apache.org/
- JavaFX: https://gluonhq.com/products/javafx/
- JNativeHook: https://github.com/kwhat/jnativehook

---

## 📝 Ghi Chú

- Tất cả scripts hỗ trợ Windows PowerShell
- Build time: ~10-15 giây (chỉ Maven)
- jpackage builder time: ~1-2 phút (thêm EXE)
- Toàn bộ quá trình: ~3-5 phút

---

**Tạo bởi**: NguyenNhatCuong  
**Ứng dụng**: KeyboardShadow  
**Phiên bản**: 1.0.0

