package com.keyboarddisplay.model;

import javafx.scene.input.KeyCode;

import java.util.Objects;

/**
 * Configuration model for individual keyboard keys
 * Defines position, size, and properties of each key in the layout
 */
public class KeyConfig {

    // Display properties
    private String label;           // Text displayed on the key
    private KeyCode keyCode;        // JavaFX KeyCode for logic
    private String keyId;           // UNIQUE ID to distinguish keys (e.g., "SHIFT_L" vs "SHIFT_R")

    // Position and size
    private double x;
    private double y;
    private double width;
    private double height;

    // Metadata
    private String category;
    private boolean visible;
    private String description;

    /**
     * Constructor với keyId định danh riêng biệt
     */
    public KeyConfig(String label, KeyCode keyCode, String keyId, double x, double y, double width, double height) {
        this.label = label;
        this.keyCode = keyCode;
        this.keyId = keyId; // ID duy nhất dùng làm Key trong Map
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.category = "main";
        this.visible = true;
        this.description = "";
    }

    /**
     * Constructor với category và keyId
     */
    public KeyConfig(String label, KeyCode keyCode, String keyId, double x, double y,
                     double width, double height, String category) {
        this(label, keyCode, keyId, x, y, width, height);
        this.category = category;
    }

    /**
     * Full constructor
     */
    public KeyConfig(String label, KeyCode keyCode, String keyId, double x, double y,
                     double width, double height, String category,
                     boolean visible, String description) {
        this.label = label;
        this.keyCode = keyCode;
        this.keyId = keyId;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.category = category;
        this.visible = visible;
        this.description = description;
    }

    // ==================== Getters and Setters ====================

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public KeyCode getKeyCode() { return keyCode; }
    public void setKeyCode(KeyCode keyCode) { this.keyCode = keyCode; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // ==================== Utility Methods ====================

    public boolean isModifierKey() {
        return keyCode == KeyCode.SHIFT
                || keyCode == KeyCode.CONTROL
                || keyCode == KeyCode.ALT
                || keyCode == KeyCode.WINDOWS
                || keyCode == KeyCode.META
                || keyCode == KeyCode.CAPS;
    }

    public boolean isFunctionKey() {
        return category.equals("function")
                || (keyCode.getName().startsWith("F") && keyCode.getName().length() <= 3);
    }

    /**
     * Quan trọng: equals và hashCode giờ dựa trên keyId thay vì keyCode
     * Điều này cho phép tồn tại 2 đối tượng KeyConfig có cùng KeyCode (như SHIFT) nhưng ID khác nhau.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyConfig keyConfig = (KeyConfig) o;
        return Objects.equals(keyId, keyConfig.keyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId);
    }

    @Override
    public String toString() {
        return String.format("KeyConfig{id='%s', label='%s', code=%s}", keyId, label, keyCode);
    }

    public KeyConfig copy() {
        return new KeyConfig(label, keyCode, keyId, x, y, width, height, category, visible, description);
    }
}