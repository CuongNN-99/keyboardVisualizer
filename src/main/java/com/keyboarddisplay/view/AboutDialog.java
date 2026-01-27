package com.keyboarddisplay.view;

import com.keyboarddisplay.util.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * About dialog showing application information
 *
 * File Location: src/main/java/com/keyboarddisplay/view/AboutDialog.java
 */
public class AboutDialog extends Stage {

    /**
     * Constructor for AboutDialog
     */
    public AboutDialog() {
        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("About " + Constants.APP_NAME);
        setResizable(false);

        VBox root = createContent();
        Scene scene = new Scene(root, 400, 350);

        // Try to load stylesheet, but don't fail if not found
        try {
            String cssPath = getClass().getResource("/css/style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            // Style not found, use default
        }

        setScene(scene);
    }

    /**
     * Create the content for the about dialog
     *
     * @return VBox containing all UI elements
     */
    private VBox createContent() {
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        vbox.setStyle("-fx-background-color: #2b2b2b;");

        // App Name
        Label nameLabel = new Label(Constants.APP_NAME);
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        nameLabel.setStyle("-fx-text-fill: white;");

        // Version
        Label versionLabel = new Label("Version " + Constants.APP_VERSION);
        versionLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");

        // Description
        Label descLabel = new Label("Real-time keyboard display for streaming and tutorials");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(350);
        descLabel.setAlignment(Pos.CENTER);
        descLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");

        // Author
        Label authorLabel = new Label("Developed by " + Constants.APP_AUTHOR);
        authorLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");

        // Features
        Label featuresLabel = new Label(
                "Features:\n" +
                        "• Global keyboard hook\n" +
                        "• Multiple layout options\n" +
                        "• Customizable colors and opacity\n" +
                        "• Smooth animations\n" +
                        "• Always on top"
        );
        featuresLabel.setStyle("-fx-text-fill: #ccc; -fx-font-size: 12px;");
        featuresLabel.setAlignment(Pos.CENTER_LEFT);

        // GitHub link (optional)
        Hyperlink githubLink = new Hyperlink("View Documentation");
        githubLink.setStyle("-fx-text-fill: #4CAF50;");
        githubLink.setOnAction(e -> {
            // Open browser to documentation
            try {
                java.awt.Desktop.getDesktop().browse(
                        new java.net.URI("https://github.com/yourusername/keyboard-display")
                );
            } catch (Exception ex) {
                // Ignore if Desktop is not supported
            }
        });

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 30 10 30; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> close());

        // Add hover effect
        closeButton.setOnMouseEntered(e ->
                closeButton.setStyle(
                        "-fx-background-color: #66BB6A; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 10 30 10 30; " +
                                "-fx-background-radius: 5; " +
                                "-fx-cursor: hand;"
                )
        );
        closeButton.setOnMouseExited(e ->
                closeButton.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-padding: 10 30 10 30; " +
                                "-fx-background-radius: 5; " +
                                "-fx-cursor: hand;"
                )
        );

        vbox.getChildren().addAll(
                nameLabel,
                versionLabel,
                descLabel,
                authorLabel,
                featuresLabel,
                githubLink,
                closeButton
        );

        return vbox;
    }

    /**
     * Show the about dialog
     */
    public void showAbout() {
        showAndWait();
    }
}