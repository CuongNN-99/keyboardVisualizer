package com.keyboarddisplay.model;

import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model class for application settings.
 * Contains all configurable parameters for the Keyboard Display application.
 */
public class AppSettings {
    private static final Logger logger = LoggerFactory.getLogger(AppSettings.class);

    // --- Color settings ---
    private String highlightColor = "#00BFFF"; // Đổi sang DeepSkyBlue cho hiện đại
    private String backgroundColor = "#1E1E1E";

    // --- Opacity settings ---
    private double opacity = 1.0;
    private double backgroundOpacity = 0.3;

    // --- Animation settings ---
    private int animationDuration = 200;

    // --- Layout settings ---
    private KeyboardLayoutType layoutType = KeyboardLayoutType.FULL_87;

    // --- Window Behavior settings ---
    private boolean alwaysOnTop = true;
    private boolean clickThrough = false;

    private boolean showOnlySelected = false;
    private String[] selectedKeys = {};

    /**
     * Enum cho các kiểu Layout.
     * Lưu ý: Tùy chọn CUSTOM sẽ bị lọc bỏ ở UI trong SettingsController.
     */
    public enum KeyboardLayoutType {
        FULL_87("Full 87 Keys"),
        WASD("WASD Only"),
        ARROWS("Arrow Keys"),
        NUMPAD("Numpad"),
        CUSTOM("Custom Selection");

        private final String displayName;
        KeyboardLayoutType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
        @Override public String toString() { return displayName; }
    }

    public AppSettings() {}

    // ==================== Getters and Setters ====================

    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    public String getHighlightColor() { return highlightColor; }
    public void setHighlightColor(String highlightColor) { this.highlightColor = highlightColor; }

    public Color getHighlightColorAsColor() {
        try {
            return Color.web(highlightColor);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid highlight color: {}, defaulting to RED", highlightColor);
            return Color.RED;
        }
    }

    public double getOpacity() { return opacity; }
    public void setOpacity(double opacity) {
        this.opacity = Math.max(0.0, Math.min(1.0, opacity));
    }

    public double getBackgroundOpacity() { return backgroundOpacity; }
    public void setBackgroundOpacity(double backgroundOpacity) {
        this.backgroundOpacity = Math.max(0.0, Math.min(1.0, backgroundOpacity));
    }

    public int getAnimationDuration() { return animationDuration; }
    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = Math.max(50, Math.min(1000, animationDuration));
    }

    public KeyboardLayoutType getLayoutType() { return layoutType; }
    public void setLayoutType(KeyboardLayoutType layoutType) {
        this.layoutType = (layoutType != null) ? layoutType : KeyboardLayoutType.FULL_87;
    }

    public boolean isAlwaysOnTop() { return alwaysOnTop; }
    public void setAlwaysOnTop(boolean alwaysOnTop) { this.alwaysOnTop = alwaysOnTop; }

    public boolean isClickThrough() { return clickThrough; }
    public void setClickThrough(boolean clickThrough) { this.clickThrough = clickThrough; }

    public boolean isShowOnlySelected() { return showOnlySelected; }
    public void setShowOnlySelected(boolean showOnlySelected) { this.showOnlySelected = showOnlySelected; }

    public String[] getSelectedKeys() { return selectedKeys; }
    public void setSelectedKeys(String[] selectedKeys) {
        this.selectedKeys = (selectedKeys != null) ? selectedKeys : new String[0];
    }

    // ==================== Utility Methods ====================

    public boolean validate() {
        try {
            if (opacity < 0.0 || opacity > 1.0) return false;
            if (backgroundOpacity < 0.0 || backgroundOpacity > 1.0) return false;
            if (animationDuration < 50 || animationDuration > 1000) return false;
            Color.web(highlightColor);
            Color.web(backgroundColor);
            return layoutType != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void resetToDefaults() {
        this.highlightColor = "#00BFFF";
        this.backgroundColor = "#1E1E1E";
        this.opacity = 1.0;
        this.backgroundOpacity = 0.3;
        this.animationDuration = 200;
        this.layoutType = KeyboardLayoutType.FULL_87;
        this.alwaysOnTop = true;
        this.clickThrough = false;
        this.showOnlySelected = false;
        this.selectedKeys = new String[0];
    }

    public AppSettings copy() {
        AppSettings copy = new AppSettings();
        copy.highlightColor = this.highlightColor;
        copy.backgroundColor = this.backgroundColor;
        copy.opacity = this.opacity;
        copy.backgroundOpacity = this.backgroundOpacity;
        copy.animationDuration = this.animationDuration;
        copy.layoutType = this.layoutType;
        copy.alwaysOnTop = this.alwaysOnTop;
        copy.clickThrough = this.clickThrough;
        copy.showOnlySelected = this.showOnlySelected;
        copy.selectedKeys = (this.selectedKeys != null) ? this.selectedKeys.clone() : new String[0];
        return copy;
    }
}