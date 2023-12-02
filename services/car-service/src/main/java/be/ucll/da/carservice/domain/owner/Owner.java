package be.ucll.da.carservice.domain.owner;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Owner {
    @Id
    @GeneratedValue
    private int id;
    private String email;

    // Default constructor required by JPA
    protected Owner() {
    }

    public Owner(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
