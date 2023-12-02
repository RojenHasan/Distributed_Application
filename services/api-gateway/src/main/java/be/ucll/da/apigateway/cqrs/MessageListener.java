package be.ucll.da.apigateway.cqrs;

import be.ucll.da.apigateway.client.user.model.UserValidatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public MessageListener(UserRepository userRepository, OwnerRepository ownerRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.reservationRepository = reservationRepository;
    }

    @RabbitListener(queues = {"q.user-validated.api-gateway"})
    public void onUserValidated(UserValidatedEvent event) {
        LOGGER.info("Receiving event: " + event);

        User user = new User(event.getUserId(), event.getFirstName(), event.getLastName(), event.getLastName());
        userRepository.save(user);
    }



}
