package be.ucll.da.apigateway.web;

//import be.ucll.da.apigateway.api.ReservationApi;
import be.ucll.da.apigateway.api.model.ApiUsers;
import be.ucll.da.apigateway.client.car.api.CarApi;
import be.ucll.da.apigateway.client.user.api.UserApi;
import be.ucll.da.apigateway.client.reservation.api.ReservationApi;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import be.ucll.da.apigateway.api.PlatformApiDelegate;
import be.ucll.da.apigateway.client.owner.api.OwnerApi;
import be.ucll.da.apigateway.api.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class PlatformController implements PlatformApiDelegate {
    @Autowired
    private UserApi userApi;

    @Autowired
    private OwnerApi ownerApi;

    @Autowired
    private CarApi carApi;

    @Autowired
    private ReservationApi reservationApi;

    @Autowired
    private EurekaClient discoveryClient;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public ResponseEntity<ApiUsers> getAllUsers() {
        ApiUsers overview = new ApiUsers();

        userApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("user-service", false).getHomePageUrl());
        be.ucll.da.apigateway.client.user.model.ApiUsers users = circuitBreakerFactory.create("userApi").run(() -> userApi.getAllUsers());

        if (users.getUsers() == null || users.getUsers().isEmpty()) {
            return ResponseEntity.ok(overview);
        }
        for (be.ucll.da.apigateway.client.user.model.ApiUser user : users.getUsers()) {

            overview.addUsersItem(toResponse(user));
        }

        return ResponseEntity.ok(overview);
    }
    private ApiUser toResponse(be.ucll.da.apigateway.client.user.model.ApiUser user) {
        ApiUser response = new ApiUser();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setIsClient(user.getIsClient());

        return response;
    }

    @Override
    public ResponseEntity<ApiUser> createUser(ApiUser apiUser) {

        be.ucll.da.apigateway.client.user.model.ApiUser client = new be.ucll.da.apigateway.client.user.model.ApiUser();
        client.setEmail(apiUser.getEmail());
        client.setFirstName(apiUser.getFirstName());
        client.setLastName(apiUser.getLastName());
        client.setIsClient(apiUser.getIsClient());

        userApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("user-service", false).getHomePageUrl());
        be.ucll.da.apigateway.client.user.model.ApiUser user = circuitBreakerFactory.create("userApi").run(() -> userApi.createUser(client));
        if (user == null) {
            // Handle the case where the user is null (e.g., log an error, return an appropriate response)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(toResponse(user));
    }
    @Override
    public ResponseEntity<ApiOwners> getOwners() {
        ApiOwners overview = new ApiOwners();

        ownerApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("owner-service", false).getHomePageUrl());
        be.ucll.da.apigateway.client.owner.model.ApiOwners owners = circuitBreakerFactory.create("ownerApi").run(() -> ownerApi.getOwners());

        if (owners.getOwners() == null || owners.getOwners().isEmpty()) {
            return ResponseEntity.ok(overview);
        }
        for (be.ucll.da.apigateway.client.owner.model.ApiOwner owner : owners.getOwners()) {

            overview.addOwnersItem(toResponse(owner));
        }

        return ResponseEntity.ok(overview);

    }
    private ApiOwner toResponse(be.ucll.da.apigateway.client.owner.model.ApiOwner owner) {
        ApiOwner response = new ApiOwner();
        response.setOwnerId(owner.getOwnerId());
        response.setEmail(owner.getEmail());
        response.setFirstName(owner.getFirstName());
        response.setLastName(owner.getLastName());
        response.setAddress(owner.getAddress());
        response.setCarType(owner.getCarType());

        return response;
    }

    @Override
    public ResponseEntity<Void> createOwner(ApiOwner apiOwner) {
        try {
            be.ucll.da.apigateway.client.owner.model.ApiOwner client = new be.ucll.da.apigateway.client.owner.model.ApiOwner();
            client.setEmail(apiOwner.getEmail());
            client.setOwnerId(apiOwner.getOwnerId());
            client.setFirstName(apiOwner.getFirstName());
            client.setLastName(apiOwner.getLastName());
            client.setAddress(apiOwner.getAddress());
            client.setCarType(apiOwner.getCarType());

            ownerApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("owner-service", false).getHomePageUrl());

            circuitBreakerFactory.create("ownerApi").run(() -> {
                ownerApi.createOwner(client);
                return null; // This is required since the lambda expression needs to return a value
            });

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> createCar(ApiCar apiCar) {
        try {
        be.ucll.da.apigateway.client.car.model.ApiCar client = new be.ucll.da.apigateway.client.car.model.ApiCar();
        client.setLocation(apiCar.getLocation());
        client.setCarId(apiCar.getCarId());
        client.setCarType(apiCar.getCarType());
        client.setPrice(apiCar.getPrice());
        client.setOwnerId(apiCar.getOwnerId());
        client.setIsAvailable(apiCar.getIsAvailable());

        carApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());
            circuitBreakerFactory.create("carApi").run(() -> {
                carApi.createCar(client);
                return null; // This is required since the lambda expression needs to return a value
            });
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Override
    public ResponseEntity<ApiCars> getCars() {
        ApiCars overview = new ApiCars();

        carApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());
        be.ucll.da.apigateway.client.car.model.ApiCars cars = circuitBreakerFactory.create("carrApi").run(() -> carApi.getCars());

        if (cars.getCars() == null || cars.getCars().isEmpty()) {
            return ResponseEntity.ok(overview);
        }
        for (be.ucll.da.apigateway.client.car.model.ApiCar car : cars.getCars()) {
            overview.addCarsItem(toResponse(car));
        }
        return ResponseEntity.ok(overview);
    }
    private ApiCar toResponse(be.ucll.da.apigateway.client.car.model.ApiCar car) {
        ApiCar response = new ApiCar();
        response.setCarId(car.getCarId());
        response.setLocation(car.getLocation());
        response.setPrice(car.getPrice());
        response.setOwnerId(car.getOwnerId());
        response.setIsAvailable(car.getIsAvailable());
        response.setCarType(car.getCarType());
        return response;
    }

    @Override
    public ResponseEntity<Void> removeCar(Integer carId) {
        carApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());
        circuitBreakerFactory.create("carApi").run(() -> {
            carApi.removeCar(carId);
            return null;
        });
        return ResponseEntity.ok().build();

    }

    @Override
    public ResponseEntity<ApiCars> searchCars(String location, String carType, BigDecimal maxPrice) {
        ApiCars overview = new ApiCars();

        carApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());
        be.ucll.da.apigateway.client.car.model.ApiCars cars = circuitBreakerFactory.create("carrApi").run(() -> carApi.searchCars(location, carType, maxPrice));

        if (cars.getCars() == null || cars.getCars().isEmpty()) {
            return ResponseEntity.ok(overview);
        }
        for (be.ucll.da.apigateway.client.car.model.ApiCar car : cars.getCars()) {
            overview.addCarsItem(toResponse(car));
        }
        return PlatformApiDelegate.super.searchCars(location, carType, maxPrice);
    }

    @Override
    public ResponseEntity<ApiReservationRequestResponse> apiV1ReservationRequestPost(ApiReservationRequest apiReservationRequest) {
        be.ucll.da.apigateway.client.reservation.model.ApiReservationRequest reservationRequest = new be.ucll.da.apigateway.client.reservation.model.ApiReservationRequest();
        reservationRequest.setCarId(apiReservationRequest.getCarId());
        reservationRequest.setUserId(apiReservationRequest.getUserId());
        reservationRequest.setPreferredDay(apiReservationRequest.getPreferredDay());
        reservationRequest.setTotalDays(apiReservationRequest.getTotalDays());

        reservationApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("reservation-service", false).getHomePageUrl());
        be.ucll.da.apigateway.client.reservation.model.ApiReservationRequestResponse client = circuitBreakerFactory.create("reservationApi").run(() -> reservationApi.apiV1ReservationRequestPost(reservationRequest));

        ApiReservationRequestResponse response = new ApiReservationRequestResponse();
        response.setReservationRequestNumber(client.getReservationRequestNumber());
        return ResponseEntity.ok(response);
    }

/*
    @Override
    public ResponseEntity<Void> apiV1ReservationConfirmationPost(ApiReservationConfirmation apiReservationConfirmation) {
        be.ucll.da.apigateway.client.reservation.model.ApiReservationConfirmation reservationConfirmation = new be.ucll.da.apigateway.client.reservation.model.ApiReservationConfirmation();
        reservationConfirmation.setReservationRequestNumber(apiReservationConfirmation.getReservationRequestNumber());
        reservationConfirmation.setAcceptProposedReservation(apiReservationConfirmation.getAcceptProposedReservation());


        reservationApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("reservation-service", false).getHomePageUrl());
        be.ucll.da.apigateway.client.reservation.model.ApiReservationRequestResponse client = circuitBreakerFactory.create("reservationApi").run(() ->
        {
            reservationApi.apiV1ReservationConfirmationPost(reservationConfirmation);
            return null;
        });

        return ResponseEntity.ok().build();
    }
*/

}