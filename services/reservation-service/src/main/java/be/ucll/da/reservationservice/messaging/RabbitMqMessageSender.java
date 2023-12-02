package be.ucll.da.reservationservice.messaging;


import be.ucll.da.reservationservice.api.model.ReservationFinalizedEvent;
import be.ucll.da.reservationservice.client.car.api.model.ReleaseCarCommand;
import be.ucll.da.reservationservice.client.car.api.model.ReserveCarCommand;
//import be.ucll.da.reservationservice.client.user.api.model.CheckUserOwnerCommand;
import be.ucll.da.reservationservice.client.car.api.model.ValidateCarCommand;
import be.ucll.da.reservationservice.client.notification.api.model.SendEmailCommand;
import be.ucll.da.reservationservice.client.user.api.model.ValidateUserCommand;
import be.ucll.da.reservationservice.domain.reservation.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RabbitMqMessageSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMqMessageSender.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendValidateUserCommand(Integer reservationId, Integer userId) {
        var command = new ValidateUserCommand();
        command.userId(userId);
        command.reservationId(reservationId);
        sendToQueue("q.user-service.validate-user", command);
    }
    public void sendValidateCarCommand(Integer reservationId, Integer carId) {
        var command = new ValidateCarCommand();
        command.carId(carId);
        command.reservationId(reservationId);
        sendToQueue("q.car-service.validate-car", command);
    }

   /* public void sendBookCarCommand(Integer reservationId, LocalDate preferredDay) {
        var command = new ReserveCarCommand();
        command.reservationId(reservationId);
        command.day(preferredDay);
        sendToQueue("q.car-service.book-car", command);
    }
    public void sendReleaseCarCommand(Integer reservationId, Integer carId, LocalDate preferredDay) {
        var command = new ReleaseCarCommand();
        command.reservationId(reservationId);
        command.carId(carId);
        command.day(preferredDay);
        sendToQueue("q.car-service.release-car", command);
    }*/

    public void sendEmail(String recipient, String message) {
        var command = new SendEmailCommand();
        command.recipient(recipient);
        command.message(message);
        sendToQueue("q.notification-service.send-email", command);
    }

    private void sendToQueue(String queue, Object message) {
        LOGGER.info("Sending message: " + message);

        this.rabbitTemplate.convertAndSend(queue, message);
    }
    public void sendReservationFinalizedEvent(Reservation reservation, boolean isAccepted) {
        var event = new ReservationFinalizedEvent();
        event.reservationRequestNumber(reservation.getId().toString());
        event.setDay(reservation.getPreferredDay());
        event.userId(reservation.getUserId());
        //event.car(reservation.getNeededCarType());
        event.carId(reservation.getCarId());
        event.accepted(isAccepted);

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.reservation-finalized", "", event);
    }
}
