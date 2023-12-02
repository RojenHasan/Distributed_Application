package be.ucll.da.reservationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    /**
     *is responsible for converting messages to and from JSON format when they are sent or received from a RabbitMQ message broker.
     */
    @Bean
    public Jackson2JsonMessageConverter converter() {
        ObjectMapper mapper =
                new ObjectMapper()
                        .registerModule(new ParameterNamesModule())
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());

        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter, CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    //1
    @Bean
    public Declarables createValidateUserQueue(){
        return new Declarables(new Queue("q.user-service.validate-user"));
    }
    @Bean
    public Declarables createValidateCarQueue(){
        return new Declarables(new Queue("q.car-service.validate-car"));
    }


    @Bean
    public Declarables createUserValidatedExchange(){
        return new Declarables(
                new FanoutExchange("x.user-validated"),
                new Queue("q.user-validated.reservation-service" ),
                new Queue("q.user-validated.api-gateway" ),
                new Binding("q.user-validated.reservation-service", Binding.DestinationType.QUEUE, "x.user-validated", "user-validated.reservation-service", null),
                new Binding("q.user-validated.api-gateway",
                        Binding.DestinationType.QUEUE,
                        "x.user-validated",
                        "user-validated.api-gateway",
                        null));
    }

    @Bean
    public Declarables createCarValidatedExchange(){
        return new Declarables(
                new FanoutExchange("x.car-validated"),
                new Queue("q.car-validated.reservation-service" ),
                new Queue("q.car-validated.api-gateway" ),
                new Binding("q.car-validated.reservation-service", Binding.DestinationType.QUEUE, "x.car-validated", "car-validated.reservation-service", null),
                new Binding("q.car-validated.api-gateway",
                        Binding.DestinationType.QUEUE,
                        "x.car-validated",
                        "car-validated.api-gateway",
                        null));
    }
   /* @Bean
    public Declarables createOwnerValidatedExchange(){
        return new Declarables(
                new FanoutExchange("x.owner-validated"),
                new Queue("q.owner-validated.reservation-service" ),
                new Queue("q.owner-validated.api-gateway" ),
                new Binding("q.owner-validated.reservation-service", Binding.DestinationType.QUEUE, "x.owner-validated", "owner-validated.reservation-service", null),
                new Binding("q.owner-validated.api-gateway",
                        Binding.DestinationType.QUEUE,
                        "x.owner-validated",
                        "owner-validated.api-gateway",
                        null));
    }*/

    @Bean
    public Declarables createBookCarQueue(){
        return new Declarables(new Queue("q.car-service.book-car"));
    }

    @Bean
    public Declarables createCarBookedExchange(){
        return new Declarables(
                new FanoutExchange("x.car-bookings"),
                new Queue("q.car-bookings.reservation-service" ),
                new Binding("q.car-bookings.reservation-service", Binding.DestinationType.QUEUE, "x.car-bookings", "car-bookings.reservation-service", null));
    }

    @Bean
    public Declarables createReleaseCarQueue(){
        return new Declarables(new Queue("q.car-service.release-car"));
    }

    @Bean
    public Declarables createCarReleasedExchange(){
        return new Declarables(
                new FanoutExchange("x.car-releases"));
    }

    /*@Bean
    public Declarables createOpenAccountQueue(){
        return new Declarables(new Queue("q.account-service.open-account"));
    }

    @Bean
    public Declarables createAccountOpenedExchange(){
        return new Declarables(
                new FanoutExchange("x.account-openings"),
                new Queue("q.account-openings.appointment-service" ),
                new Binding("q.account-openings.appointment-service", Binding.DestinationType.QUEUE, "x.account-openings", "account-openings.appointment-service", null));
    }

    @Bean
    public Declarables createCloseAccountQueue(){
        return new Declarables(new Queue("q.account-service.close-account"));
    }

    @Bean
    public Declarables createAccountTerminationsExchange(){
        return new Declarables(
                new FanoutExchange("x.account-terminations"));
    }*/

    @Bean
    public Declarables createReservationFinalizedExchange(){
        return new Declarables(
                new FanoutExchange("x.reservation-finalized"),
                new Queue("q.reservation-finalized.api-gateway" ),
                new Binding("q.reservation-finalized.api-gateway", Binding.DestinationType.QUEUE, "x.reservation-finalized", "reservation-finalized.api-gateway", null));
    }

    @Bean
    public Declarables createSendEmailQueue(){
        return new Declarables(new Queue("q.notification-service.send-email"));
    }

}