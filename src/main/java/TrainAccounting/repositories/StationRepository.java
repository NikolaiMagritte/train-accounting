package TrainAccounting.repositories;

import TrainAccounting.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository <Station, Long> {

}
