package be.ucll.da.carservice.messaging;

import be.ucll.da.carservice.domain.car.Car;
import be.ucll.da.carservice.api.model.ValidateCarCommand;
import be.ucll.da.carservice.api.model.CarValidatedEvent;
import be.ucll.da.carservice.domain.car.CarService;
import be.ucll.da.carservice.domain.owner.Owner;
import be.ucll.da.carservice.domain.owner.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final CarService carService;
    private final RabbitTemplate rabbitTemplate;
    private final OwnerRepository ownerRepository;

    @Autowired
    public MessageListener(CarService carService, RabbitTemplate rabbitTemplate,
                           OwnerRepository ownerRepository) {
        this.carService = carService;
        this.rabbitTemplate = rabbitTemplate;
        this.ownerRepository = ownerRepository;
    }

    @RabbitListener(queues = {"q.car-service.validate-car"})
    public void onValidateCar(ValidateCarCommand command) {
        LOGGER.info("Received command: " + command);

        Car car = carService.validateCar(command.getCarId());
        CarValidatedEvent event = new CarValidatedEvent();
        event.reservationId(command.getReservationId());
        event.carId(command.getCarId());
        if(car != null){
            //event.carId(car.getId());
            Owner owner =  ownerRepository.findById(car.getOwnerId()).orElse(null);
            event.carType(car.getCarType());
            event.location(car.getLocation());
            event.ownerEmail(owner.getEmail());
            event.price((car.getPrice()));
            event.isAvailable(true);
        }else{
            event.setOwnerEmail("N/A");
            event.setLocation("N/A");
            event.setPrice(BigDecimal.valueOf(0));
            event.setCarType("N/A");
            event.isAvailable(false);
        }

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.car-validated", "", event);
    }
    /*

    @RabbitListener(queues = {"q.car-service.book-car"})
    public void onBookCar(ReserveCarCommand command) {
        LOGGER.info("Received command: " + command);

        Integer carId = carService.reserveCar(command.getDay());
        CarReservedEvent event = new CarReservedEvent();
        event.reservationId(command.getReservationId());
        event.day(command.getDay());
        event.carAvailable(carId != -1);
        event.carId(carId);

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.car-bookings", "", event);
    }

    @RabbitListener(queues = {"q.car-service.release-car"})
    public void onReleaseCar(ReleaseCarCommand command) {
        LOGGER.info("Received command: " + command);
        carService.releaseCar(command.getDay(), command.getCarId());

        CarReleasedEvent event = new CarReleasedEvent();
        event.reservationId(command.getReservationId());
        event.day(command.getDay());
        event.carId(command.getCarId());

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.car-releases", "", event);
    }*/
}
