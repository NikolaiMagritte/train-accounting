package TrainAccounting.services;

import TrainAccounting.model.CarriagePassport;
import TrainAccounting.model.Station;
import TrainAccounting.model.StationPath;
import TrainAccounting.repositories.CarriagePassportRepository;
import TrainAccounting.repositories.StationPathRepository;
import TrainAccounting.repositories.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RearrangeCarriagesServiceImplTest {
    @Mock
    private CarriagePassportRepository carriagePassportRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private StationPathRepository stationPathRepository;

    @InjectMocks
    private RearrangeCarriagesServiceImpl rearrangeCarriagesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRearrangeCarriagesWithValidData() {
        Station station = new Station(1L, "Station A");
        StationPath sourcePath = new StationPath(1L, "Source Path", station, new ArrayList<>());
        StationPath destinationPath = new StationPath(2L, "Destination Path", station, new ArrayList<>());

        List<CarriagePassport> carriages = new ArrayList<>();
        CarriagePassport carriage1 = new CarriagePassport(1L, 1, 1);
        CarriagePassport carriage2 = new CarriagePassport(2L, 2, 2);
        CarriagePassport carriage3 = new CarriagePassport(3L, 3, 3);
        carriages.add(carriage1);
        carriages.add(carriage2);
        carriages.add(carriage3);

        sourcePath.getCarriages().add(carriage1);
        sourcePath.getCarriages().add(carriage2);
        destinationPath.getCarriages().add(carriage3);

        when(stationRepository.findById(station.getId())).thenReturn(Optional.of(station));
        when(stationPathRepository.findById(sourcePath.getId())).thenReturn(Optional.of(sourcePath));
        when(stationPathRepository.findById(destinationPath.getId())).thenReturn(Optional.of(destinationPath));

        assertDoesNotThrow(() -> rearrangeCarriagesService.rearrangeCarriages(carriages, station, sourcePath, destinationPath));

        assertEquals(0, sourcePath.getCarriages().size());
        assertFalse(sourcePath.getCarriages().contains(carriage1));
        assertFalse(sourcePath.getCarriages().contains(carriage2));
        assertTrue(destinationPath.getCarriages().contains(carriage1));
        assertTrue(destinationPath.getCarriages().contains(carriage2));
        assertTrue(destinationPath.getCarriages().contains(carriage3));
        assertEquals(destinationPath, carriage1.getStationPath());
        assertEquals(destinationPath, carriage2.getStationPath());
        assertEquals(destinationPath, carriage3.getStationPath());
        assertEquals(4, carriage1.getSequenceNumber());
        assertEquals(5, carriage2.getSequenceNumber());
        assertEquals(6, carriage3.getSequenceNumber());

        verify(carriagePassportRepository, times(3)).save(any(CarriagePassport.class));
        verify(stationPathRepository, times(2)).save(any(StationPath.class));
    }
}
