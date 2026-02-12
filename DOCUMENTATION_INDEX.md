# 📚 KeyboardShadow - Documentation Index

## 🎯 Tìm Kiếm Nhanh

### 🚀 Bắt Đầu Nhanh (5 phút)
👉 **[QUICK_START.md](QUICK_START.md)** - Hướng dẫn build ngay lập tức
- Cách 1: Build tất cả
- Cách 2: Build chỉ JAR+ZIP
- Cách 3: Build chỉ EXE
- Kiểm tra yêu cầu tiên quyết

### ✅ Kiểm Tra Trước Build (10 phút)
👉 **[PRE_BUILD_CHECKLIST.md](PRE_BUILD_CHECKLIST.md)** - Danh sách kiểm tra
- Yêu cầu hệ thống
- Setup checklist
- Build readiness
- Troubleshooting

### 📋 Tóm Tắt Setup (5 phút)
👉 **[SETUP_SUMMARY.md](SETUP_SUMMARY.md)** - Tổng quan những gì đã setup
- Những gì đã cấu hình
- Files được tạo/cập nhật
- Cách build
- Thông tin ứng dụng

### 📦 Hướng Dẫn Chi Tiết (30 phút)
👉 **[PACKAGING_GUIDE.md](PACKAGING_GUIDE.md)** - Hướng dẫn đầy đủ
- Yêu cầu hệ thống chi tiết
- Build từng bước
- Cấu trúc các gói
- Khắc phục sự cố

### 📝 Về Ứng Dụng
👉 **[README.md](README.md)** - Thông tin ứng dụng
- Giới thiệu KeyboardShadow
- Tính năng
- Cách sử dụng
- Yêu cầu hệ thống

---

## 📂 Scripts & Tools

### 🔴 **build-all.ps1** (⭐ TÓI KHUYÊN DÙNG)
```powershell
.\build-all.ps1
```
- Tạo JAR shaded + ZIP portable + EXE installer
- Hơi mất thời gian nhưng hoàn chỉnh
- Dung lượng output: ~250-300 MB total

### 🟠 **build-packages.ps1**
```powershell
.\build-packages.ps1
```
- Chỉ tạo JAR + ZIP (không tạo EXE)
- Nhanh hơn (30 giây)
- Dùng khi không cần EXE installer

### 🟡 **build-installer.ps1**
```powershell
.\build-installer.ps1
```
- Chỉ tạo EXE installer
- Yêu cầu phải chạy Maven build trước
- Dùng khi đã có JAR

### 🟢 **create-installer.ps1**
```powershell
.\create-installer.ps1
```
- Script gốc (tương tự build-all)
- Hỗ trợ cả Maven + jpackage

### 🔵 **Maven trực tiếp**
```powershell
mvn clean package -DskipTests
```
- Tạo JAR + ZIP
- Nhanh nhất
- Không tạo EXE

### 🟣 **Launcher Scripts**
```powershell
# Trong thư mục ZIP portable:
run.bat    # Command Prompt
run.ps1    # PowerShell
```

---

## 📊 So Sánh Các Định Dạng Output

| Định Dạng | File | Kích Thước | Dùng Cho | Build Time |
|-----------|------|-----------|----------|-----------|
| **JAR** | KeyboardShadow-1.0.0.jar | 50-80 MB | Developers | 5-10s |
| **ZIP** | KeyboardShadow-1.0.0.zip | 50-80 MB | Portable | 30s |
| **EXE** | KeyboardShadow-1.0.0.exe | 150-200 MB | End Users | 2-3m |

---

## 🎯 Dòng Chảy Quyết Định (Decision Tree)

```
Bạn muốn gì?
│
├─→ Build nhanh (JAR + ZIP)
│   └─→ mvn clean package -DskipTests
│       ⏱️ 30 giây
│
├─→ Build tất cả (JAR + ZIP + EXE)
│   └─→ .\build-all.ps1
│       ⏱️ 3-5 phút
│
├─→ Chỉ tạo EXE installer
│   └─→ .\build-installer.ps1
│       ⏱️ 2-3 phút (cần Maven build trước)
│
├─→ Phân phối cho End Users
│   └─→ Dùng KeyboardShadow-1.0.0.exe
│       ✅ Dễ cài đặt, không cần setup
│
├─→ Phân phối bản Portable
│   └─→ Dùng KeyboardShadow-1.0.0.zip
│       ✅ Nhẹ, chạy từ USB, không cài đặt
│
└─→ Phân phối cho Developers
    └─→ Dùng KeyboardShadow-1.0.0.jar
        ✅ Cần Java 17+ để chạy
```

---

## 📋 Quy Trình Build Chi Tiết

### Bước 1: Chuẩn Bị (Before Build)
```powershell
# Mở PowerShell
cd D:\keyboardVisualizer\keyboardVisualClaude\KeyboardDisplay

# Kiểm tra yêu cầu
java -version      # 17+
mvn --version      # 3.6.0+
```

