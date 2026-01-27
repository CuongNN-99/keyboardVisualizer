package com.keyboarddisplay.view;

import com.keyboarddisplay.model.AppSettings;
import com.keyboarddisplay.model.KeyConfig;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardLayout extends Pane {
    private final Map<String, KeyButton> keyButtons = new HashMap<>();

    private static final double KEY_SIZE = 50;
    private static final double KEY_GAP = 4;

    // Kích thước chuẩn thiết kế
    public static final double STANDARD_WIDTH = 1100.0;
    public static final double STANDARD_HEIGHT = 450.0;

    public KeyboardLayout(AppSettings.KeyboardLayoutType layoutType) {
        // 1. Reset trạng thái
        this.getChildren().clear();
        this.keyButtons.clear();

        // 2. Cấu hình Pane - Đảm bảo Pane có kích thước để chứa các phím
        this.setPrefSize(STANDARD_WIDTH, STANDARD_HEIGHT);
        this.setMinSize(STANDARD_WIDTH, STANDARD_HEIGHT);
        this.setMaxSize(STANDARD_WIDTH, STANDARD_HEIGHT);

        // Style lớp nền
        this.getStyleClass().add("keyboard-layout-pane");
        this.setStyle("-fx-background-color: transparent;"); // Đảm bảo không bị đè bởi màu mặc định

        // 3. Xây dựng phím
        List<KeyConfig> keyConfigs = generateKeyConfigs(layoutType);
        buildLayout(keyConfigs);
    }

    private void buildLayout(List<KeyConfig> keyConfigs) {
        for (KeyConfig config : keyConfigs) {
            KeyButton button = new KeyButton(config);

            // Set vị trí tuyệt đối trong Pane
            button.setLayoutX(config.getX());
            button.setLayoutY(config.getY());

            // Đảm bảo nút có kích thước đúng
            button.setPrefSize(config.getWidth(), config.getHeight());

            keyButtons.put(config.getKeyId(), button);

            // Thêm trực tiếp vào Pane (this) thay vì qua Group để tránh lỗi render
            this.getChildren().add(button);
        }
    }

    private List<KeyConfig> generateKeyConfigs(AppSettings.KeyboardLayoutType layoutType) {
        List<KeyConfig> configs = new ArrayList<>();
        switch (layoutType) {
            case WASD: configs.addAll(generateWASDKeys()); break;
            case ARROWS: configs.addAll(generateArrowKeys()); break;
            case NUMPAD: configs.addAll(generateNumpadKeys()); break;
            case FULL_87:
            default: configs.addAll(generateFull87Keys()); break;
        }
        return configs;
    }

    private List<KeyConfig> generateFull87Keys() {
        List<KeyConfig> keys = new ArrayList<>();
        double startX = 20;
        double startY = 20;
        double x, y;

        // Row 0: F-Keys
        y = startY; x = startX;
        keys.add(new KeyConfig("Esc", KeyCode.ESCAPE, "ESCAPE", x, y, KEY_SIZE, KEY_SIZE, "function"));
        x += KEY_SIZE + KEY_GAP * 3;
        for (int i = 1; i <= 12; i++) {
            keys.add(new KeyConfig("F" + i, KeyCode.valueOf("F" + i), "F" + i, x, y, KEY_SIZE, KEY_SIZE, "function"));
            x += KEY_SIZE + KEY_GAP;
            if (i == 4 || i == 8) x += KEY_GAP * 2;
        }

        // Row 1: Numbers & Symbols
        y += KEY_SIZE + KEY_GAP * 2; x = startX;
        String[] row1Labels = {"~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "Bksp"};
        String[] row1Ids = {"BACK_QUOTE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "MINUS", "EQUALS", "BACKSPACE"};
        for (int i = 0; i < row1Labels.length - 1; i++) {
            keys.add(new KeyConfig(row1Labels[i], KeyCode.UNDEFINED, row1Ids[i], x, y, KEY_SIZE, KEY_SIZE));
            x += KEY_SIZE + KEY_GAP;
        }
        keys.add(new KeyConfig("Bksp", KeyCode.BACK_SPACE, "BACKSPACE", x, y, KEY_SIZE * 2, KEY_SIZE));

        // Row 2: QWERTY
        y += KEY_SIZE + KEY_GAP; x = startX;
        keys.add(new KeyConfig("Tab", KeyCode.TAB, "TAB", x, y, KEY_SIZE * 1.5, KEY_SIZE));
        x += KEY_SIZE * 1.5 + KEY_GAP;
        String[] row2 = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"};
        String[] row2Ids = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "OPEN_BRACKET", "CLOSE_BRACKET", "BACK_SLASH"};
        for (int i = 0; i < row2.length; i++) {
            keys.add(new KeyConfig(row2[i], KeyCode.UNDEFINED, row2Ids[i], x, y, KEY_SIZE, KEY_SIZE));
            x += KEY_SIZE + KEY_GAP;
        }

        // Row 3: ASDF
        y += KEY_SIZE + KEY_GAP; x = startX;
        keys.add(new KeyConfig("Caps", KeyCode.CAPS, "CAPS_LOCK", x, y, KEY_SIZE * 1.75, KEY_SIZE));
        x += KEY_SIZE * 1.75 + KEY_GAP;
        String[] row3 = {"A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "'"};
        String[] row3Ids = {"A", "S", "D", "F", "G", "H", "J", "K", "L", "SEMICOLON", "QUOTE"};
        for (int i = 0; i < row3.length; i++) {
            keys.add(new KeyConfig(row3[i], KeyCode.UNDEFINED, row3Ids[i], x, y, KEY_SIZE, KEY_SIZE));
            x += KEY_SIZE + KEY_GAP;
        }
        keys.add(new KeyConfig("Enter", KeyCode.ENTER, "ENTER", x, y, KEY_SIZE * 2.25, KEY_SIZE));

        // Row 4: Shift
        y += KEY_SIZE + KEY_GAP; x = startX;
        keys.add(new KeyConfig("Shift", KeyCode.SHIFT, "SHIFT_L", x, y, KEY_SIZE * 2.25, KEY_SIZE));
        x += KEY_SIZE * 2.25 + KEY_GAP;
        String[] row4 = {"Z", "X", "C", "V", "B", "N", "M", ",", ".", "/"};
        String[] row4Ids = {"Z", "X", "C", "V", "B", "N", "M", "COMMA", "PERIOD", "SLASH"};
        for (int i = 0; i < row4.length; i++) {
            keys.add(new KeyConfig(row4[i], KeyCode.UNDEFINED, row4Ids[i], x, y, KEY_SIZE, KEY_SIZE));
            x += KEY_SIZE + KEY_GAP;
        }
        keys.add(new KeyConfig("Shift", KeyCode.SHIFT, "SHIFT_R", x, y, KEY_SIZE * 2.75, KEY_SIZE));

        // Row 5: Modifiers
        y += KEY_SIZE + KEY_GAP; x = startX;
        keys.add(new KeyConfig("Ctrl", KeyCode.CONTROL, "CONTROL_L", x, y, KEY_SIZE * 1.25, KEY_SIZE));
        x += KEY_SIZE * 1.25 + KEY_GAP;
        keys.add(new KeyConfig("Win", KeyCode.WINDOWS, "META_L", x, y, KEY_SIZE * 1.25, KEY_SIZE));
        x += KEY_SIZE * 1.25 + KEY_GAP;
        keys.add(new KeyConfig("Alt", KeyCode.ALT, "ALT_L", x, y, KEY_SIZE * 1.25, KEY_SIZE));
        x += KEY_SIZE * 1.25 + KEY_GAP;
        keys.add(new KeyConfig("Space", KeyCode.SPACE, "SPACE", x, y, KEY_SIZE * 6.25, KEY_SIZE));
        x += KEY_SIZE * 6.25 + KEY_GAP;
        keys.add(new KeyConfig("Alt", KeyCode.ALT, "ALT_R", x, y, KEY_SIZE * 1.25, KEY_SIZE));
        x += KEY_SIZE * 1.25 + KEY_GAP;
        keys.add(new KeyConfig("Ctrl", KeyCode.CONTROL, "CONTROL_R", x, y, KEY_SIZE * 1.25, KEY_SIZE));

        // Nav Cluster
        double navX = startX + (KEY_SIZE + KEY_GAP) * 15.3;
        double navY = startY + KEY_SIZE + KEY_GAP * 2;
        keys.add(new KeyConfig("Ins", KeyCode.INSERT, "INSERT", navX, navY, KEY_SIZE, KEY_SIZE, "navigation"));
        keys.add(new KeyConfig("Hm", KeyCode.HOME, "HOME", navX + (KEY_SIZE + KEY_GAP), navY, KEY_SIZE, KEY_SIZE, "navigation"));
        keys.add(new KeyConfig("PU", KeyCode.PAGE_UP, "PAGE_UP", navX + (KEY_SIZE + KEY_GAP) * 2, navY, KEY_SIZE, KEY_SIZE, "navigation"));

        navY += KEY_SIZE + KEY_GAP;
        keys.add(new KeyConfig("Del", KeyCode.DELETE, "DELETE", navX, navY, KEY_SIZE, KEY_SIZE, "navigation"));
        keys.add(new KeyConfig("End", KeyCode.END, "END", navX + (KEY_SIZE + KEY_GAP), navY, KEY_SIZE, KEY_SIZE, "navigation"));
        keys.add(new KeyConfig("PD", KeyCode.PAGE_DOWN, "PAGE_DOWN", navX + (KEY_SIZE + KEY_GAP) * 2, navY, KEY_SIZE, KEY_SIZE, "navigation"));

        navY += KEY_SIZE + KEY_GAP * 1.5;
        keys.add(new KeyConfig("↑", KeyCode.UP, "UP", navX + (KEY_SIZE + KEY_GAP), navY, KEY_SIZE, KEY_SIZE, "navigation"));
        navY += KEY_SIZE + KEY_GAP;
        keys.add(new KeyConfig("←", KeyCode.LEFT, "LEFT", navX, navY, KEY_SIZE, KEY_SIZE, "navigation"));
        keys.add(new KeyConfig("↓", KeyCode.DOWN, "DOWN", navX + (KEY_SIZE + KEY_GAP), navY, KEY_SIZE, KEY_SIZE, "navigation"));
        keys.add(new KeyConfig("→", KeyCode.RIGHT, "RIGHT", navX + (KEY_SIZE + KEY_GAP) * 2, navY, KEY_SIZE, KEY_SIZE, "navigation"));

        return keys;
    }

    private List<KeyConfig> generateNumpadKeys() {
        List<KeyConfig> keys = new ArrayList<>();
        double startX = (STANDARD_WIDTH - (KEY_SIZE * 4 + KEY_GAP * 3)) / 2;
        double startY = (STANDARD_HEIGHT - (KEY_SIZE * 5 + KEY_GAP * 4)) / 2;

        String[][] numpad = {
                {"KP_7", "KP_8", "KP_9", "KP_DIVIDE"},
                {"KP_4", "KP_5", "KP_6", "KP_MULTIPLY"},
                {"KP_1", "KP_2", "KP_3", "KP_SUBTRACT"},
                {"KP_0", "KP_DECIMAL", "KP_ENTER", "KP_ADD"}
        };

        for (int r = 0; r < numpad.length; r++) {
            for (int c = 0; c < numpad[r].length; c++) {
                String id = numpad[r][c];
                String label = id.replace("KP_", "").replace("MULTIPLY", "*").replace("DIVIDE", "/").replace("SUBTRACT", "-").replace("ADD", "+").replace("DECIMAL", ".");
                keys.add(new KeyConfig(label, KeyCode.UNDEFINED, id,
                        startX + c * (KEY_SIZE + KEY_GAP),
                        startY + r * (KEY_SIZE + KEY_GAP),
                        KEY_SIZE, KEY_SIZE, "numpad"));
            }
        }
        return keys;
    }

    private List<KeyConfig> generateWASDKeys() {
        List<KeyConfig> keys = new ArrayList<>();
        double centerX = (STANDARD_WIDTH - (KEY_SIZE * 3 + KEY_GAP * 2)) / 2;
        double centerY = (STANDARD_HEIGHT - (KEY_SIZE * 2 + KEY_GAP)) / 2;
        keys.add(new KeyConfig("W", KeyCode.W, "W", centerX + (KEY_SIZE + KEY_GAP), centerY, KEY_SIZE, KEY_SIZE));
        keys.add(new KeyConfig("A", KeyCode.A, "A", centerX, centerY + (KEY_SIZE + KEY_GAP), KEY_SIZE, KEY_SIZE));
        keys.add(new KeyConfig("S", KeyCode.S, "S", centerX + (KEY_SIZE + KEY_GAP), centerY + (KEY_SIZE + KEY_GAP), KEY_SIZE, KEY_SIZE));
        keys.add(new KeyConfig("D", KeyCode.D, "D", centerX + (KEY_SIZE + KEY_GAP) * 2, centerY + (KEY_SIZE + KEY_GAP), KEY_SIZE, KEY_SIZE));
        return keys;
    }

    private List<KeyConfig> generateArrowKeys() {
        List<KeyConfig> keys = new ArrayList<>();
        double centerX = (STANDARD_WIDTH - (KEY_SIZE * 3 + KEY_GAP * 2)) / 2;
        double centerY = (STANDARD_HEIGHT - (KEY_SIZE * 2 + KEY_GAP)) / 2;
        keys.add(new KeyConfig("↑", KeyCode.UP, "UP", centerX + (KEY_SIZE + KEY_GAP), centerY, KEY_SIZE, KEY_SIZE));
        keys.add(new KeyConfig("←", KeyCode.LEFT, "LEFT", centerX, centerY + (KEY_SIZE + KEY_GAP), KEY_SIZE, KEY_SIZE));
        keys.add(new KeyConfig("↓", KeyCode.DOWN, "DOWN", centerX + (KEY_SIZE + KEY_GAP), centerY + (KEY_SIZE + KEY_GAP), KEY_SIZE, KEY_SIZE));
        keys.add(new KeyConfig("→", KeyCode.RIGHT, "RIGHT", centerX + (KEY_SIZE + KEY_GAP) * 2, centerY + (KEY_SIZE + KEY_GAP), KEY_SIZE, KEY_SIZE));
        return keys;
    }

    public KeyButton getKeyButton(String keyId) { return keyButtons.get(keyId); }
    public Map<String, KeyButton> getKeyButtons() { return keyButtons; }
}