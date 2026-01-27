package com.keyboarddisplay.controller;

import com.keyboarddisplay.model.AppSettings;
import com.keyboarddisplay.util.ConfigManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller cho hộp thoại Settings.
 * Quản lý tùy chọn người dùng và đồng bộ hóa với MainController.
 */
public class SettingsController {
    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @FXML private ComboBox<AppSettings.KeyboardLayoutType> layoutComboBox;
    @FXML private ColorPicker highlightColorPicker;
    @FXML private ColorPicker backgroundColorPicker;

    @FXML private Slider opacitySlider;
    @FXML private Label opacityLabel;

    @FXML private Slider backgroundOpacitySlider;
    @FXML private Label backgroundOpacityLabel;

    @FXML private Slider animationDurationSlider;
    @FXML private Label animationDurationLabel;

    @FXML private CheckBox alwaysOnTopCheckBox;
    @FXML private CheckBox clickThroughCheckBox;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private MainController mainController;
    private AppSettings settings;

    @FXML
    public void initialize() {
        logger.debug("Initializing SettingsController");
        // Lấy bản sao cài đặt hiện tại từ ConfigManager
        settings = ConfigManager.getInstance().getSettings();

        setupLayoutComboBox();
        setupColorPickers();
        setupOpacitySlider();
        setupBackgroundOpacitySlider();
        setupAnimationDurationSlider();
        setupCheckBoxes();
        setupButtons();

        logger.debug("SettingsController initialized");
    }

    private void setupLayoutComboBox() {
        layoutComboBox.getItems().setAll(AppSettings.KeyboardLayoutType.values());
        layoutComboBox.setValue(settings.getLayoutType());
    }

    private void setupColorPickers() {
        highlightColorPicker.setValue(settings.getHighlightColorAsColor());
        try {
            backgroundColorPicker.setValue(Color.web(settings.getBackgroundColor()));
        } catch (Exception e) {
            backgroundColorPicker.setValue(Color.valueOf("#1E1E1E"));
        }
    }

    private void setupOpacitySlider() {
        double currentVal = settings.getOpacity() * 100;
        opacitySlider.setValue(currentVal);
        opacityLabel.setText(String.format("%.0f%%", currentVal));

        opacitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            opacityLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
        });
    }

    private void setupBackgroundOpacitySlider() {
        double currentBgVal = settings.getBackgroundOpacity() * 100;
        backgroundOpacitySlider.setValue(currentBgVal);
        backgroundOpacityLabel.setText(String.format("%.0f%%", currentBgVal));

        backgroundOpacitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            backgroundOpacityLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
        });
    }

    private void setupAnimationDurationSlider() {
        animationDurationSlider.setValue(settings.getAnimationDuration());
        animationDurationLabel.setText(settings.getAnimationDuration() + " ms");

        animationDurationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            animationDurationLabel.setText(newVal.intValue() + " ms");
        });
    }

    private void setupCheckBoxes() {
        alwaysOnTopCheckBox.setSelected(settings.isAlwaysOnTop());
        clickThroughCheckBox.setSelected(settings.isClickThrough());
    }

    private void setupButtons() {
        saveButton.setOnAction(event -> saveSettings());
        cancelButton.setOnAction(event -> closeDialog());
    }

    /**
     * Gán tham chiếu của MainController để gọi hàm applySettings()
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Thu thập dữ liệu từ UI, lưu vào model và yêu cầu MainController cập nhật.
     */
    private void saveSettings() {
        logger.info("Applying and saving settings...");
        try {
            // 1. Cập nhật Layout & Màu sắc
            settings.setLayoutType(layoutComboBox.getValue());
            settings.setHighlightColor(toHex(highlightColorPicker.getValue()));
            settings.setBackgroundColor(toHex(backgroundColorPicker.getValue()));

            // 2. Cập nhật Độ mờ (Chuyển về hệ 0.0 - 1.0)
            settings.setOpacity(opacitySlider.getValue() / 100.0);
            settings.setBackgroundOpacity(backgroundOpacitySlider.getValue() / 100.0);

            // 3. Cập nhật Animation & Window Behavior
            settings.setAnimationDuration((int) animationDurationSlider.getValue());
            settings.setAlwaysOnTop(alwaysOnTopCheckBox.isSelected());
            settings.setClickThrough(clickThroughCheckBox.isSelected());

            // 4. Lưu vào file vật lý thông qua ConfigManager
            ConfigManager.getInstance().saveSettings();

            // 5. Áp dụng trực tiếp lên giao diện chính
            // Đảm bảo MainController có phương thức PUBLIC applySettings()
            if (mainController != null) {
                mainController.applySettings();
            }

            closeDialog();
        } catch (Exception e) {
            logger.error("Failed to save settings", e);
            showError("Save Error", "Could not save settings: " + e.getMessage());
        }
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    private void closeDialog() {
        if (saveButton.getScene() != null && saveButton.getScene().getWindow() != null) {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}