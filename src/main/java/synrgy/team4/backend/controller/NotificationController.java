package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import synrgy.team4.backend.model.dto.request.NotificationRequest;
import synrgy.team4.backend.model.dto.response.NotificationResponse;
import synrgy.team4.backend.model.entity.Notification;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.security.jwt.CustomUserDetails;
import synrgy.team4.backend.service.FCMService;
import synrgy.team4.backend.service.NotificationService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class NotificationController {
    @Autowired
    private FCMService fcmService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notification")
    public ResponseEntity sendNotification(@RequestBody NotificationRequest request) throws ExecutionException, InterruptedException {
        fcmService.sendMessageToToken(request);
        return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }


    @GetMapping("/notification")
    public ResponseEntity<List<Notification>> getNotifications(Authentication authentication) {
        // Mendapatkan user yang sedang login
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Mengambil notifikasi berdasarkan userId
        List<Notification> notifications = notificationService.getNotificationsByUserId(userDetails.getId());

        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

}