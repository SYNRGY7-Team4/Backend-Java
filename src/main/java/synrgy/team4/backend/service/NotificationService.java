package synrgy.team4.backend.service;

import synrgy.team4.backend.model.entity.Notification;
import synrgy.team4.backend.model.dto.request.NotificationRequest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface NotificationService {

    List<Notification> getNotificationsByUserId(UUID userId);
}
