package be.ucll.da.carservice.domain.car;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Car {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer ownerId;
    private String location;
    private BigDecimal price;
    private String carType;
    private boolean isAvailable;
    // is
    protected Car() {}


    public Car(int ownerId,String location, BigDecimal price,
               String carType) {
        this.ownerId = ownerId;
        this.location = location;
        this.price = price;
        this.carType = carType;
        this.isAvailable =true;
    }


    public Integer getId() {
        return id;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public String getLocation() {
        return location;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCarType() {
        return carType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}



