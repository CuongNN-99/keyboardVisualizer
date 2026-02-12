REM KeyboardShadow - Create EXE Installer
REM Run this after Maven build

@echo off
setlocal enabledelayedexpansion

echo.
echo ========================================
echo KeyboardShadow - jpackage Installer
echo ========================================
echo.

REM Check jpackage
echo [Step 1] Checking jpackage...
where jpackage >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] jpackage not found!
    echo You need Java JDK 17+ installed
    echo Download from: https://adoptium.net/
    exit /b 1
)
echo [OK] jpackage found

REM Check JAR file
echo.
echo [Step 2] Checking JAR file...
if not exist "target\KeyboardShadow-1.0.0.jar" (
    echo [ERROR] JAR file not found!
    echo Run: mvn clean package -DskipTests
    exit /b 1
)
echo [OK] JAR file found

REM Create output folder
echo.
echo [Step 3] Creating output folder...
if exist "target\installer" rmdir /s /q "target\installer"
mkdir "target\installer"
echo [OK] Output folder created

REM Get JAVA_HOME
echo.
echo [Step 4] Setting JAVA_HOME...
if not defined JAVA_HOME (
    REM Try to find JDK automatically
    for /f "tokens=*" %%i in ('where jpackage') do set JAVA_HOME=%%~dpi..\
    set JAVA_HOME=!JAVA_HOME:\bin\!=!
)
echo [OK] JAVA_HOME: %JAVA_HOME%

REM Check icon
echo.
echo [Step 5] Checking icon...
set ICON_PATH=src\main\resources\icons\KeyboardDisplay.ico
if exist "%ICON_PATH%" (
    echo [OK] Icon found: %ICON_PATH%
) else (
    echo [WARN] Icon not found (optional)
    set ICON_PATH=
)

REM Run jpackage
echo.
echo [Step 6] Running jpackage...
echo.

if defined ICON_PATH (
    jpackage --type exe --name KeyboardShadow --app-version 1.0.0 --vendor NguyenNhatCuong --description "Real-time keyboard display for streaming" --input target --main-jar KeyboardShadow-1.0.0.jar --main-class com.keyboarddisplay.AppLauncher --dest target\installer --win-shortcut --win-menu --win-dir-chooser --icon "%ICON_PATH%"
) else (
    jpackage --type exe --name KeyboardShadow --app-version 1.0.0 --vendor NguyenNhatCuong --description "Real-time keyboard display for streaming" --input target --main-jar KeyboardShadow-1.0.0.jar --main-class com.keyboarddisplay.AppLauncher --dest target\installer --win-shortcut --win-menu --win-dir-chooser
)

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo SUCCESS!
    echo ========================================
    echo.
    echo [OUTPUT]
    for %%f in (target\installer\*.exe) do (
        echo [OK] %%~nxf
    )
    echo.
    echo Application Details:
    echo Name: KeyboardShadow
    echo Developer: NguyenNhatCuong
    echo Version: 1.0.0
    echo.
) else (
    echo.
    echo ========================================
    echo FAILED!
    echo ========================================
    echo.
    echo Error code: %ERRORLEVEL%
    echo.
    echo Troubleshooting:
    echo 1. Make sure you have JDK 17+ installed (not JRE)
    echo 2. Set JAVA_HOME to your JDK installation
    echo 3. Run: mvn clean package -DskipTests first
    exit /b 1
)

endlocal