### Bước 2: Build
```powershell
# Option A: Nhanh (JAR + ZIP)
mvn clean package -DskipTests

# Option B: Hoàn chỉnh (JAR + ZIP + EXE)
.\build-all.ps1
```

### Bước 3: Kiểm Tra
```powershell
# Kiểm tra files đã tạo
ls target/KeyboardShadow*.jar
ls target/KeyboardShadow*.zip
ls target/installer/KeyboardShadow*.exe
```

### Bước 4: Phân Phối
```powershell
# Copy files theo nhu cầu
# - EXE: cho End Users
# - ZIP: cho Portable version
# - JAR: cho Developers
```

---

## 🔐 Yêu Cầu Tiên Quyết

### ✅ Bắt Buộc
- [ ] **JDK 17+** (cần JDK, không phải JRE)
  ```
  Download: https://adoptium.net/
  Check: java -version
  ```
- [ ] **Maven 3.6.0+**
  ```
  Download: https://maven.apache.org/
  Check: mvn --version
  ```

### ⭐ Tuỳ Chọn (cho EXE installer)
- [ ] **jpackage** (đi kèm JDK 17+)
  ```
  Check: jpackage --version
  ```
- [ ] **JAVA_HOME** environment variable
  ```
  Set: $env:JAVA_HOME = "C:\Program Files\jdk-17"
  ```

---

## 🎯 Use Cases & Recommendations

### Use Case 1: "Tôi muốn build nhanh"
→ Chạy: `mvn clean package -DskipTests`  
⏱️ Thời gian: ~30 giây  
📦 Output: JAR + ZIP

### Use Case 2: "Tôi muốn build hoàn chỉnh"
→ Chạy: `.\build-all.ps1`  
⏱️ Thời gian: ~3-5 phút  
📦 Output: JAR + ZIP + EXE

### Use Case 3: "Tôi chỉ muốn ZIP portable"
→ Chạy: `mvn clean package -DskipTests`  
📦 Output: `target\KeyboardShadow-1.0.0.zip`

### Use Case 4: "Tôi chỉ muốn EXE installer"
→ Chạy: `mvn clean package -DskipTests` + `.\build-installer.ps1`  
📦 Output: `target\installer\KeyboardShadow-1.0.0.exe`

### Use Case 5: "Tôi muốn phân phối cho End Users"
→ Sử dụng: `KeyboardShadow-1.0.0.exe`  
✅ Lợi ích: Dễ cài, không cần setup Java, bundled JRE

### Use Case 6: "Tôi muốn portable version cho USB"
→ Sử dụng: `KeyboardShadow-1.0.0.zip`  
✅ Lợi ích: Nhẹ, chạy từ bất kỳ đâu, không cài đặt

---

## 🆘 Khắc Phục Sự Cố

### ❌ "mvn: command not found"
👉 Xem: [PACKAGING_GUIDE.md](PACKAGING_GUIDE.md#khắc-phục-sự-cố)

### ❌ "jpackage: command not found"
👉 Xem: [PACKAGING_GUIDE.md](PACKAGING_GUIDE.md#khắc-phục-sự-cố)

### ❌ "JAVA_HOME not set"
👉 Xem: [PACKAGING_GUIDE.md](PACKAGING_GUIDE.md#khắc-phục-sự-cố)

### ❌ "Build failed"
👉 Xem: [QUICK_START.md](QUICK_START.md#-kiểm-tra-lỗi)

---

## 📊 Thông Tin Ứng Dụng

| Tham số | Giá Trị |
|--------|--------|
| **App Name** | KeyboardShadow |
| **Developer** | NguyenNhatCuong |
| **Version** | 1.0.0 |
| **Framework** | JavaFX 21.0.1 |
| **Java Version** | 17+ |
| **OS** | Windows 7+ |
| **License** | MIT (xem LICENSE file) |

---

## 🔗 Các Liên Kết Hữu Ích

- **Java JDK**: https://adoptium.net/
- **Maven**: https://maven.apache.org/
- **JavaFX**: https://gluonhq.com/products/javafx/
- **JNativeHook**: https://github.com/kwhat/jnativehook
- **JNA**: https://github.com/java-native-access/jna

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề:

1. **Kiểm tra**: [PRE_BUILD_CHECKLIST.md](PRE_BUILD_CHECKLIST.md)
2. **Chi tiết**: [PACKAGING_GUIDE.md](PACKAGING_GUIDE.md)
3. **Nhanh**: [QUICK_START.md](QUICK_START.md)

---

## ✨ Tóm Tắt Nhanh

```
🚀 BUILD NGAY
─────────────
.\build-all.ps1

📦 OUTPUT
─────────
target/KeyboardShadow-1.0.0.jar
target/KeyboardShadow-1.0.0.zip
target/installer/KeyboardShadow-1.0.0.exe
```

---

**Tài liệu được cập nhật**: 12 February 2026  
**Status**: ✅ Sẵn sàng build

