package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileNotificationService implements NotificationServiceContract {

    private final String filePath;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileNotificationService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void dispatch(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String formatted = String.format("[%s] %s%n", timestamp, message);

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(formatted);
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }

}
