package synrgy.team4.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import synrgy.team4.backend.model.entity.Notification;
import synrgy.team4.backend.model.dto.request.NotificationRequest;
import synrgy.team4.backend.repository.NotificationRepository;
import synrgy.team4.backend.service.FCMService;
import synrgy.team4.backend.service.NotificationService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FCMService fcmService;



    @Override
    public List<Notification> getNotificationsByUserId(UUID userId) {
        return notificationRepository.findByUserId(userId);
    }
}
