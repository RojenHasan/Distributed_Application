package be.ucll.da.carservice.domain.car;

import be.ucll.da.carservice.domain.owner.Owner;
import be.ucll.da.carservice.domain.owner.OwnerRepository;
import be.ucll.da.carservice.messaging.OwnerCreatedEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import be.ucll.da.carservice.api.model.ApiCar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class CarService {

    private final CarRepository carRepository;
    private final OwnerRepository ownerRepository;
    private final List<Car> cars = new ArrayList<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(CarService.class);


    @Autowired
    public CarService(CarRepository carRepository, OwnerRepository ownerRepository) {
        this.carRepository = carRepository;
        this.ownerRepository = ownerRepository;
    }
    public Car validateCar(Integer id) {
        return  carRepository.findById(id).orElse(null);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public void createCar(ApiCar data) {
        Owner ownerOptional = ownerRepository.findById(data.getOwnerId()).orElse(null);


        if (ownerOptional == null) {
            throw new CarException("Owner does not exist");
        }
        Car car = new Car(
                data.getOwnerId(),
                data.getLocation(),
                data.getPrice(),
                data.getCarType());
        carRepository.save(car);
    }
    public void removeCar(int carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            carRepository.delete(car);
        } else {
            throw new CarException("Car not found");
        }
    }

    public List<Car> searchCars(String location, String carType, BigDecimal maxPrice) {

        return carRepository.findByLocationAndCarTypeAndPriceLessThan(location, carType, maxPrice);
    }
    // Returns carID is reservation is successful, -1 otherwise
    public Integer reserveCar(LocalDate day) {
        var carReservations = getCarReservations(day);

        for (var carReservation : carReservations.entrySet()) {
            if (!carReservation.getValue()) { // isReserved -> false, so car is available
                carReservations.put(carReservation.getKey(), true);
                return carReservation.getKey();
            }
        }

        return -1;
    }

    public Integer releaseCar(LocalDate day, Integer carId) {
        getCarReservations(day).put(carId, false);
        return carId;
    }

    // ---- Internal Code to Simulate Cars in a Clinic ----

    // There are 3 car in the clinic, every car is booked or unbooked for a full day

    // Day -> CarID -> IsReserved
    private Map<LocalDate, Map<Integer, Boolean>> platformReservations;

    private Map<Integer, Boolean> getCarReservations(LocalDate day) {
        if (platformReservations == null) {
            platformReservations = new HashMap<>();
        }

        platformReservations.computeIfAbsent(day, k -> fillInUnreservedCars());
        return platformReservations.get(day);
    }

    private Map<Integer, Boolean> fillInUnreservedCars() {
        var newCarMap = new HashMap<Integer, Boolean>();
        newCarMap.put(1, false);
        newCarMap.put(2, false);
        newCarMap.put(3, false);

        return newCarMap;
    }
}