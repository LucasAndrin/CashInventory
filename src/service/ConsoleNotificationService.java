package service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleNotificationService implements NotificationServiceContract {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void dispatch(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.printf("[%s] %s%n", timestamp, message);
    }

}
