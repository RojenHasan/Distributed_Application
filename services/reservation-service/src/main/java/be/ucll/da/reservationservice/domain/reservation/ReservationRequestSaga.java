package be.ucll.da.reservationservice.domain.reservation;


import be.ucll.da.reservationservice.client.car.api.model.CarReservedEvent;
import be.ucll.da.reservationservice.client.car.api.model.CarValidatedEvent;
import be.ucll.da.reservationservice.client.user.api.model.UserValidatedEvent;
import be.ucll.da.reservationservice.messaging.RabbitMqMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationRequestSaga {

    private final RabbitMqMessageSender eventSender;
    private final ReservationRepository repository;

    @Autowired
    public ReservationRequestSaga(RabbitMqMessageSender eventSender, ReservationRepository repository) {
        this.eventSender = eventSender;
        this.repository = repository;
    }

    public void executeSaga(Reservation reservation) {
        reservation.validatingUser();
        eventSender.sendValidateUserCommand(reservation.getId(), reservation.getUserId());
    }

   public void executeSaga(Integer id, UserValidatedEvent event) {
        Reservation reservation = getReservationById(id);
        if (event.getIsClient()) {
            reservation.userValid(event.getEmail());
            eventSender.sendValidateCarCommand(reservation.getId(), reservation.getCarId());
        } else {
            reservation.userInvalid();
            eventSender.sendEmail(event.getEmail(), generateMessage(reservation.getId(), "You cannot book a car, you are not a user at this platform."));
        }
    }
    public void executeSaga(Integer id, CarValidatedEvent event) {
        Reservation reservation = getReservationById(id);
        if (event.getIsAvailable()) {
            reservation.carAvailable(event.getCarId(), event.getOwnerEmail(), event.getPrice());
            eventSender.sendEmail(reservation.getEmail(), generateMessage(reservation.getId(), "Proposal for reservation registered. Please accept or decline."));
            eventSender.sendEmail(reservation.getUserEmail(), generateMessage(reservation.getId(), "Your factuur is: " + event.getPrice()));

        } else {
            reservation.noCarAvailable();
            eventSender.sendEmail(reservation.getUserEmail(), generateMessage(reservation.getId(), "You cannot reserve a car, we have no cars available on your preferred day."));
        }
    }

    public void accept(Integer id) {
        Reservation reservation = getReservationById(id);

        if (reservation.getStatus() == ReservationStatus.REQUEST_REGISTERED) {
            reservation.accept();
           // eventSender.sendReservationFinalizedEvent(reservation, true);

        } else {
            throw new RuntimeException("reservation Request is not in a valid status to be accepted");
        }
    }

    public void decline(Integer id) {
        Reservation reservation = getReservationById(id);

        if (reservation.getStatus() == ReservationStatus.REQUEST_REGISTERED) {
            reservation.decline();
           // eventSender.sendReleaseCarCommand(reservation.getId(), reservation.getCarId(), reservation.getPreferredDay());
            eventSender.sendReservationFinalizedEvent(reservation, false);

        } else {
            throw new RuntimeException("reservation Request is not in a valid status to be declined");
        }
    }

    private Reservation getReservationById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    private String generateMessage(Integer id, String message) {
        return "Regarding reservation " + id + ". " + message;
    }
}
