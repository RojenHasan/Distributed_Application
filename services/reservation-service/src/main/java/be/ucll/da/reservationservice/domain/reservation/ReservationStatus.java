package be.ucll.da.reservationservice.domain.reservation;

public enum ReservationStatus {

    // Happy Flow
    REGISTERED,
    VALIDATING_USER,
    VALIDATING_CAR,
    REQUEST_REGISTERED,
    RESERVE_ROOM,

    // Failure States
    NO_USER,
    NO_OWNER,
    NO_CAR,
    DOUBLE_BOOKING,

    // End States
    ACCEPTED,
    DECLINED;
}
