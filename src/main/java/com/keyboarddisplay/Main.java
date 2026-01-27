package com.keyboarddisplay;

import com.keyboarddisplay.controller.MainController;
import com.keyboarddisplay.service.GlobalKeyListener;
import com.keyboarddisplay.util.ConfigManager;
import com.keyboarddisplay.util.AppIconGenerator;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private GlobalKeyListener keyListener;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Ensure PNG icons exist in src/main/resources/icons/ and target/classes/icons/ for packaging and reliable loading
            try {
                Path srcIconsDir = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "icons");
                Path targetIconsDir = Paths.get(System.getProperty("user.dir"), "target", "classes", "icons");
                if (Files.notExists(srcIconsDir)) Files.createDirectories(srcIconsDir);
                if (Files.notExists(targetIconsDir)) Files.createDirectories(targetIconsDir);
                int[] sizes = {16, 32, 64};
                for (int s : sizes) {
                    Path pSrc = srcIconsDir.resolve(String.format("KeyboardDisplay-%d.png", s));
                    Path pTarget = targetIconsDir.resolve(String.format("KeyboardDisplay-%d.png", s));
                    if (Files.notExists(pSrc) || Files.notExists(pTarget)) {
                        javafx.scene.image.Image fx = AppIconGenerator.generateKeyboardIcon(s);
                        if (fx != null) {
                            BufferedImage bi = SwingFXUtils.fromFXImage(fx, null);
                            if (Files.notExists(pSrc)) ImageIO.write(bi, "png", pSrc.toFile());
                            if (Files.notExists(pTarget)) ImageIO.write(bi, "png", pTarget.toFile());
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("Could not generate icon PNGs into resources/ or target/classes/icons/", e);
            }

            // 1. Load FXML và lấy Controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            MainController controller = loader.getController();

            // 2. Khởi tạo và bắt đầu lắng nghe bàn phím hệ thống
            keyListener = new GlobalKeyListener(controller);
            keyListener.start();

            // 3. Thiết lập Scene (Trong suốt để làm Widget)
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setTitle("Keyboard Display Widget");
            primaryStage.setScene(scene);

            // --- Set application icons (multiple sizes) ---
            try {
                boolean addedAny = false;

                // 1) Try explicit canonical PNG via ImageIO (BufferedImage) for robust Taskbar icon setting
                try {
                    URL canonical = getClass().getResource("/icons/KeyboardDisplay.png");
                    if (canonical != null) {
                        BufferedImage bi = ImageIO.read(canonical);
                        if (bi != null) {
                            logger.info("Loaded canonical icon resource: /icons/KeyboardDisplay.png (w=" + bi.getWidth() + ", h=" + bi.getHeight() + ")");
                            // Add JavaFX Image for Stage
                            Image fx = SwingFXUtils.toFXImage(bi, null);
                            if (fx != null) primaryStage.getIcons().add(fx);

                            // Set AWT Taskbar icon on AWT event thread (safer)
                            try {
                                java.awt.EventQueue.invokeLater(() -> {
                                    try {
                                        java.awt.Taskbar.getTaskbar().setIconImage(bi);
                                        logger.info("AWT Taskbar icon set from canonical PNG.");
                                    } catch (Throwable t) {
                                        logger.warn("AWT Taskbar icon set failed", t);
                                    }
                                });
                            } catch (Throwable t) {
                                logger.warn("Could not invoke AWT event queue to set taskbar icon", t);
                            }
                            addedAny = true;
                        }
                    } else {
                        logger.debug("Canonical resource /icons/KeyboardDisplay.png not found in classpath");
                    }
                } catch (Exception ex) {
                    logger.debug("Could not load canonical PNG via ImageIO", ex);
                }

                // 2) Fallback: try sized PNGs and .ico as before
                String[] resourceCandidates = {"/icons/KeyboardDisplay-16.png", "/icons/KeyboardDisplay-32.png", "/icons/KeyboardDisplay-64.png", "/icons/KeyboardDisplay.ico"};
                for (String res : resourceCandidates) {
                    try (InputStream is = getClass().getResourceAsStream(res)) {
                        if (is != null) {
                            Image img = new Image(is);
                            if (img != null && img.getWidth() > 0) {
                                primaryStage.getIcons().add(img);
                                addedAny = true;
                                logger.info("Loaded fallback icon resource: " + res + " (w=" + img.getWidth() + ", h=" + img.getHeight() + ")");
                            }
                        }
                    } catch (Exception ignore) {
                        logger.debug("Failed to load resource " + res, ignore);
                    }
                }

                // If nothing was loaded from resources, fall back to generated icon
                if (!addedAny) {
                    Image fxIcon = AppIconGenerator.generateKeyboardIcon(64);
                    if (fxIcon != null) {
                        primaryStage.getIcons().add(fxIcon);
                        logger.info("No resource icons found; using generated icon (64)");
                        try {
                            BufferedImage bi = SwingFXUtils.fromFXImage(fxIcon, null);
                            if (bi != null) java.awt.EventQueue.invokeLater(() -> {
                                try { java.awt.Taskbar.getTaskbar().setIconImage(bi); logger.info("AWT Taskbar icon set from generated icon."); } catch (Throwable t) { logger.warn("AWT Taskbar icon set failed for generated icon", t); }
                            });
                        } catch (Throwable t) {
                            logger.warn("Failed to convert generated icon to AWT image", t);
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("Failed to set application/taskbar icon", e);
            }

            // Hiển thị cửa sổ
            primaryStage.show();

            logger.info("Application started successfully.");
        } catch (Exception e) {
            logger.error("Failed to start application", e);
        }
    }

    private static BufferedImage javafxImageToBufferedImage(Image fxImage) {
        if (fxImage == null) return null;
        int width = (int) Math.max(1, fxImage.getWidth());
        int height = (int) Math.max(1, fxImage.getHeight());
        javafx.scene.image.PixelReader reader = fxImage.getPixelReader();
        if (reader == null) return null;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] buffer = new int[width * height];
        reader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), buffer, 0, width);
        bufferedImage.setRGB(0, 0, width, height, buffer, 0, width);
        return bufferedImage;
    }

    @Override
    public void stop() {
        // CỰC KỲ QUAN TRỌNG: Phải dừng listener khi đóng app để giải phóng hook hệ thống
        if (keyListener != null) {
            keyListener.stop();
        }
        ConfigManager.getInstance().saveSettings();
        System.out.println("Application stopped.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

