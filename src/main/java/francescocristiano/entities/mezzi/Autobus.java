package francescocristiano.entities.mezzi;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("autobus")
public class Autobus extends Mezzo {

    public Autobus() {
    }

    public Autobus(boolean inServizio) {
        super(50, inServizio);
    }

    @Override
    public String toString() {
        return "Autobus: " + super.toString();
    }
}
