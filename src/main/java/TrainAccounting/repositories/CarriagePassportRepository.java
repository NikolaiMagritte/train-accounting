package TrainAccounting.repositories;

import TrainAccounting.model.CarriagePassport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarriagePassportRepository extends JpaRepository <CarriagePassport, Long> {

}
