package TrainAccounting.services;

import TrainAccounting.model.CarriagePassport;
import TrainAccounting.model.Station;
import TrainAccounting.model.StationPath;

import java.util.List;

public interface AcceptCarriagesService {
    void acceptCarriages (List<CarriagePassport> carriages, Station station, StationPath stationPath);
}
