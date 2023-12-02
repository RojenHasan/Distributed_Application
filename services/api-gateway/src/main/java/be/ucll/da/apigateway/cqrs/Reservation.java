package be.ucll.da.apigateway.cqrs;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer userId;
    private Boolean isValidUserId;
    private String userEmail;
    private LocalDate preferredDay;
    private Integer carId;
    private String neededCarType;
    private Integer owner;

    protected Reservation() {}
    public Reservation(Integer id, LocalDate preferredDay, int carId, int owner, int userId) {
        this.userId = userId;
        this.id = id;
        this.owner= owner;
        this.carId = carId;
        this.preferredDay = preferredDay;
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


    public Integer getCarId() {
        return carId;
    }


    public Integer getOwner() {
        return owner;
    }

    public LocalDate getPreferredDay() {
        return preferredDay;
    }

    public String getNeededCarType() {
        return neededCarType;
    }

}
