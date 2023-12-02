package be.ucll.da.ownerservice.domain;

public interface EventSender {

    void sendOwnerCreatedEvent(Owner owner);
}
