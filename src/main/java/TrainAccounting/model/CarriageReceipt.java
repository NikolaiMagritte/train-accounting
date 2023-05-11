package TrainAccounting.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class CarriageReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "carriageReceipt", cascade = CascadeType.ALL)
    private List<CarriagePassport> carriages = new ArrayList<>();

    public CarriageReceipt(List<CarriagePassport> carriages) {
        this.carriages = carriages;
    }

    public CarriageReceipt() {
    }
}
