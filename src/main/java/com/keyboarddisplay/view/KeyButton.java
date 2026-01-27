package com.keyboarddisplay.view;

import com.keyboarddisplay.model.KeyConfig;
import com.keyboarddisplay.util.ConfigManager;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class KeyButton extends StackPane {
    private final KeyConfig keyConfig;
    private final Rectangle background;
    private final Label label;
    private boolean pressed = false;

    private Timeline pulseAnimation;
    private FadeTransition fadeTransition;

    public KeyButton(KeyConfig keyConfig) {
        this.keyConfig = keyConfig;

        // Gán StyleClass để quản lý qua CSS
        this.getStyleClass().add("key-button");

        background = new Rectangle();
        background.setArcWidth(10); // Bo góc hiện đại hơn
        background.setArcHeight(10);

        // Đảm bảo background luôn co giãn theo StackPane
        background.widthProperty().bind(this.widthProperty());
        background.heightProperty().bind(this.heightProperty());

        // Thiết lập kích thước từ config
        this.setPrefSize(keyConfig.getWidth(), keyConfig.getHeight());
        this.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        this.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        label = new Label(keyConfig.getLabel());
        label.getStyleClass().add("key-label");

        // Đảm bảo chữ luôn ở giữa và có màu sáng nếu CSS chưa load
        label.setTextFill(Color.WHITE);

        this.getChildren().addAll(background, label);

        setupAnimations();
        updateStyle(); // Áp dụng giao diện ban đầu
    }

    private void setupAnimations() {
        // Hiệu ứng co giãn khi nhấn
        pulseAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(this.scaleXProperty(), 1.0),
                        new KeyValue(this.scaleYProperty(), 1.0)),
                new KeyFrame(Duration.millis(50),
                        new KeyValue(this.scaleXProperty(), 0.92),
                        new KeyValue(this.scaleYProperty(), 0.92)),
                new KeyFrame(Duration.millis(150),
                        new KeyValue(this.scaleXProperty(), 1.0),
                        new KeyValue(this.scaleYProperty(), 1.0))
        );

        fadeTransition = new FadeTransition();
        fadeTransition.setNode(background);
    }

    public void keyPressed() {
        if (pressed) return;
        pressed = true;

        pulseAnimation.stop();
        fadeTransition.stop();

        background.setOpacity(1.0);

        // Lấy màu highlight từ cấu hình người dùng
        Color highlightColor = ConfigManager.getInstance().getSettings().getHighlightColorAsColor();
        double settingsOpacity = ConfigManager.getInstance().getSettings().getOpacity();

        String hexColor = String.format("#%02x%02x%02x",
                (int)(highlightColor.getRed() * 255),
                (int)(highlightColor.getGreen() * 255),
                (int)(highlightColor.getBlue() * 255));

        // Khi nhấn: Đổi màu nền rực rỡ và thêm viền trắng sáng
        background.setStyle(String.format(
                "-fx-fill: %s; -fx-opacity: %.2f; -fx-stroke: #FFFFFF; -fx-stroke-width: 2;",
                hexColor, settingsOpacity
        ));

        pulseAnimation.playFromStart();
    }

    public void keyReleased() {
        if (!pressed) return;
        pressed = false;

        int duration = ConfigManager.getInstance().getSettings().getAnimationDuration();

        fadeTransition.setDuration(Duration.millis(duration));
        fadeTransition.setFromValue(background.getOpacity());
        fadeTransition.setToValue(0.3); // Mờ hẳn đi sau khi thả

        fadeTransition.setOnFinished(e -> updateStyle());
        fadeTransition.playFromStart();
    }

    public void updateStyle() {
        if (pressed) return;

        // Trạng thái mặc định: Màu xám đậm, viền mờ nhẹ để thấy rõ layout
        background.setStyle("-fx-fill: rgba(45, 45, 45, 0.8); -fx-stroke: rgba(255, 255, 255, 0.2); -fx-stroke-width: 1;");
        background.setOpacity(1.0);
        this.setScaleX(1.0);
        this.setScaleY(1.0);
    }

    public String getKeyId() { return keyConfig.getKeyId(); }
    public void setKeyVisible(boolean visible) { this.setVisible(visible); }
}