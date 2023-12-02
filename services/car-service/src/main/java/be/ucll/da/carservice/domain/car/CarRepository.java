package be.ucll.da.carservice.domain.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {


    // Method to remove a car by carId
    void deleteById(Integer carId);

    List<Car> getAllByCarType(String carType);

    List<Car> getCarByOwnerId(int ownerId);

    List<Car> findByLocationAndCarTypeAndPriceLessThan(String location, String carType, BigDecimal maxPrice);
}
