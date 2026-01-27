package com.keyboarddisplay.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.keyboarddisplay.model.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    // Sử dụng thư mục người dùng để đảm bảo quyền ghi (Windows/Mac/Linux)
    private static final String CONFIG_DIR = System.getProperty("user.home") + File.separator + ".keyboarddisplay";
    private static final String CONFIG_FILE = CONFIG_DIR + File.separator + "settings.json";

    private static ConfigManager instance;
    private AppSettings settings;
    private final Gson gson;

    private ConfigManager() {
        // PrettyPrinting giúp file JSON dễ đọc khi mở bằng Notepad
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls() // Đảm bảo cấu trúc file JSON đầy đủ
                .create();
        this.settings = new AppSettings();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Tải cấu hình từ file. Nếu file lỗi hoặc không tồn tại, sử dụng mặc định.
     */
    public void loadSettings() {
        Path filePath = Paths.get(CONFIG_FILE);

        try {
            // Tạo thư mục nếu chưa có
            Files.createDirectories(filePath.getParent());

            if (Files.exists(filePath)) {
                // Sử dụng UTF_8 để tránh lỗi hiển thị ký tự đặc biệt
                try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
                    AppSettings loadedSettings = gson.fromJson(reader, AppSettings.class);

                    if (loadedSettings != null) {
                        this.settings = loadedSettings;
                        logger.info("Settings loaded successfully from: {}", CONFIG_FILE);
                    } else {
                        throw new IOException("JSON file is empty");
                    }
                } catch (Exception e) {
                    logger.error("Error parsing settings JSON, resetting to defaults: {}", e.getMessage());
                    this.settings = new AppSettings();
                    saveSettings(); // Ghi đè lại file lỗi bằng dữ liệu sạch
                }
            } else {
                logger.info("Settings file not found. Creating default at: {}", CONFIG_FILE);
                this.settings = new AppSettings();
                saveSettings();
            }
        } catch (IOException e) {
            logger.error("System error creating/reading config directory", e);
            this.settings = new AppSettings();
        }
    }

    /**
     * Lưu cấu hình hiện tại xuống đĩa.
     */
    public void saveSettings() {
        Path filePath = Paths.get(CONFIG_FILE);
        try {
            Files.createDirectories(filePath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                gson.toJson(settings, writer);
            }
            logger.info("Settings saved successfully.");
        } catch (IOException e) {
            logger.error("Could not save settings to file", e);
        }
    }

    public AppSettings getSettings() {
        // Tránh trả về null
        if (settings == null) settings = new AppSettings();
        return settings;
    }

    public void setSettings(AppSettings settings) {
        this.settings = settings;
    }
}