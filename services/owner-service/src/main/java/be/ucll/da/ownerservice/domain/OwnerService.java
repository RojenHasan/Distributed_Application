package be.ucll.da.ownerservice.domain;

import be.ucll.da.ownerservice.api.model.ApiOwner;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OwnerService {
    private final OwnerRepository repository;
    private final EventSender eventSender;

    private final Faker faker = new Faker();

    private final List<Owner> owners = new ArrayList<>();
    @Autowired
    public OwnerService(OwnerRepository repository, EventSender eventSender) {
        this.repository = repository;
        this.eventSender = eventSender;
    }

    public Owner validateOwner(Integer id) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        String email = firstName + "." + lastName + "@google.com";
        String typeCar = "";

        if (Math.random() > 0.3) {
            return new Owner(typeCar, firstName, lastName, email, "123");
        }

        return new Owner(typeCar, firstName, lastName, email,"123");
    }

    public Owner getOwner(Integer id) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        String email = firstName + "." + lastName + "@google.com";
        String typeCar= "";

        return new Owner(typeCar, firstName, lastName, email,"adres");
    }

    public Optional<Owner> getOwner1(Integer id) {
        return getAllOwners().stream()
                .filter(owner -> owner.getId() == id)
                .findFirst();

    }

    public List<Owner> getOwners(String carType) {
        if (carType == null || carType.isEmpty()) {
            throw new OwnerException("FieldOfCarType is empty");
        }

        return repository.findAllByCarType(carType);
    }

    public List<Owner> getAllOwners() {
        return new ArrayList<>(owners);
    }
    public void addOwner(ApiOwner request) {
        Owner newOwner = new Owner(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getAddress(),
                request.getCarType()
        );
        owners.add(newOwner);
        repository.save(newOwner);
        eventSender.sendOwnerCreatedEvent(newOwner);
        //return newOwner;
    }

}