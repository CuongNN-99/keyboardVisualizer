#!/bin/bash
# Keyboard Display Runner Script (Linux/Mac)
# This script runs the application with JavaFX modules properly configured

# Get the directory of this script
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Maven repository path
M2_REPO="$HOME/.m2/repository"

# JavaFX modules path (version 21.0.1)
JAVAFX_VERSION="21.0.1"
JAVAFX_PATH="$M2_REPO/org/openjfx"

# Determine platform
OS_NAME=$(uname -s)
if [[ "$OS_NAME" == "Darwin" ]]; then
    PLATFORM="mac"
elif [[ "$OS_NAME" == "Linux" ]]; then
    PLATFORM="linux"
else
    echo "Unsupported platform: $OS_NAME"
    exit 1
fi

# Build the module path
MODULE_PATH="$JAVAFX_PATH/javafx-base/$JAVAFX_VERSION/javafx-base-$JAVAFX_VERSION-$PLATFORM.jar:\
$JAVAFX_PATH/javafx-controls/$JAVAFX_VERSION/javafx-controls-$JAVAFX_VERSION-$PLATFORM.jar:\
$JAVAFX_PATH/javafx-graphics/$JAVAFX_VERSION/javafx-graphics-$JAVAFX_VERSION-$PLATFORM.jar:\
$JAVAFX_PATH/javafx-fxml/$JAVAFX_VERSION/javafx-fxml-$JAVAFX_VERSION-$PLATFORM.jar:\
$JAVAFX_PATH/javafx-swing/$JAVAFX_VERSION/javafx-swing-$JAVAFX_VERSION-$PLATFORM.jar"

# Class path includes all dependencies and the app JAR
CLASS_PATH="$SCRIPT_DIR/target/keyboard-display-$JAVAFX_VERSION.jar:\
$M2_REPO/com/github/kwhat/jnativehook/2.2.2/jnativehook-2.2.2.jar:\
$M2_REPO/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar:\
$M2_REPO/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar:\
$M2_REPO/ch/qos/logback/logback-classic/1.4.14/logback-classic-1.4.14.jar:\
$M2_REPO/ch/qos/logback/logback-core/1.4.14/logback-core-1.4.14.jar"

echo "Launching Keyboard Display..."
echo "Platform: $PLATFORM"

# Run the application with JavaFX modules
java --module-path "$MODULE_PATH" \
     --add-modules javafx.controls,javafx.fxml,javafx.swing \
     -cp "$CLASS_PATH" \
     com.keyboarddisplay.Main

exit $?
