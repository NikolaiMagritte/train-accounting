package TrainAccounting.config;

import TrainAccounting.model.*;
import TrainAccounting.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final CarriagePassportRepository carriagePassportRepository;
    private final CarriageReceiptRepository carriageReceiptRepository;
    private final StationRepository stationRepository;
    private final StationPathRepository stationPathRepository;
    private final CargoRepository cargoRepository;

    @Override
    public void run(String... args) throws Exception {
        Cargo cargo1 = new Cargo("steel");
        Cargo cargo2 = new Cargo("coal");
        Cargo cargo3 = new Cargo("wood");

        Station station1 = new Station("A");
        StationPath stationPath1 = new StationPath("IA", station1);
        StationPath stationPath2 = new StationPath("IIA", station1);

        List<StationPath> stationPaths = new ArrayList<>();
        stationPaths.add(stationPath1);
        stationPaths.add(stationPath2);

        station1.setStationPaths(stationPaths);


        CarriagePassport carriage1 = new CarriagePassport(100, 0,
                CarriageType.PLATFORM, 100, 300, 100,
                null, cargo1, stationPath1);
        CarriagePassport carriage2 = new CarriagePassport(102, 0,
                CarriageType.PLATFORM, 100, 300, 100,
                null, cargo2, stationPath1);
        CarriagePassport carriage3 = new CarriagePassport(400, 0,
                CarriageType.PLATFORM, 100, 300, 100,
                null, cargo3, stationPath1);

        stationRepository.save(station1);
        stationPathRepository.save(stationPath1);
        stationPathRepository.save(stationPath2);
        cargoRepository.save(cargo1);
        cargoRepository.save(cargo2);
        cargoRepository.save(cargo3);
        carriagePassportRepository.save(carriage1);
        carriagePassportRepository.save(carriage2);
        carriagePassportRepository.save(carriage3);
    }
}
