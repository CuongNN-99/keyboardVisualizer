package com.keyboarddisplay.tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple utility to generate PNG icons (16x, 32x, 64x) programmatically
 * Draws a stylized keyboard-like motif so the app can load PNG icons from resources.
 */
public class IconPngGenerator {
    public static void main(String[] args) {
        try {
            Path srcIcons = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "icons");
            Path targetIcons = Paths.get(System.getProperty("user.dir"), "target", "classes", "icons");
            if (Files.notExists(srcIcons)) Files.createDirectories(srcIcons);
            if (Files.notExists(targetIcons)) Files.createDirectories(targetIcons);

            int[] sizes = {16, 32, 64};
            for (int s : sizes) {
                BufferedImage img = new BufferedImage(s, s, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = img.createGraphics();
                try {
                    // background
                    g.setColor(new Color(30, 30, 30));
                    g.fillRect(0, 0, s, s);

                    // draw keyboard keys in grid
                    int cols = 4;
                    int rows = 3;
                    int gap = Math.max(1, s / 24);
                    int keySize = (s - (cols + 1) * gap) / cols;
                    int startX = gap;
                    int startY = Math.max(1, s / 6);

                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            int x = startX + c * (keySize + gap);
                            int y = startY + r * (keySize + gap);
                            g.setColor(new Color(70, 70, 70));
                            g.fillRoundRect(x, y, keySize, keySize, Math.max(2, keySize/4), Math.max(2, keySize/4));
                            g.setColor(new Color(110, 110, 110));
                            g.setStroke(new BasicStroke(Math.max(1, s/64)));
                            g.drawRoundRect(x, y, keySize, keySize, Math.max(2, keySize/4), Math.max(2, keySize/4));
                        }
                    }

                    // highlight middle key red
                    int midRow = 1, midCol = 1;
                    int mx = startX + midCol * (keySize + gap);
                    int my = startY + midRow * (keySize + gap);
                    g.setColor(new Color(220, 60, 60));
                    g.fillRoundRect(mx, my, keySize, keySize, Math.max(2, keySize/4), Math.max(2, keySize/4));
                    g.setColor(new Color(255, 180, 180));
                    g.drawRoundRect(mx, my, keySize, keySize, Math.max(2, keySize/4), Math.max(2, keySize/4));
                } finally {
                    g.dispose();
                }

                File f1 = srcIcons.resolve(String.format("KeyboardDisplay-%d.png", s)).toFile();
                File f2 = targetIcons.resolve(String.format("KeyboardDisplay-%d.png", s)).toFile();
                ImageIO.write(img, "png", f1);
                ImageIO.write(img, "png", f2);
                System.out.println("Wrote: " + f1.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }
}
