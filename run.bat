@echo off
REM Keyboard Display Runner Script (Windows Batch)
REM This script runs the application with JavaFX modules properly configured

setlocal enabledelayedexpansion

REM Get the directory of this script
set scriptDir=%~dp0

REM Maven repository path
set m2Repo=%USERPROFILE%\.m2\repository

REM JavaFX modules path (version 21.0.1)
set javafxVersion=21.0.1
set javafxPath=%m2Repo%\org\openjfx

REM Build the module path
set modulePath=%javafxPath%\javafx-base\%javafxVersion%\javafx-base-%javafxVersion%-win.jar;%javafxPath%\javafx-controls\%javafxVersion%\javafx-controls-%javafxVersion%-win.jar;%javafxPath%\javafx-graphics\%javafxVersion%\javafx-graphics-%javafxVersion%-win.jar;%javafxPath%\javafx-fxml\%javafxVersion%\javafx-fxml-%javafxVersion%-win.jar;%javafxPath%\javafx-swing\%javafxVersion%\javafx-swing-%javafxVersion%-win.jar

REM Class path includes all dependencies and the app JAR
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
