package be.ucll.da.carservice.web;

import be.ucll.da.carservice.api.CarApiDelegate;
import be.ucll.da.carservice.domain.car.Car;
import be.ucll.da.carservice.domain.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import be.ucll.da.carservice.api.model.ApiCar;
import be.ucll.da.carservice.api.model.ApiCars;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CarController implements CarApiDelegate {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Override
    public ResponseEntity<Void> createCar(ApiCar data) {
        carService.createCar(data);
        return ResponseEntity.ok().build();
    }
    @Override
    public ResponseEntity<ApiCars> getCars() {
        ApiCars cars = new ApiCars();
        cars.addAll(
                carService.getAllCars().stream()
                        .map(this::toDto)
                        .toList()
        );

        return ResponseEntity.ok(cars);
    }
    @Override
    public ResponseEntity<Void> removeCar(Integer carId) {
        carService.removeCar(carId); // Convert to Long as per your service method
        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<ApiCars> searchCars(String location, String carType, BigDecimal maxPrice) {
        ApiCars cars = new ApiCars();
        cars.addAll(
                carService.searchCars(location, carType, maxPrice).stream()
                        .map(this::toDto)
                        .toList()
        );
        return ResponseEntity.ok(cars);
    }

    private ApiCar toDto(Car car) {
        return new ApiCar()
                .carId(car.getId())
                .ownerId(car.getOwnerId())
                .carType(car.getCarType())
                .location(car.getLocation())
                .price(car.getPrice());
    }
}