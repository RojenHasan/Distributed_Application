package be.ucll.da.notificationservice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import be.ucll.da.notificationservice.client.owner.api.model.OwnerCreatedEvent;

@Component
public class OwnerCreatedEventListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(OwnerCreatedEventListener.class);

    @RabbitListener(queues = {"q.doctor-notification-service"})
    public void onDoctorCreated(OwnerCreatedEvent event) {
        LOGGER.info("Trying...");
        throw new RuntimeException("Cannot send notification");

        // LOGGER.info("Sending a notification...");
    }

    @RabbitListener(queues = {"q.fall-back-notification"})
    public void onFailedNotificationSend(OwnerCreatedEvent event) {
        LOGGER.info("Executing fallback code...");

        // LOGGER.info("Sending a notification...");
    }
}
