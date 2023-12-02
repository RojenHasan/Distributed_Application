package be.ucll.da.apigateway.cqrs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

        List<Reservation> getReservationByPreferredDay(LocalDate day);
}