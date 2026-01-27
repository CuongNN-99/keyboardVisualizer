# Keyboard Display Runner Script
# This script runs the application with JavaFX modules properly configured

# Get the directory of this script
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

# Maven repository path
$m2Repo = "$env:USERPROFILE\.m2\repository"

# JavaFX modules path (version 21.0.1)
$javafxVersion = "21.0.1"
$javafxPath = "$m2Repo\org\openjfx"

# Build the module path
$modulePath = @(
    "$javafxPath\javafx-base\$javafxVersion\javafx-base-$javafxVersion-win.jar",
    "$javafxPath\javafx-controls\$javafxVersion\javafx-controls-$javafxVersion-win.jar",
    "$javafxPath\javafx-graphics\$javafxVersion\javafx-graphics-$javafxVersion-win.jar",
    "$javafxPath\javafx-fxml\$javafxVersion\javafx-fxml-$javafxVersion-win.jar",
    "$javafxPath\javafx-swing\$javafxVersion\javafx-swing-$javafxVersion-win.jar"
) -join ";"

# Class path includes all dependencies and the app JAR
$classPath = @(
    "$scriptDir\target\keyboard-display-1.0.0.jar",
    "$m2Repo\com\github\kwhat\jnativehook\2.2.2\jnativehook-2.2.2.jar",
    "$m2Repo\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar",
    "$m2Repo\org\slf4j\slf4j-api\2.0.9\slf4j-api-2.0.9.jar",
    "$m2Repo\ch\qos\logback\logback-classic\1.4.14\logback-classic-1.4.14.jar",
    "$m2Repo\ch\qos\logback\logback-core\1.4.14\logback-core-1.4.14.jar"
) -join ";"

Write-Host "Launching Keyboard Display..."
Write-Host "Module Path: $modulePath"
Write-Host "Class Path: $classPath"

# Run the application with JavaFX modules
& java `
    --module-path "$modulePath" `
    --add-modules javafx.controls,javafx.fxml,javafx.swing `
    -cp "$classPath" `
    com.keyboarddisplay.Main

if ($LASTEXITCODE -ne 0) {
    Write-Host "Application exited with code $LASTEXITCODE"
    pause
}
