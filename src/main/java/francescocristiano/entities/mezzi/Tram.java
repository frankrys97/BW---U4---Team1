package francescocristiano.entities.mezzi;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("tram")
public class Tram extends Mezzo {

    public Tram() {
    }

    public Tram(boolean inServizio) {
        super(100, inServizio);
    }

    @Override
    public String toString() {
        return "Tram: " + super.toString();
    }
}
