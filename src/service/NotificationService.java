package service;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private final List<NotificationServiceContract> notifiers = new ArrayList<>();

    public void register(NotificationServiceContract notifier) {
        notifiers.add(notifier);
    }

    public void notifyAll(String message) {
        for (NotificationServiceContract notifier : notifiers) {
            notifier.dispatch(message);
        }
    }

}
