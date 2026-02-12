package com.keyboarddisplay.controller;

import com.keyboarddisplay.model.AppSettings;
import com.keyboarddisplay.util.ConfigManager;
import com.keyboarddisplay.util.Constants;
import com.keyboarddisplay.view.KeyButton;
import com.keyboarddisplay.view.KeyboardLayout;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private ScheduledExecutorService mouseCheckService;
    private boolean currentNativeTransparent = false;

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

                stage.widthProperty().addListener((obs, old, val) -> updateKeyboardScale());
                stage.heightProperty().addListener((obs, old, val) -> updateKeyboardScale());

                // FIX: Thiết lập con trỏ chuột bằng Java thay vì FXML để tránh lỗi crash
                if (resizeHandle != null) {
                    resizeHandle.setCursor(Cursor.NW_RESIZE);
                }

                updateKeyboardScale();
                startMouseHitTestService();
            }
        });

        applyGlobalStyles();
        createKeyboardLayout();
        setupWindowControls();
        setupButtons();
    }

    private void startMouseHitTestService() {
        mouseCheckService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        mouseCheckService.scheduleAtFixedRate(() -> {
            if (!settings.isClickThrough()) {
                if (currentNativeTransparent) setNativeClickThrough(false);
                return;
            }

            Platform.runLater(() -> {
                Stage stage = getStage();
                if (stage == null || !stage.isShowing() || stage.isIconified()) return;

                WinDef.POINT pt = new WinDef.POINT();
                User32.INSTANCE.GetCursorPos(pt);

                double mouseX = pt.x - stage.getX();
                double mouseY = pt.y - stage.getY();

                // Kiểm tra va chạm: TopBar hoặc Nút kéo góc
                boolean overTopBar = isMouseOverNode(topBar, mouseX, mouseY);
                boolean overResize = isMouseOverNode(resizeHandle, mouseX, mouseY);

                // Nếu chuột chạm vùng điều khiển thì tắt xuyên thấu
                boolean shouldBeTransparent = !(overTopBar || overResize);

                if (shouldBeTransparent != currentNativeTransparent) {
                    setNativeClickThrough(shouldBeTransparent);
                }
            });
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    private boolean isMouseOverNode(javafx.scene.Node node, double sceneX, double sceneY) {
        if (node == null || !node.isVisible()) return false;
        Bounds bounds = node.localToScene(node.getBoundsInLocal());
        return bounds.contains(sceneX, sceneY);
    }

    private void setNativeClickThrough(boolean transparent) {
        currentNativeTransparent = transparent;
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("win")) return;

        Stage stage = getStage();
        if (stage == null) return;

        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, stage.getTitle());
        if (hwnd == null) return;

        int wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);
        if (transparent) {
            wl |= WinUser.WS_EX_TRANSPARENT | WinUser.WS_EX_LAYERED;
        } else {
            wl &= ~WinUser.WS_EX_TRANSPARENT;
        }
        User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl);
    }

    public void applySettings() {
        settings = ConfigManager.getInstance().getSettings();
        applyGlobalStyles();
        createKeyboardLayout();
        if (getStage() != null) {
            getStage().setAlwaysOnTop(settings.isAlwaysOnTop());
        }
    }

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

    private void updateKeyboardScale() {
        if (keyboardLayout == null || keyboardGroup == null) return;
        double containerWidth = keyboardContainer.getWidth();
        double containerHeight = keyboardContainer.getHeight();
        if (containerWidth <= 0 || containerHeight <= 0) return;

        double paddingH = 90.0, paddingV = 110.0;
        double scaleX = (containerWidth - paddingH) / Constants.STANDARD_WIDTH;
        double scaleY = (containerHeight - paddingV) / Constants.STANDARD_HEIGHT;
        double finalScale = Math.max(Math.min(scaleX, scaleY), 0.40);

        keyboardLayout.setScaleX(finalScale);
        keyboardLayout.setScaleY(finalScale);
        StackPane.setAlignment(keyboardGroup, Pos.CENTER);
        StackPane.setMargin(keyboardGroup, new Insets(25, 0, 0, 0));
    }

    private void createKeyboardLayout() {
        if (keyboardGroup == null) return;
        keyboardGroup.getChildren().clear();
        keyboardLayout = new KeyboardLayout(settings.getLayoutType());
        keyboardLayout.setMinWidth(Constants.STANDARD_WIDTH);
        keyboardLayout.setPrefWidth(Constants.STANDARD_WIDTH);
        keyboardLayout.setMaxWidth(Constants.STANDARD_WIDTH);
        keyboardLayout.setMinHeight(Constants.STANDARD_HEIGHT);
        keyboardLayout.setPrefHeight(Constants.STANDARD_HEIGHT);
        keyboardLayout.setMaxHeight(Constants.STANDARD_HEIGHT);

        Group wrapper = new Group(keyboardLayout);
        keyboardGroup.getChildren().add(wrapper);
        Platform.runLater(this::updateKeyboardScale);
    }

    private void applyGlobalStyles() {
        if (rootPane == null) return;
        String hex = settings.getBackgroundColor();
        double opacity = settings.getBackgroundOpacity();

        rootPane.setPickOnBounds(!settings.isClickThrough());
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
        topBar.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        topBar.setOnMouseDragged(e -> {
            Stage s = getStage();
            if (s != null && !isResizing) {
                s.setX(e.getScreenX() - xOffset);
                s.setY(e.getScreenY() - yOffset);
            }
        });

        if (closeButton != null) closeButton.setOnAction(e -> {
            if (mouseCheckService != null) mouseCheckService.shutdown();
            ConfigManager.getInstance().saveSettings();
            Platform.exit();
        });

        if (minimizeButton != null) minimizeButton.setOnAction(e -> getStage().setIconified(true));

        if (resizeHandle != null) {
            resizeHandle.setOnMousePressed(e -> {
                isResizing = true;
                e.consume();
            });

            // Resize logic: Gán trực tiếp lên rootPane để mượt hơn khi di chuyển nhanh
            rootPane.setOnMouseDragged(e -> {
                if (isResizing) {
                    Stage s = getStage();
                    if (s != null) {
                        double nw = Math.max(e.getScreenX() - s.getX(), Constants.MIN_WINDOW_WIDTH);
                        double nh = Math.max(e.getScreenY() - s.getY(), Constants.MIN_WINDOW_HEIGHT);
                        s.setWidth(nw);
                        s.setHeight(nh);
                    }
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