package synrgy.team4.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import synrgy.team4.backend.model.entity.Notification;
import synrgy.team4.backend.security.jwt.CustomUserDetails;
import synrgy.team4.backend.service.NotificationService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable UUID notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read.");
    }

    @PutMapping("/read/all")
    public ResponseEntity<String> markAllAsRead(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationService.markAllNotificationsAsRead(userDetails.getId());
        return ResponseEntity.ok("All notifications marked as read.");
    }

    @GetMapping("/")
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
