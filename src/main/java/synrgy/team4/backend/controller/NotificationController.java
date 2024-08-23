package synrgy.team4.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import synrgy.team4.backend.model.entity.Notification;
import synrgy.team4.backend.security.jwt.CustomUserDetails;
import synrgy.team4.backend.service.NotificationService;

import java.util.List;

@Slf4j
@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @GetMapping("/notification")
    public ResponseEntity<List<Notification>> getNotifications(Authentication authentication) {
        // Mendapatkan user yang sedang login
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            // Mengambil notifikasi berdasarkan userId
            List<Notification> notifications = notificationService.getNotificationsByUserId(userDetails.getId());
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Failed to get notifications", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
