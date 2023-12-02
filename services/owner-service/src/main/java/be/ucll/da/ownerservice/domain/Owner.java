package be.ucll.da.ownerservice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Owner{
    @Id
    @GeneratedValue
    private int id;
    private String carType;
    private String firstName;
    private String lastName;
    private String email;
    private String address;

    // Default constructor required by JPA
    protected Owner() {
    }

    public Owner(String carType, String firstName, String lastName, String email, String address) {
        //this.id = id;
        this.carType = carType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
