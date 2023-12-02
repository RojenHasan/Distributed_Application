package be.ucll.da.reservationservice.web;


import be.ucll.da.reservationservice.api.ReservationApiDelegate;
import be.ucll.da.reservationservice.api.model.*;
import be.ucll.da.reservationservice.domain.reservation.Reservation;
import be.ucll.da.reservationservice.domain.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ReservationController implements ReservationApiDelegate {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public ResponseEntity<ApiReservationRequestResponse> apiV1ReservationRequestPost(ApiReservationRequest apiReservationRequest) {
        ApiReservationRequestResponse response = new ApiReservationRequestResponse();
        response.reservationRequestNumber(reservationService.registerRequest(apiReservationRequest));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> apiV1ReservationConfirmationPost(ApiReservationConfirmation apiReservationConfirmation) {
        reservationService.finalizeReservation(apiReservationConfirmation);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApiReservationOverview> apiV1ReservationDayGet(String dayString) {
        LocalDate day = LocalDate.parse(dayString, DateTimeFormatter.ISO_DATE);

        ApiReservationOverview overview = new ApiReservationOverview();
        overview.setDay(day);

        for (Reservation reservation : reservationService.getReservationsOnDay(day)) {
            ApiReservation apiReservation = new ApiReservation();
            apiReservation.setUserId(reservation.getUserId());
            apiReservation.setEmail(reservation.getEmail());
            apiReservation.setCarId(reservation.getCarId());

            overview.addReservationsItem(apiReservation);
        }

        return ResponseEntity.ok(overview);
    }
}
