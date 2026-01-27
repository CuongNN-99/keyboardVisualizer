package com.keyboarddisplay.util;

import java.io.File;

public class Constants {
    // Thông tin ứng dụng
    public static final String APP_NAME = "Keyboard Display";
    public static final String APP_VERSION = "1.1.0";
    public static final String APP_AUTHOR = "Senior Full Stack Developer";

    // Cấu hình Cửa sổ
    public static final int MIN_WINDOW_WIDTH = 850;
    public static final int MIN_WINDOW_HEIGHT = 350;
    public static final int DEFAULT_WINDOW_WIDTH = 950;
    public static final int DEFAULT_WINDOW_HEIGHT = 380;
    public static final double ASPECT_RATIO = (double) DEFAULT_WINDOW_WIDTH / DEFAULT_WINDOW_HEIGHT;

    // Kích thước chuẩn của bản vẽ thiết kế (Canvas)
    // Dùng để làm mốc tính toán tỉ lệ Scale khi co giãn cửa sổ
    public static final double STANDARD_WIDTH = 1100.0;
    public static final double STANDARD_HEIGHT = 360.0;

    // Quản lý file cấu hình
    public static final String CONFIG_DIR_NAME = ".keyboarddisplay";
    public static final String CONFIG_FILE_NAME = "settings.json";

    public static String getConfigDir() {
        return System.getProperty("user.home") + File.separator + CONFIG_DIR_NAME;
    }
    public static String getConfigFile() {
        return getConfigDir() + File.separator + CONFIG_FILE_NAME;
    }

    private Constants() { throw new AssertionError(); }
}