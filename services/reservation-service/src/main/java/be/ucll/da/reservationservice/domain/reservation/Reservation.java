package be.ucll.da.reservationservice.domain.reservation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int userId;
    private Boolean isValidUserId;
    private String userEmail;
    private LocalDate preferredDay;
    private int carId;
    private String email;
    private BigDecimal totalCost;
    private int totalDays;


    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    protected Reservation() {}
    public Reservation(int userId, LocalDate preferredDay, int carId, int totalDays) {
        this.userId = userId;
        this.preferredDay = preferredDay;
        this.carId = carId;
        this.totalDays= totalDays;
        this.status = ReservationStatus.REGISTERED;
    }
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Boolean getValidUserId() {
        return isValidUserId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public Integer getCarId() {
        return carId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getPreferredDay() {
        return preferredDay;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void validatingUser() {
        this.status = ReservationStatus.VALIDATING_USER;
    }

    public void userValid(String userEmail) {
        this.status = ReservationStatus.VALIDATING_CAR;
        this.isValidUserId = true;
        this.userEmail = userEmail;
    }
    public void userInvalid() {
        this.status = ReservationStatus.NO_USER;
        this.isValidUserId = false;
    }

    public void carAvailable(Integer id, String email, BigDecimal price) {
        this.carId = id;
        this.email = email;
        this.totalCost = price.multiply(BigDecimal.valueOf(this.totalDays));
        this.status = ReservationStatus.REQUEST_REGISTERED;
    }

    public void noCarAvailable() {
        this.status = ReservationStatus.NO_CAR;
    }

    public void doubleBooking() {
        this.status = ReservationStatus.DOUBLE_BOOKING;
    }
    public void accept() {
        this.status = ReservationStatus.ACCEPTED;
       // calculateTotalCost();
    }

    private void calculateTotalCost() {
        this.totalCost = BigDecimal.valueOf(calculateBasedOnCarTypeAndDuration());
    }

    private double calculateBasedOnCarTypeAndDuration() {
        return totalDays * 100;
    }

    public void decline() {
        this.status = ReservationStatus.DECLINED;
    }

}
