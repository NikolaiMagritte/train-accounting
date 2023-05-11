package TrainAccounting.services;

import TrainAccounting.model.CarriagePassport;
import TrainAccounting.model.Station;
import TrainAccounting.model.StationPath;
import TrainAccounting.repositories.CarriagePassportRepository;
import TrainAccounting.repositories.StationPathRepository;
import TrainAccounting.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DepartCarriagesServiceImpl implements DepartCarriagesService{
    private final CarriagePassportRepository carriagePassportRepository;
    private final StationRepository stationRepository;
    private final StationPathRepository stationPathRepository;

    @Override
    public void departCarriages(List<CarriagePassport> carriages, Station station, StationPath stationPath) {
        Station existingStation = stationRepository.findById(station.getId())
                .orElseThrow(() -> new IllegalArgumentException("Station not found"));
        StationPath existingPath = stationPathRepository.findById(stationPath.getId())
                .orElseThrow(() -> new IllegalArgumentException("StationPath not found"));

        if (!existingPath.getStation().equals(existingStation)) {
            throw new IllegalArgumentException("Station path does not belong to the Station");
        }

        List<CarriagePassport> existingCarriages = existingPath.getCarriages();
        existingCarriages.removeAll(carriages);

        for (CarriagePassport carriage : carriages) {
            carriage.setSequenceNumber(0);
            carriage.setStationPath(null);
            carriagePassportRepository.save(carriage);
        }
    }
}
