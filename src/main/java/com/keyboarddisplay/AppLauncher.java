package com.keyboarddisplay;

/**
 * Lớp khởi chạy trung gian để tránh lỗi JavaFX Runtime Components Missing
 */
public class AppLauncher {
    public static void main(String[] args) {
        // Gọi trực tiếp main của class Main cũ
        Main.main(args);
    }
}