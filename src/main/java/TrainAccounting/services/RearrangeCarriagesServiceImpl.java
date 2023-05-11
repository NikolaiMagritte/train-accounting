package TrainAccounting.services;

import TrainAccounting.model.CarriagePassport;
import TrainAccounting.model.CarriageReceipt;
import TrainAccounting.model.Station;
import TrainAccounting.model.StationPath;
import TrainAccounting.repositories.CarriagePassportRepository;
import TrainAccounting.repositories.StationPathRepository;
import TrainAccounting.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RearrangeCarriagesServiceImpl implements RearrangeCarriagesService {
    private final CarriagePassportRepository carriagePassportRepository;
    private final StationRepository stationRepository;
    private final StationPathRepository stationPathRepository;


    @Override
    public void rearrangeCarriages(List<CarriagePassport> carriages, Station station,
                                   StationPath sourcePath, StationPath destinationPath) {
        Station existingStation = stationRepository.findById(station.getId())
                .orElseThrow(() -> new IllegalArgumentException("Station not found"));
        StationPath existingSourcePath = stationPathRepository.findById(sourcePath.getId())
                .orElseThrow(() -> new IllegalArgumentException("StationPath not found"));
        StationPath existingDestinationPath = stationPathRepository.findById(destinationPath.getId())
                .orElseThrow(() -> new IllegalArgumentException("StationPath not found"));

        if (!existingSourcePath.getStation().equals(existingStation)
                || !existingDestinationPath.getStation().equals(existingStation)) {
            throw new IllegalArgumentException("StationPath does not belong to the Station");
        }

        // Удаляем вагоны с текущего пути
        List<CarriagePassport> existingSourceCarriages = existingSourcePath.getCarriages();
        existingSourceCarriages.removeAll(carriages);
        stationPathRepository.save(existingSourcePath);

        List<CarriagePassport> existingCarriages = existingDestinationPath.getCarriages();

        int lastSequenceNumber = existingCarriages.isEmpty() ? 0 : existingCarriages.get(existingCarriages.size() - 1).getSequenceNumber();

        for (CarriagePassport carriage : carriages) {
            carriage.setSequenceNumber(lastSequenceNumber + 1);
            carriage.setStationPath(existingDestinationPath);
            carriagePassportRepository.save(carriage);
            existingDestinationPath.getCarriages().add(carriage);
        }

        stationPathRepository.save(existingDestinationPath);
    }
}
