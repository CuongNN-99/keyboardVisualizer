package com.keyboarddisplay.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Utility class to generate application icon programmatically
 *
 * File Location: src/main/java/com/keyboarddisplay/util/AppIconGenerator.java
 */
public class AppIconGenerator {

    /**
     * Generate a keyboard icon
     *
     * @param size Size of the icon (width and height)
     * @return Generated icon image
     */
    public static Image generateKeyboardIcon(int size) {
        WritableImage icon = new WritableImage(size, size);
        PixelWriter writer = icon.getPixelWriter();

        // Background - dark gray
        fillRect(writer, 0, 0, size, size, Color.rgb(30, 30, 30));

        // Draw keyboard keys in a grid
        int keySize = size / 6;
        int gap = size / 30;
        int startX = size / 6;
        int startY = size / 4;

        // Draw 3x4 grid of keys
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                int x = startX + col * (keySize + gap);
                int y = startY + row * (keySize + gap);

                // Key background - gray
                fillRect(writer, x, y, keySize, keySize, Color.rgb(60, 60, 60));

                // Key border - lighter gray
                drawRect(writer, x, y, keySize, keySize, Color.rgb(100, 100, 100));

                // Highlight middle key (simulating pressed key) - red
                if (row == 1 && col == 1) {
                    fillRect(writer, x, y, keySize, keySize, Color.rgb(255, 0, 0, 0.8));
                }
            }
        }

        return icon;
    }

    /**
     * Fill a rectangle with a solid color
     *
     * @param writer PixelWriter to write to
     * @param x Starting X coordinate
     * @param y Starting Y coordinate
     * @param width Rectangle width
     * @param height Rectangle height
     * @param color Fill color
     */
    private static void fillRect(PixelWriter writer, int x, int y, int width, int height, Color color) {
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                if (i >= 0 && j >= 0) {
                    writer.setColor(i, j, color);
                }
            }
        }
    }

    /**
     * Draw a rectangle outline
     *
     * @param writer PixelWriter to write to
     * @param x Starting X coordinate
     * @param y Starting Y coordinate
     * @param width Rectangle width
     * @param height Rectangle height
     * @param color Line color
     */
    private static void drawRect(PixelWriter writer, int x, int y, int width, int height, Color color) {
        // Top and bottom lines
        for (int i = x; i < x + width; i++) {
            if (i >= 0 && y >= 0) {
                writer.setColor(i, y, color);
            }
            if (i >= 0 && y + height - 1 >= 0) {
                writer.setColor(i, y + height - 1, color);
            }
        }

        // Left and right lines
        for (int j = y; j < y + height; j++) {
            if (j >= 0 && x >= 0) {
                writer.setColor(x, j, color);
            }
            if (j >= 0 && x + width - 1 >= 0) {
                writer.setColor(x + width - 1, j, color);
            }
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private AppIconGenerator() {
        throw new AssertionError("Cannot instantiate AppIconGenerator");
    }
}