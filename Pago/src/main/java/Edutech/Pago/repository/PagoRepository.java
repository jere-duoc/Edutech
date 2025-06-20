package Edutech.Pago.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Edutech.Pago.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long>{

}
