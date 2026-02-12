@echo off
REM KeyboardShadow Runner Script (Windows Batch)
REM This script runs the application

setlocal enabledelayedexpansion

REM Get the directory of this script
set scriptDir=%~dp0

REM Check if running from installed directory or portable directory
if exist "%scriptDir%KeyboardShadow-1.0.0.jar" (
    echo Launching KeyboardShadow...
    java -jar "%scriptDir%KeyboardShadow-1.0.0.jar"
) else if exist "%PROGRAMFILES%\KeyboardShadow\KeyboardShadow-1.0.0.jar" (
    echo Launching KeyboardShadow...
    java -jar "%PROGRAMFILES%\KeyboardShadow\KeyboardShadow-1.0.0.jar"
) else (
    echo Error: KeyboardShadow JAR file not found!
    pause
    exit /b 1
)
set classPath=%scriptDir%target\keyboard-display-1.0.0.jar;%m2Repo%\com\github\kwhat\jnativehook\2.2.2\jnativehook-2.2.2.jar;%m2Repo%\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar;%m2Repo%\org\slf4j\slf4j-api\2.0.9\slf4j-api-2.0.9.jar;%m2Repo%\ch\qos\logback\logback-classic\1.4.14\logback-classic-1.4.14.jar;%m2Repo%\ch\qos\logback\logback-core\1.4.14\logback-core-1.4.14.jar

echo Launching Keyboard Display...
echo Module Path: %modulePath%
echo Class Path: %classPath%

REM Run the application with JavaFX modules
java --module-path "%modulePath%" --add-modules javafx.controls,javafx.fxml,javafx.swing -cp "%classPath%" com.keyboarddisplay.Main

if errorlevel 1 (
    echo Application exited with error code %errorlevel%
    pause
)

endlocal
