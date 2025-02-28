package controllers.joueur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrackPlayersController {

    @FXML private Button startTrackingButton;
    @FXML private Button selectVideoButton;
    @FXML private Button backButton;
    @FXML private Button homeButton;
    @FXML private ProgressIndicator trackingProgress;

    private String inputVideoPath = "src/main/football_analysis-main/input_videos/08fd33_4.mp4"; // Default input video

    @FXML
    private void handleStartTracking() {
        try {
            String projectRoot = System.getProperty("user.dir");
            String pythonPath = "python"; // Use system PATH Python; update to absolute if needed
            String scriptPath = new File(projectRoot, "/CRUD/src/main/football_analysis-main/main.py").getAbsolutePath();
            String workingDir = new File(projectRoot, "/CRUD/src/main/football_analysis-main").getAbsolutePath();

            // Verify script exists
            File scriptFile = new File(scriptPath);
            if (!scriptFile.exists()) {
                showAlert("Error", "Script Not Found", "Python script not found at: " + scriptPath);
                return;
            }

            // Show progress indicator
            trackingProgress.setVisible(true);

            // Execute the Python script with the input video path
            ProcessBuilder pb = new ProcessBuilder(pythonPath, scriptPath, new File(projectRoot, inputVideoPath).getAbsolutePath());
            pb.directory(new File(workingDir));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Capture output for debugging
            java.util.Scanner scanner = new java.util.Scanner(process.getInputStream()).useDelimiter("\\A");
            String output = scanner.hasNext() ? scanner.next() : "";
            scanner.close();
            System.out.println("Python script output: " + output);

            // Wait for the script to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // Hide progress indicator after tracking
                trackingProgress.setVisible(false);
                openVideoWithExternalPlayer();
            } else {
                trackingProgress.setVisible(false);
                showAlert("Error", "Tracking Failed", "Python script exited with code: " + exitCode + "\nOutput: " + output);
            }
        } catch (IOException e) {
            trackingProgress.setVisible(false);
            showAlert("Error", "Failed to Run Tracking", "IO Exception: " + e.getMessage());
        } catch (InterruptedException e) {
            trackingProgress.setVisible(false);
            showAlert("Error", "Tracking Interrupted", "Interrupted Exception: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @FXML
    private void handleSelectVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Input Video");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi"));
        Stage stage = (Stage) selectVideoButton.getScene().getWindow(); // Use the current stage instead of a new one
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String projectRoot = System.getProperty("user.dir");
            Path inputDirPath = Paths.get(projectRoot, "src", "main", "football_analysis-main", "input_videos");
            Path destinationPath = inputDirPath.resolve(selectedFile.getName());
            inputVideoPath = Paths.get("src", "main", "football_analysis-main", "input_videos", selectedFile.getName()).toString();

            // Ensure the input_videos directory exists
            try {
                Files.createDirectories(inputDirPath); // Creates the directory if it doesn’t exist
            } catch (IOException e) {
                showAlert("Error", "Failed to Create Directory", "Could not create directory: " + inputDirPath + "\nException: " + e.getMessage());
                return;
            }

            // Copy the file
            try {
                System.out.println("Copying from: " + selectedFile.getAbsolutePath());
                System.out.println("Copying to: " + destinationPath.toString());
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                showAlert("Success", "Video Selected", "Input video set to: " + selectedFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to Select Video", "Exception: " + e.getMessage() + "\nCheck console for details.");
            }
        }
    }


    @FXML
    private void handleBack() {
        loadScene("/joueur/DisplayJoueur.fxml", backButton);
    }

    @FXML
    private void handleHome() {
        loadScene("/Home.fxml", homeButton);
    }

    @FXML
    private void openVideoWithExternalPlayer() {
        try {
            String projectRoot = System.getProperty("user.dir");
            File videoFile = new File(projectRoot, "/CRUD/src/main/football_analysis-main/output_videos/output_video.mp4");
            System.out.println("Video file path: " + videoFile.getAbsolutePath());
            System.out.println("File exists: " + videoFile.exists());
            System.out.println("File size: " + videoFile.length() + " bytes");
            System.out.println("Can read: " + videoFile.canRead());
            System.out.println("Desktop supported: " + Desktop.isDesktopSupported());
            System.out.println("Open action supported: " + (Desktop.isDesktopSupported() ? Desktop.getDesktop().isSupported(Desktop.Action.OPEN) : "N/A"));

            if (!videoFile.exists()) {
                showAlert("Error", "Video Not Found", "Output video not found at: " + videoFile.getAbsolutePath());
                return;
            }
            if (videoFile.length() == 0) {
                showAlert("Error", "Video Empty", "Output video is empty at: " + videoFile.getAbsolutePath());
                return;
            }
            if (!videoFile.canRead()) {
                showAlert("Error", "Permission Denied", "Cannot read video file at: " + videoFile.getAbsolutePath());
                return;
            }

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                System.out.println("Attempting to open with system default player...");
                Desktop.getDesktop().open(videoFile);
            } else {
                System.out.println("Falling back to VLC...");
                ProcessBuilder pb = new ProcessBuilder("vlc", videoFile.getAbsolutePath());
                pb.start();
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to Open Video", "Exception: " + e.getMessage());
            System.out.println("IOException details: " + e.getMessage());
        }
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to Load Scene", e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if ("Success".equals(title)) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}