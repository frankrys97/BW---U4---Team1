package francescocristiano.entities.mezzi;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("tram")
public class Tram extends Mezzo {
}
