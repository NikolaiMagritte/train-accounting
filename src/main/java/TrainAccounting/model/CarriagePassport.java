package TrainAccounting.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CarriagePassport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    @Column(name = "sequence_number")
    private int sequenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "carriage_type", nullable = false)
    private CarriageType carriageType;

    @Column(name = "carriage_weight")
    private double carriageWeight;

    @Column(name = "cargo_weight", nullable = false)
    private double cargoWeight;

    @Column(name = "cargo_capacity", nullable = false)
    private double cargoCapacity;

    @ManyToOne
    @JoinColumn(name = "carriage_receipt_id")
    private CarriageReceipt carriageReceipt;

    @ManyToOne
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "station_path_id")
    private StationPath stationPath;

    public CarriagePassport(int number, int sequenceNumber, CarriageType carriageType,
                            double carriageWeight, double cargoWeight, double cargoCapacity,
                            CarriageReceipt carriageReceipt, Cargo cargo, StationPath stationPath) {
        this.number = number;
        this.sequenceNumber = sequenceNumber;
        this.carriageType = carriageType;
        this.carriageWeight = carriageWeight;
        this.cargoWeight = cargoWeight;
        this.cargoCapacity = cargoCapacity;
        this.carriageReceipt = carriageReceipt;
        this.cargo = cargo;
        this.stationPath = stationPath;
    }

    public CarriagePassport(Long id, int number, int sequenceNumber) {
        this.id = id;
        this.number = number;
        this.sequenceNumber = sequenceNumber;
    }

    public CarriagePassport() {
    }
}
