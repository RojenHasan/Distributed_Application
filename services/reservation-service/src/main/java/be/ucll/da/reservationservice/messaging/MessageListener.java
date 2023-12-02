package be.ucll.da.reservationservice.messaging;


import be.ucll.da.reservationservice.client.car.api.model.CarReservedEvent;
import be.ucll.da.reservationservice.client.car.api.model.CarValidatedEvent;
import be.ucll.da.reservationservice.client.user.api.model.UserValidatedEvent;
import be.ucll.da.reservationservice.domain.reservation.ReservationRequestSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final ReservationRequestSaga saga;

    @Autowired
    public MessageListener(ReservationRequestSaga saga) {
        this.saga = saga;
    }

    @RabbitListener(queues = {"q.user-validated.reservation-service"})
    public void onUserValidated(UserValidatedEvent event) {
        LOGGER.info("Receiving event: " + event);
       this.saga.executeSaga(event.getReservationId(), event);
    }

    @RabbitListener(queues = {"q.car-validated.reservation-service"})
    public void onCarValidated(CarValidatedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event.getReservationId(), event);
    }

/*
    @RabbitListener(queues = {"q.car-bookings.reservation-service"})
    public void onCarReserved(CarReservedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event.getReservationId(), event);
    }
*/


}