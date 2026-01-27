package com.keyboarddisplay.service;

import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing keyboard event state and statistics
 * Tracks key press/release events and provides analytics
 *
 * File Location: src/main/java/com/keyboarddisplay/service/KeyEventService.java
 */
public class KeyEventService {
    private static final Logger logger = LoggerFactory.getLogger(KeyEventService.class);
    private static KeyEventService instance;

    // Track key press timestamps
    private final Map<KeyCode, Long> keyPressTimestamps = new ConcurrentHashMap<>();

    // Track key press counts
    private final Map<KeyCode, Integer> keyPressCount = new ConcurrentHashMap<>();

    // Track total time keys have been held down
    private final Map<KeyCode, Long> totalKeyPressTime = new ConcurrentHashMap<>();

    /**
     * Private constructor for singleton pattern
     */
    private KeyEventService() {
        logger.debug("KeyEventService initialized");
    }

    /**
     * Get singleton instance
     *
     * @return KeyEventService instance
     */
    public static synchronized KeyEventService getInstance() {
        if (instance == null) {
            instance = new KeyEventService();
        }
        return instance;
    }

    /**
     * Record a key press event
     *
     * @param keyCode The key that was pressed
     */
    public void recordKeyPress(KeyCode keyCode) {
        long timestamp = System.currentTimeMillis();
        keyPressTimestamps.put(keyCode, timestamp);
        keyPressCount.merge(keyCode, 1, Integer::sum);

        logger.trace("Key pressed: {} (total presses: {})", keyCode, keyPressCount.get(keyCode));
    }

    /**
     * Record a key release event
     *
     * @param keyCode The key that was released
     */
    public void recordKeyRelease(KeyCode keyCode) {
        Long pressTime = keyPressTimestamps.remove(keyCode);
        if (pressTime != null) {
            long duration = System.currentTimeMillis() - pressTime;
            totalKeyPressTime.merge(keyCode, duration, Long::sum);

            logger.trace("Key released: {} (held for {}ms)", keyCode, duration);
        } else {
            logger.trace("Key released without press record: {}", keyCode);
        }
    }

    /**
     * Check if a key is currently pressed
     *
     * @param keyCode The key to check
     * @return true if the key is currently pressed
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return keyPressTimestamps.containsKey(keyCode);
    }

    /**
     * Get the number of times a key has been pressed
     *
     * @param keyCode The key to check
     * @return Number of times the key has been pressed
     */
    public int getKeyPressCount(KeyCode keyCode) {
        return keyPressCount.getOrDefault(keyCode, 0);
    }

    /**
     * Get the total time a key has been held down (in milliseconds)
     *
     * @param keyCode The key to check
     * @return Total time in milliseconds
     */
    public long getTotalKeyPressTime(KeyCode keyCode) {
        return totalKeyPressTime.getOrDefault(keyCode, 0L);
    }

    /**
     * Get the average time a key has been held down
     *
     * @param keyCode The key to check
     * @return Average time in milliseconds
     */
    public long getAverageKeyPressTime(KeyCode keyCode) {
        int count = getKeyPressCount(keyCode);
        if (count == 0) return 0;

        long totalTime = getTotalKeyPressTime(keyCode);
        return totalTime / count;
    }

    /**
     * Get statistics for all keys
     *
     * @return Map of KeyCode to KeyStatistics
     */
    public Map<KeyCode, KeyStatistics> getAllKeyStatistics() {
        Map<KeyCode, KeyStatistics> stats = new HashMap<>();

        for (KeyCode keyCode : keyPressCount.keySet()) {
            int count = keyPressCount.getOrDefault(keyCode, 0);
            long totalTime = totalKeyPressTime.getOrDefault(keyCode, 0L);
            long avgTime = count > 0 ? totalTime / count : 0;

            stats.put(keyCode, new KeyStatistics(count, totalTime, avgTime));
        }

        return stats;
    }

    /**
     * Reset all statistics
     */
    public void resetStatistics() {
        keyPressTimestamps.clear();
        keyPressCount.clear();
        totalKeyPressTime.clear();
        logger.info("Key statistics reset");
    }

    /**
     * Get the most pressed key
     *
     * @return The KeyCode that has been pressed most, or null if none
     */
    public KeyCode getMostPressedKey() {
        return keyPressCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get the least pressed key (that has been pressed at least once)
     *
     * @return The KeyCode that has been pressed least, or null if none
     */
    public KeyCode getLeastPressedKey() {
        return keyPressCount.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get total number of key presses across all keys
     *
     * @return Total key press count
     */
    public int getTotalKeyPresses() {
        return keyPressCount.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    /**
     * Get number of currently pressed keys
     *
     * @return Number of keys currently held down
     */
    public int getCurrentlyPressedKeysCount() {
        return keyPressTimestamps.size();
    }

    /**
     * Get all currently pressed keys
     *
     * @return Map of currently pressed keys with their press timestamps
     */
    public Map<KeyCode, Long> getCurrentlyPressedKeys() {
        return new HashMap<>(keyPressTimestamps);
    }

    /**
     * Statistics for a single key
     */
    public static class KeyStatistics {
        private final int pressCount;
        private final long totalPressTime;
        private final long averagePressTime;

        /**
         * Constructor for key statistics
         *
         * @param pressCount Number of times pressed
         * @param totalPressTime Total time held down in milliseconds
         * @param averagePressTime Average time held down in milliseconds
         */
        public KeyStatistics(int pressCount, long totalPressTime, long averagePressTime) {
            this.pressCount = pressCount;
            this.totalPressTime = totalPressTime;
            this.averagePressTime = averagePressTime;
        }

        /**
         * Get number of times the key was pressed
         *
         * @return Press count
         */
        public int getPressCount() {
            return pressCount;
        }

        /**
         * Get total time the key has been held down
         *
         * @return Total time in milliseconds
         */
        public long getTotalPressTime() {
            return totalPressTime;
        }

        /**
         * Get average time the key has been held down
         *
         * @return Average time in milliseconds
         */
        public long getAveragePressTime() {
            return averagePressTime;
        }

        @Override
        public String toString() {
            return String.format("KeyStatistics{count=%d, total=%dms, avg=%dms}",
                    pressCount, totalPressTime, averagePressTime);
        }
    }

    /**
     * Export statistics as a formatted string
     *
     * @return Formatted statistics report
     */
    public String exportStatisticsReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Keyboard Statistics Report ===\n");
        report.append(String.format("Total Key Presses: %d\n", getTotalKeyPresses()));
        report.append(String.format("Currently Pressed: %d keys\n", getCurrentlyPressedKeysCount()));
        report.append(String.format("Most Pressed Key: %s (%d times)\n",
                getMostPressedKey(),
                getMostPressedKey() != null ? getKeyPressCount(getMostPressedKey()) : 0));
        report.append("\n=== Individual Key Statistics ===\n");

        getAllKeyStatistics().forEach((keyCode, stats) -> {
            report.append(String.format("%s: %s\n", keyCode, stats));
        });

        return report.toString();
    }
}
