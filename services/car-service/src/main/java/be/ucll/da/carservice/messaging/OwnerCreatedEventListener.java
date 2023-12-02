package be.ucll.da.carservice.messaging;
import be.ucll.da.carservice.client.owner.api.model.OwnerCreatedEvent;
import be.ucll.da.carservice.domain.owner.Owner;
import be.ucll.da.carservice.domain.owner.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OwnerCreatedEventListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(OwnerCreatedEventListener.class);

    private final OwnerRepository repository;

    @Autowired
    public OwnerCreatedEventListener(OwnerRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = {"q.owner-car-service"})
    public void onOwnerCreated(OwnerCreatedEvent event) {
        LOGGER.info("Received ownerCreatedEvent..." + event);
        this.repository.save(new Owner(event.getOwner().getOwnerId(), event.getOwner().getEmail()));
    }
}