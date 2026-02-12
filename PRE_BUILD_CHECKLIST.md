# ✅ KeyboardShadow - Pre-Build Checklist

## 🔍 Kiểm Tra Yêu Cầu Hệ Thống

### Java Development Kit (JDK)
- [ ] JDK 17 hoặc cao hơn đã cài đặt
- [ ] Kiểm tra: `java -version`
- [ ] Download link: https://adoptium.net/

### Maven
- [ ] Maven 3.6.0 hoặc cao hơp đã cài đặt
- [ ] Kiểm tra: `mvn --version`
- [ ] Download link: https://maven.apache.org/

### PowerShell
- [ ] Windows PowerShell 5.0+ hoặc PowerShell Core
- [ ] Scripts được set to "Unrestricted" execution policy
  ```powershell
  Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Scope CurrentUser
  ```

---

## 📁 Project Setup Checklist

### Cấu Trúc Thư Mục
- [ ] Thư mục gốc: `D:\keyboardVisualizer\keyboardVisualClaude\KeyboardDisplay`
- [ ] Thư mục `src\main\java\` chứa source code
- [ ] Thư mục `src\main\resources\` chứa resources
- [ ] Thư mục `src\main\assembly\` chứa assembly configs

### Build Scripts
- [ ] `pom.xml` - Maven configuration (✅ UPDATED)
- [ ] `build-all.ps1` - Total build script (✅ CREATED)
- [ ] `build-installer.ps1` - EXE installer script (✅ CREATED)
- [ ] `build-packages.ps1` - Maven build script (✅ CREATED)
- [ ] `create-installer.ps1` - Original installer script (✅ UPDATED)

### Launcher Scripts
- [ ] `run.bat` - Command Prompt launcher (✅ UPDATED)
- [ ] `run.ps1` - PowerShell launcher (✅ UPDATED)

### Assembly Config
- [ ] `src/main/assembly/portable.xml` - ZIP config (✅ CREATED)
- [ ] `src/main/assembly/dist.xml` - Existing config

### Documentation
- [ ] `README.md` - App description (✅ CREATED)
- [ ] `QUICK_START.md` - Quick guide (✅ CREATED)
- [ ] `PACKAGING_GUIDE.md` - Detailed guide (✅ CREATED)
- [ ] `SETUP_SUMMARY.md` - Setup summary (✅ CREATED)

---

## 🔧 Maven Configuration Checklist

### pom.xml Updates
- [ ] Artifact ID: `KeyboardShadow` (✅ UPDATED)
- [ ] App Name: `KeyboardShadow` (✅ UPDATED)
- [ ] Developer: `NguyenNhatCuong` (✅ UPDATED)
- [ ] Properties: `<app.name>` and `<app.vendor>` (✅ ADDED)
- [ ] maven-assembly-plugin (✅ ADDED)
- [ ] maven-shade-plugin (✅ CONFIGURED)
- [ ] javafx-maven-plugin (✅ UPDATED)

### Dependencies
- [ ] JavaFX 21.0.1 - Controls, FXML, Swing
- [ ] JNA 5.14.0 - Platform integration
- [ ] JNativeHook 2.2.2 - Global key listening
- [ ] GSON 2.10.1 - JSON serialization
- [ ] SLF4J 2.0.9 - Logging API
- [ ] Logback 1.4.14 - Logging implementation

---

## 🎯 Build Readiness Checklist

### Before Running Build
- [ ] All source files are in `src/main/java/`
- [ ] All resources are in `src/main/resources/`
- [ ] Icon file exists: `src/main/resources/icons/KeyboardDisplay.ico`
- [ ] Configuration files are correctly structured

### Build Targets
- [ ] JAR Output: `target/KeyboardShadow-1.0.0.jar`
- [ ] ZIP Output: `target/KeyboardShadow-1.0.0.zip`
- [ ] EXE Output: `target/installer/KeyboardShadow-1.0.0.exe` (optional)

---

## 🚀 Ready to Build

### One-Command Build (All in One)
```powershell
cd D:\keyboardVisualizer\keyboardVisualClaude\KeyboardDisplay
.\build-all.ps1
```

### Expected Results
- [ ] Build starts successfully
- [ ] Maven compiles source code
- [ ] JAR file created (50-80 MB)
- [ ] ZIP file created (50-80 MB)
- [ ] EXE installer created (150-200 MB) - if jpackage available
- [ ] Build completes with "SUCCESS" message

### Output Files
- [ ] ✅ `target/KeyboardShadow-1.0.0.jar`
- [ ] ✅ `target/KeyboardShadow-1.0.0.zip`
- [ ] ✅ `target/installer/KeyboardShadow-1.0.0.exe` (if jpackage available)

---

## 📦 Post-Build Checklist

### Verify Output Files
```powershell
# Check JAR
Get-Item "target\KeyboardShadow-1.0.0.jar"

# Check ZIP
Get-Item "target\KeyboardShadow-1.0.0.zip"

# Check EXE (if created)
Get-Item "target\installer\KeyboardShadow-1.0.0.exe"
```

### Test Portable Version
```powershell
# Extract ZIP
unzip target/KeyboardShadow-1.0.0.zip

# Run application
cd KeyboardShadow-1.0.0-portable
.\run.ps1
```

### Test EXE Installer (if created)
```powershell
# Double-click installer
target/installer/KeyboardShadow-1.0.0.exe
```

---

## 📋 Troubleshooting Checklist

### If Build Fails
- [ ] Check Java version: `java -version`
- [ ] Check Maven: `mvn --version`
- [ ] Run with verbose: `mvn clean package -X`
- [ ] Check file permissions
- [ ] Delete old `target` folder: `mvn clean`

### If EXE Build Fails
- [ ] Check jpackage: `jpackage --version`
- [ ] Ensure JDK 17+, not JRE
- [ ] Set JAVA_HOME environment variable
- [ ] Check icon file exists
- [ ] Run Maven build first: `mvn clean package`

### If ZIP File is Empty or Wrong
- [ ] Check `src/main/assembly/portable.xml`
- [ ] Verify file paths are correct
- [ ] Run Maven assembly: `mvn assembly:single`

---

## ✨ Additional Notes

### Build Properties
- **App Name**: KeyboardShadow
- **Developer**: NguyenNhatCuong
- **Version**: 1.0.0
- **Java Target**: 17+
- **Framework**: JavaFX 21.0.1
- **Build Tool**: Maven 3.9.0+

### Distribution Options
1. **For End Users**: Use `.exe` installer
2. **For Portable**: Use `.zip` file
3. **For Developers**: Use `.jar` file

### File Sizes (Approximate)
- JAR Shaded: 50-80 MB
- ZIP Portable: 50-80 MB
- EXE Installer: 150-200 MB (with JRE)

---

## 🎉 Ready Status

```
✅ Source Code Ready
✅ Maven Configured
✅ Assembly Scripts Ready
✅ Documentation Complete
✅ Build Scripts Created
✅ Launcher Scripts Updated

🚀 READY TO BUILD
```

---

**Last Updated**: 12 February 2026  
**Setup Status**: ✅ Complete

