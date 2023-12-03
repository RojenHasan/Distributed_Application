package be.ucll.da.ownerservice.web;

import be.ucll.da.ownerservice.api.OwnerApiDelegate;
import be.ucll.da.ownerservice.api.model.AddOwnerRequest;
import be.ucll.da.ownerservice.api.model.ApiOwner;
import be.ucll.da.ownerservice.api.model.ApiOwners;
import be.ucll.da.ownerservice.domain.Owner;
import be.ucll.da.ownerservice.domain.OwnerService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OwnerController implements OwnerApiDelegate {
    private  final OwnerService ownerService;
    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @Override
    public ResponseEntity<ApiOwner> getOwnerByOwnerId(Integer id) {
        Owner owner = ownerService.getOwner1(id)
                .orElseThrow(() -> new RuntimeException("Owner does not exist"));


        ApiOwner apiOwner = new ApiOwner();
        apiOwner.setOwnerId(owner.getId());
        apiOwner.setCarType((owner.getCarType()));
        apiOwner.setFirstName(owner.getFirstName());
        apiOwner.setLastName(owner.getLastName());
        apiOwner.setEmail(owner.getEmail());
        apiOwner.setAddress(owner.getAddress());

        return ResponseEntity.ok(apiOwner);
    }
    @Override
    public ResponseEntity<Void> createOwner(@Valid @RequestBody ApiOwner apiOwner) {
        ownerService.addOwner(apiOwner);
        return ResponseEntity.ok().build();
    }
   /* @Override
    @PostMapping
    public ResponseEntity<Void> createOwner(@Valid @RequestBody ApiOwner apiOwner) {
        AddOwnerRequest addOwnerRequest = new AddOwnerRequest();
        addOwnerRequest.setFirstName(apiOwner.getFirstName());
        addOwnerRequest.setLastName(apiOwner.getLastName());
        addOwnerRequest.setEmail(apiOwner.getEmail());
        addOwnerRequest.setPassword(apiOwner.getPassword());
        addOwnerRequest.setCarType(apiOwner.getCarType());

        ownerService.addOwner(addOwnerRequest);

        return ResponseEntity.ok().build();
    }*/
   @Override
   public ResponseEntity<ApiOwners> getOwners() {
       ApiOwners owners = new ApiOwners();
       owners.setOwners(
               ownerService.getAllOwners().stream()
                       .map(this::toDto)
                       .toList()
       );

       return ResponseEntity.ok(owners);
   }

    public ResponseEntity<ApiOwners> getOwners(String carType) {
        ApiOwners owners = new ApiOwners();
        owners.setOwners(
                ownerService.getOwners(carType).stream()
                        .map(this::toDto)
                        .toList()
        );

        return ResponseEntity.ok(owners);
    }

    private ApiOwner toDto(Owner owner) {
        return new ApiOwner()
                .ownerId(owner.getId())
                .carType(owner.getCarType())
                .firstName(owner.getFirstName())
                .lastName(owner.getLastName())
                .email(owner.getEmail())
                .address(owner.getAddress());
    }
    public ResponseEntity<ApiOwners> getOwners1() {
        List<Owner> owners = ownerService.getAllOwners();

        List<ApiOwner> apiOwners = owners.stream()
                .map(owner -> {
                    ApiOwner apiOwner = new ApiOwner();
                    apiOwner.setFirstName(owner.getFirstName());
                    apiOwner.setLastName(owner.getLastName());
                    apiOwner.setEmail(owner.getEmail());
                    apiOwner.setAddress(owner.getAddress());
                    apiOwner.setCarType(owner.getCarType());
                    return apiOwner;
                })
                .collect(Collectors.toList());

        ApiOwners apiOwnersResponse = new ApiOwners();
        apiOwnersResponse.setOwners(apiOwners);

        return ResponseEntity.ok(apiOwnersResponse);
    }
   /* @PostMapping("/confirm-reservation/{ownerId}/{reservationId}")
    public ResponseEntity<Void> confirmReservation(@PathVariable Integer ownerId, @PathVariable Integer reservationId) {
        ownerService.confirmReservation(ownerId, reservationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decline-reservation/{ownerId}/{reservationId}")
    public ResponseEntity<Void> declineReservation(@PathVariable Integer ownerId, @PathVariable Integer reservationId) {
        ownerService.declineReservation(ownerId, reservationId);
        return ResponseEntity.ok().build();
    }*/

}
