package be.ucll.da.ownerservice.messaging;


import be.ucll.da.ownerservice.api.model.CheckOwnerIsClientCommand;
import be.ucll.da.ownerservice.api.model.OwnerValidatedEvent;
import be.ucll.da.ownerservice.api.model.ValidateOwnerCommand;
import be.ucll.da.ownerservice.domain.Owner;
import be.ucll.da.ownerservice.domain.OwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final OwnerService ownerService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageListener(OwnerService ownerService, RabbitTemplate rabbitTemplate) {
        this.ownerService = ownerService;
        this.rabbitTemplate = rabbitTemplate;
    }
    /*
    @RabbitListener(queues = {"q.owner-service.validate-owner"})
    public void onValidateOwner(ValidateOwnerCommand command) {
        LOGGER.info("Received command: " + command);

        Owner owner = ownerService.validateOwner(command.getOwnerId());
        OwnerValidatedEvent event = new OwnerValidatedEvent();
        event.reservationId(command.getReservationId());
        event.ownerId(owner.getId());
        event.firstName(owner.getFirstName());
        event.lastName(owner.getLastName());
        event.email(owner.getEmail());
        event.address(owner.getAddress());
        event.carType(owner.getCarType());

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.owner-validated", "", event);

        //billingService.generateBill(command.getReservationId());
    }*/
    /*

    @RabbitListener(queues = {"q.owner-service.check-isClient-owners"})
    public void onCheckClientOwners(CheckOwnerIsClientCommand command) {
        LOGGER.info("Received command: " + command);
        List<Owner> clientOwners = ownerService.getAllOwners();
        for (Owner clientOwner : clientOwners) {
            OwnerValidatedEvent event = new OwnerValidatedEvent();
            event.reservationId(command.getReservationId());
            event.ownerId(clientOwner.getId());
            event.firstName(clientOwner.getFirstName());
            event.lastName(clientOwner.getLastName());
            event.email(clientOwner.getEmail());
            event.address(clientOwner.getAddress());
            event.carType(clientOwner.getCarType());

            LOGGER.info("Sending event: " + event);
            this.rabbitTemplate.convertAndSend("x.owners-isClient", "", event);
        }
    }*/
}