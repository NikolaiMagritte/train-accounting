package TrainAccounting.services;

import TrainAccounting.model.CarriagePassport;
import TrainAccounting.model.Station;
import TrainAccounting.model.StationPath;
import TrainAccounting.repositories.CarriagePassportRepository;
import TrainAccounting.repositories.StationPathRepository;
import TrainAccounting.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DepartCarriagesServiceImpl implements DepartCarriagesService {
    private static final Logger logger = LoggerFactory.getLogger(DepartCarriagesServiceImpl.class);

    private final CarriagePassportRepository carriagePassportRepository;
    private final StationRepository stationRepository;
    private final StationPathRepository stationPathRepository;

    @Override
    public void departCarriages(List<CarriagePassport> carriages, Station station, StationPath stationPath) {
        Station existingStation = stationRepository.findById(station.getId())
                .orElseThrow(() -> {
                    String errorMessage = "Station not found";
                    logger.error(errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        StationPath existingPath = stationPathRepository.findById(stationPath.getId())
                .orElseThrow(() -> {
                    String errorMessage = "StationPath not found";
                    logger.error(errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        if (!existingPath.getStation().equals(existingStation)) {
            String errorMessage = "Station path does not belong to the Station";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        List<CarriagePassport> existingCarriages = existingPath.getCarriages();
        existingCarriages.removeAll(carriages);

        for (CarriagePassport carriage : carriages) {
            carriage.setSequenceNumber(0);
            carriage.setStationPath(null);
            carriagePassportRepository.save(carriage);
        }

        logger.info("Departed carriages: {}", carriages);
        logger.info("Carriages departed successfully from station: {}", existingStation.getName());
    }
}

