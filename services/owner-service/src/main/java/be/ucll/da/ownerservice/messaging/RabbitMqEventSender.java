package be.ucll.da.ownerservice.messaging;

import be.ucll.da.ownerservice.api.model.ApiOwner;
import be.ucll.da.ownerservice.api.model.OwnerCreatedEvent;
import be.ucll.da.ownerservice.domain.EventSender;
import be.ucll.da.ownerservice.domain.Owner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventSender implements EventSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqEventSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendOwnerCreatedEvent(Owner owner) {
        this.rabbitTemplate.convertAndSend("x.owner-created", "", toEvent(owner));
    }

    private OwnerCreatedEvent toEvent(Owner owner) {
        return new OwnerCreatedEvent()
                .owner(new ApiOwner()
                        .ownerId(owner.getId())
                        .carType(owner.getCarType())
                        .firstName(owner.getFirstName())
                        .lastName(owner.getLastName())
                        .email(owner.getEmail())
                        .address(owner.getAddress()));
    }
}
