package com.keyboarddisplay.controller;

import com.keyboarddisplay.model.AppSettings;
import com.keyboarddisplay.util.ConfigManager;
import com.keyboarddisplay.util.Constants;
import com.keyboarddisplay.view.KeyButton;
import com.keyboarddisplay.view.KeyboardLayout;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML private StackPane rootPane;
    @FXML private StackPane keyboardContainer;
    @FXML private StackPane keyboardGroup;
    @FXML private Button settingsButton;
    @FXML private Button closeButton;
    @FXML private Button minimizeButton;
    @FXML private HBox topBar;
    @FXML private Region resizeHandle;

    private KeyboardLayout keyboardLayout;
    private AppSettings settings;
    private double xOffset = 0, yOffset = 0;
    private boolean isResizing = false;

    @FXML
    public void initialize() {
        settings = ConfigManager.getInstance().getSettings();

        Platform.runLater(() -> {
            Stage stage = getStage();
            if (stage != null) {
                stage.setMinWidth(Constants.MIN_WINDOW_WIDTH);
                stage.setMinHeight(Constants.MIN_WINDOW_HEIGHT);
                stage.setWidth(Constants.DEFAULT_WINDOW_WIDTH);
                stage.setHeight(Constants.DEFAULT_WINDOW_HEIGHT);

                // Đồng bộ scale khi người dùng kéo giãn cửa sổ
                stage.widthProperty().addListener((obs, old, val) -> updateKeyboardScale());
                stage.heightProperty().addListener((obs, old, val) -> updateKeyboardScale());

                updateKeyboardScale();
            }
        });

        applyGlobalStyles();
        createKeyboardLayout();
        setupWindowControls();
        setupButtons();
    }

    /**
     * GIẢI QUYẾT LỖI BIÊN DỊCH: Phương thức cần public để GlobalKeyListener truy cập
     */
    public void handleKeyPressed(String keyId) {
        if (keyboardLayout != null) {
            KeyButton b = keyboardLayout.getKeyButton(keyId);
            if (b != null) b.keyPressed();
        }
    }

    public void handleKeyReleased(String keyId) {
        if (keyboardLayout != null) {
            KeyButton b = keyboardLayout.getKeyButton(keyId);
            if (b != null) b.keyReleased();
        }
    }

    /**
     * GIẢI QUYẾT LỖI TRONG SETTINGS: Cập nhật lại UI khi nhấn Save
     */
    public void applySettings() {
        settings = ConfigManager.getInstance().getSettings();
        applyGlobalStyles();
        createKeyboardLayout();
        if (getStage() != null) {
            getStage().setAlwaysOnTop(settings.isAlwaysOnTop());
        }
    }

    /**
     * CHIẾN THUẬT CHỐNG CẮT BIÊN:
     * Scale Node bên trong một Group giúp StackPane tính toán đúng kích thước thực tế (Visual Bounds)
     */
    private void updateKeyboardScale() {
        if (keyboardLayout == null || keyboardGroup == null) return;

        double containerWidth = keyboardContainer.getWidth();
        double containerHeight = keyboardContainer.getHeight();
        if (containerWidth <= 0 || containerHeight <= 0) return;

        // Tạo vùng đệm an toàn xung quanh (Padding)
        double paddingH = 90.0;
        double paddingV = 110.0;

        double availW = containerWidth - paddingH;
        double availH = containerHeight - paddingV;

        double scaleX = availW / Constants.STANDARD_WIDTH;
        double scaleY = availH / Constants.STANDARD_HEIGHT;
        double finalScale = Math.min(scaleX, scaleY);

        // Không cho phép bàn phím nhỏ hơn 40%
        finalScale = Math.max(finalScale, 0.40);

        keyboardLayout.setScaleX(finalScale);
        keyboardLayout.setScaleY(finalScale);

        // Căn giữa Group chứa bàn phím
        StackPane.setAlignment(keyboardGroup, Pos.CENTER);

        // Dịch chuyển nhẹ xuống dưới để không bị sát TopBar quá
        StackPane.setMargin(keyboardGroup, new Insets(25, 0, 0, 0));
    }

    private void createKeyboardLayout() {
        if (keyboardGroup == null) return;
        keyboardGroup.getChildren().clear();

        keyboardLayout = new KeyboardLayout(settings.getLayoutType());

        // Đặt kích thước cố định theo hằng số chuẩn
        keyboardLayout.setMinWidth(Constants.STANDARD_WIDTH);
        keyboardLayout.setPrefWidth(Constants.STANDARD_WIDTH);
        keyboardLayout.setMaxWidth(Constants.STANDARD_WIDTH);
        keyboardLayout.setMinHeight(Constants.STANDARD_HEIGHT);
        keyboardLayout.setPrefHeight(Constants.STANDARD_HEIGHT);
        keyboardLayout.setMaxHeight(Constants.STANDARD_HEIGHT);

        // Group Wrapper là mấu chốt để fix lỗi lệch tọa độ khi scale
        Group wrapper = new Group(keyboardLayout);
        keyboardGroup.getChildren().add(wrapper);

        Platform.runLater(this::updateKeyboardScale);
    }

    private void applyGlobalStyles() {
        if (rootPane == null) return;
        String hex = settings.getBackgroundColor();
        double opacity = settings.getBackgroundOpacity();

        // Chuyển Hex sang RGBA để hỗ trợ độ mờ nền
        rootPane.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: 15; " +
                        "-fx-border-radius: 15; -fx-border-color: rgba(255,255,255,0.1);",
                hexToRgba(hex, opacity)
        ));
    }

    private String hexToRgba(String hex, double opacity) {
        if (hex.startsWith("#")) hex = hex.substring(1);
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return String.format("rgba(%d, %d, %d, %.2f)", r, g, b, opacity);
    }

    private void setupWindowControls() {
        topBar.setOnMousePressed(e -> { xOffset = e.getSceneX(); yOffset = e.getSceneY(); });
        topBar.setOnMouseDragged(e -> {
            Stage s = getStage();
            if (s != null) {
                s.setX(e.getScreenX() - xOffset);
                s.setY(e.getScreenY() - yOffset);
            }
        });

        if (closeButton != null) closeButton.setOnAction(e -> {
            ConfigManager.getInstance().saveSettings();
            Platform.exit();
        });
        if (minimizeButton != null) minimizeButton.setOnAction(e -> getStage().setIconified(true));

        if (resizeHandle != null) {
            resizeHandle.setOnMousePressed(e -> { isResizing = true; e.consume(); });
            rootPane.setOnMouseDragged(e -> {
                if (isResizing) {
                    Stage s = getStage();
                    double nw = Math.max(e.getScreenX() - s.getX(), Constants.MIN_WINDOW_WIDTH);
                    s.setWidth(nw);
                    s.setHeight(nw / Constants.ASPECT_RATIO);
                }
            });
            rootPane.setOnMouseReleased(e -> isResizing = false);
        }
    }

    private void setupButtons() {
        if (settingsButton != null) {
            settingsButton.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings.fxml"));
                    Scene scene = new Scene(loader.load());
                    Stage stage = new Stage(StageStyle.DECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.initOwner(getStage());

                    SettingsController sc = loader.getController();
                    sc.setMainController(this);
                    stage.showAndWait();
                } catch (IOException e) { logger.error("Fail to open Settings", e); }
            });
        }
    }

    private Stage getStage() {
        return (rootPane != null && rootPane.getScene() != null) ? (Stage) rootPane.getScene().getWindow() : null;
    }
}