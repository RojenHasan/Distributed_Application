package be.ucll.da.userservice.messaging;


import be.ucll.da.userservice.api.model.CheckUserIsClientCommand;
import be.ucll.da.userservice.api.model.UserValidatedEvent;
import be.ucll.da.userservice.domain.User;
import be.ucll.da.userservice.domain.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import be.ucll.da.userservice.api.model.ValidateUserCommand;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageListener(UserService userService, RabbitTemplate rabbitTemplate) {
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = {"q.user-service.validate-user"})
    public void onValidateUser(ValidateUserCommand command) {
        LOGGER.info("Received command: " + command);

        User user = userService.validateUser(command.getUserId());
        UserValidatedEvent event = new UserValidatedEvent();
        event.reservationId(command.getReservationId());
        event.userId(command.getUserId());
       if(user != null){
           event.firstName(user.getFirstName());
           event.lastName(user.getLastName());
           event.email(user.getEmail());
           event.isClient(true);
       }else{
           event.email("wrong@email.com");
           event.isClient(false);
       }

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.user-validated", "", event);
    }
 /*  @RabbitListener(queues = {"q.user-service.check-isClient-users"})
    public void onCheckClientUsers(CheckUserIsClientCommand command) {
        LOGGER.info("Received command: " + command);
        List<User> clientUsers = userService.getAllUsers();
        for (User clientUser : clientUsers) {
            UserValidatedEvent event = new UserValidatedEvent();
            event.reservationId(command.getReservationId());
            event.userId(clientUser.id());
            event.firstName(clientUser.firstName());
            event.lastName(clientUser.lastName());
            event.email(clientUser.email());
            event.isClient(clientUser.isClient());

            LOGGER.info("Sending event: " + event);
            this.rabbitTemplate.convertAndSend("x.users-isClient", "", event);
        }
    }*/
}
