package synrgy.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import synrgy.team4.backend.model.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserId(UUID userId);
}