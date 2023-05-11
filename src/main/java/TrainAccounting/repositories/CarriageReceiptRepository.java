package TrainAccounting.repositories;

import TrainAccounting.model.CarriageReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarriageReceiptRepository extends JpaRepository <CarriageReceipt, Long> {

}
