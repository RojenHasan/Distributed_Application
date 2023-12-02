package be.ucll.da.reservationservice.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

@Query( "select a " +
        "from Reservation a " +
        "where " +
        "  a.userId = :userId AND " +
        "  a.preferredDay = :day AND " +
        "  (a.status = be.ucll.da.reservationservice.domain.reservation.ReservationStatus.REQUEST_REGISTERED OR " +
        "   a.status = be.ucll.da.reservationservice.domain.reservation.ReservationStatus.ACCEPTED)")
List<Reservation> getReservationsForUserOnDay(Integer userId, LocalDate day);
        @Query( "select a " +
                "from Reservation a " +
                "where " +
                "  a.preferredDay = :day AND " +
                "  a.status = be.ucll.da.reservationservice.domain.reservation.ReservationStatus.ACCEPTED")
        List<Reservation> getReservationsOnDay(LocalDate day);

}