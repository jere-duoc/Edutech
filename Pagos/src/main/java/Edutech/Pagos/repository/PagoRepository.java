package Edutech.Pagos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Edutech.Pagos.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long>{

}
