package be.ucll.da.reservationservice.domain.reservation;

import be.ucll.da.reservationservice.api.model.ApiReservationConfirmation;
import be.ucll.da.reservationservice.api.model.ApiReservationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository repository;
    private final ReservationRequestSaga requestSaga;

    @Autowired
    public ReservationService(ReservationRepository repository, ReservationRequestSaga requestSaga) {
        this.repository = repository;
        this.requestSaga = requestSaga;
    }

    public String registerRequest(ApiReservationRequest request) {
       
        Reservation reservation = new Reservation(
                request.getUserId(),
                request.getPreferredDay(),
                request.getCarId(),
                request.getTotalDays()
        );

        reservation = repository.save(reservation);
        requestSaga.executeSaga(reservation);

        return reservation.getId().toString();
    }

    /* public List<Reservation> getReservationsForUserWithBillingStatus(Integer userId, String billingStatus) {
        return repository.getReservationsForUserWithBillingStatus(userId, billingStatus);
    }
*/
    public void finalizeReservation(ApiReservationConfirmation apiReservationConfirmation) {
        if (apiReservationConfirmation.getAcceptProposedReservation()) {
            requestSaga.accept(Integer.valueOf(apiReservationConfirmation.getReservationRequestNumber()));
        } else {
            requestSaga.decline(Integer.valueOf(apiReservationConfirmation.getReservationRequestNumber()));
        }
    }

    public List<Reservation> getReservationsOnDay(LocalDate day) {
        return repository.getReservationsOnDay(day);
    }
}
