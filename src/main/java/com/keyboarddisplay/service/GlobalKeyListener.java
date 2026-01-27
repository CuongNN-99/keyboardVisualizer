package com.keyboarddisplay.service;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.keyboarddisplay.controller.MainController;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class GlobalKeyListener implements NativeKeyListener {
    private static final Logger logger = LoggerFactory.getLogger(GlobalKeyListener.class);
    private final MainController controller;
    private final Map<Integer, String> nativeToIdMap;

    public GlobalKeyListener(MainController controller) {
        this.controller = controller;
        this.nativeToIdMap = createKeyIdMapping();

        // Tắt log spam của JNativeHook
        java.util.logging.Logger jnhLogger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        jnhLogger.setLevel(Level.OFF);
    }

    public void start() {
        try {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.registerNativeHook();
            }
            GlobalScreen.addNativeKeyListener(this);
            logger.info("Global keyboard listener active.");
        } catch (NativeHookException e) {
            logger.error("Failed to register native hook: {}", e.getMessage());
        }
    }

    public void stop() {
        try {
            GlobalScreen.removeNativeKeyListener(this);
            if (GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.unregisterNativeHook();
            }
        } catch (NativeHookException e) {
            logger.error("Failed to unregister hook: {}", e.getMessage());
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String keyId = determineKeyId(e);
        if (keyId != null) {
            // Đảm bảo chạy trên JavaFX Application Thread
            Platform.runLater(() -> controller.handleKeyPressed(keyId));
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        String keyId = determineKeyId(e);
        if (keyId != null) {
            Platform.runLater(() -> controller.handleKeyReleased(keyId));
        }
    }

    private String determineKeyId(NativeKeyEvent e) {
        int code = e.getKeyCode();
        int location = e.getKeyLocation();

        // Xử lý các phím Modifier có vị trí Trái/Phải
        if (code == NativeKeyEvent.VC_SHIFT)
            return (location == NativeKeyEvent.KEY_LOCATION_RIGHT) ? "SHIFT_R" : "SHIFT_L";
        if (code == NativeKeyEvent.VC_CONTROL)
            return (location == NativeKeyEvent.KEY_LOCATION_RIGHT) ? "CONTROL_R" : "CONTROL_L";
        if (code == NativeKeyEvent.VC_ALT)
            return (location == NativeKeyEvent.KEY_LOCATION_RIGHT) ? "ALT_R" : "ALT_L";
        if (code == NativeKeyEvent.VC_META)
            return (location == NativeKeyEvent.KEY_LOCATION_RIGHT) ? "META_R" : "META_L";

        return nativeToIdMap.get(code);
    }

    private Map<Integer, String> createKeyIdMapping() {
        Map<Integer, String> map = new HashMap<>();

        // 1. Chữ cái A-Z
        map.put(NativeKeyEvent.VC_A, "A"); map.put(NativeKeyEvent.VC_B, "B"); map.put(NativeKeyEvent.VC_C, "C");
        map.put(NativeKeyEvent.VC_D, "D"); map.put(NativeKeyEvent.VC_E, "E"); map.put(NativeKeyEvent.VC_F, "F");
        map.put(NativeKeyEvent.VC_G, "G"); map.put(NativeKeyEvent.VC_H, "H"); map.put(NativeKeyEvent.VC_I, "I");
        map.put(NativeKeyEvent.VC_J, "J"); map.put(NativeKeyEvent.VC_K, "K"); map.put(NativeKeyEvent.VC_L, "L");
        map.put(NativeKeyEvent.VC_M, "M"); map.put(NativeKeyEvent.VC_N, "N"); map.put(NativeKeyEvent.VC_O, "O");
        map.put(NativeKeyEvent.VC_P, "P"); map.put(NativeKeyEvent.VC_Q, "Q"); map.put(NativeKeyEvent.VC_R, "R");
        map.put(NativeKeyEvent.VC_S, "S"); map.put(NativeKeyEvent.VC_T, "T"); map.put(NativeKeyEvent.VC_U, "U");
        map.put(NativeKeyEvent.VC_V, "V"); map.put(NativeKeyEvent.VC_W, "W"); map.put(NativeKeyEvent.VC_X, "X");
        map.put(NativeKeyEvent.VC_Y, "Y"); map.put(NativeKeyEvent.VC_Z, "Z");

        // 2. Số hàng ngang
        map.put(NativeKeyEvent.VC_1, "1"); map.put(NativeKeyEvent.VC_2, "2");
        map.put(NativeKeyEvent.VC_3, "3"); map.put(NativeKeyEvent.VC_4, "4");
        map.put(NativeKeyEvent.VC_5, "5"); map.put(NativeKeyEvent.VC_6, "6");
        map.put(NativeKeyEvent.VC_7, "7"); map.put(NativeKeyEvent.VC_8, "8");
        map.put(NativeKeyEvent.VC_9, "9"); map.put(NativeKeyEvent.VC_0, "0");

        // 3. Phím chức năng F1-F12
        for (int i = 1; i <= 12; i++) {
            try {
                int code = NativeKeyEvent.class.getField("VC_F" + i).getInt(null);
                map.put(code, "F" + i);
            } catch (Exception ignored) {}
        }

        // 4. KHU VỰC NUMPAD - SỬ DỤNG MÃ HEX TRỰC TIẾP ĐỂ TRÁNH LỖI "SYMBOL NOT FOUND"
        map.put(0x0052, "KP_0"); map.put(0x004F, "KP_1"); map.put(0x0050, "KP_2");
        map.put(0x0051, "KP_3"); map.put(0x004B, "KP_4"); map.put(0x004C, "KP_5");
        map.put(0x004D, "KP_6"); map.put(0x0047, "KP_7"); map.put(0x0048, "KP_8");
        map.put(0x0049, "KP_9");

        map.put(0x0037, "KP_MULTIPLY"); // *
        map.put(0x004E, "KP_ADD");      // +
        map.put(0x004A, "KP_SUBTRACT"); // -
        map.put(0x0053, "KP_DECIMAL");  // .
        map.put(0x0E0F, "KP_DIVIDE");   // /
        map.put(0x0E1C, "KP_ENTER");

        // 5. Ký tự đặc biệt (Sử dụng mã Hex cho dấu huyền/ngã gây lỗi)
        map.put(0x0029, "BACK_QUOTE"); // Phím `
        map.put(NativeKeyEvent.VC_MINUS, "MINUS");
        map.put(NativeKeyEvent.VC_EQUALS, "EQUALS");
        map.put(NativeKeyEvent.VC_OPEN_BRACKET, "OPEN_BRACKET");
        map.put(NativeKeyEvent.VC_CLOSE_BRACKET, "CLOSE_BRACKET");
        map.put(NativeKeyEvent.VC_BACK_SLASH, "BACK_SLASH");
        map.put(NativeKeyEvent.VC_SEMICOLON, "SEMICOLON");
        map.put(NativeKeyEvent.VC_QUOTE, "QUOTE");
        map.put(NativeKeyEvent.VC_COMMA, "COMMA");
        map.put(NativeKeyEvent.VC_PERIOD, "PERIOD");
        map.put(NativeKeyEvent.VC_SLASH, "SLASH");

        // 6. Điều hướng & Hệ thống
        map.put(NativeKeyEvent.VC_ESCAPE, "ESCAPE");
        map.put(NativeKeyEvent.VC_BACKSPACE, "BACKSPACE");
        map.put(NativeKeyEvent.VC_TAB, "TAB");
        map.put(NativeKeyEvent.VC_ENTER, "ENTER");
        map.put(NativeKeyEvent.VC_SPACE, "SPACE");
        map.put(NativeKeyEvent.VC_CAPS_LOCK, "CAPS_LOCK");
        map.put(NativeKeyEvent.VC_UP, "UP");
        map.put(NativeKeyEvent.VC_DOWN, "DOWN");
        map.put(NativeKeyEvent.VC_LEFT, "LEFT");
        map.put(NativeKeyEvent.VC_RIGHT, "RIGHT");
        map.put(NativeKeyEvent.VC_INSERT, "INSERT");
        map.put(NativeKeyEvent.VC_DELETE, "DELETE");
        map.put(NativeKeyEvent.VC_HOME, "HOME");
        map.put(NativeKeyEvent.VC_END, "END");
        map.put(NativeKeyEvent.VC_PAGE_UP, "PAGE_UP");
        map.put(NativeKeyEvent.VC_PAGE_DOWN, "PAGE_DOWN");

        return map;
    }
}