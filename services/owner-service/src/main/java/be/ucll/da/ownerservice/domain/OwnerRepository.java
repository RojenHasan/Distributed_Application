package be.ucll.da.ownerservice.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OwnerRepository extends CrudRepository<Owner, Long> {

    List<Owner> findAllByCarType(String carType);
    List<Owner> findById(int id);

}