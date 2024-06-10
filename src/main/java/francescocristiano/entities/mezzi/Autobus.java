package francescocristiano.entities.mezzi;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("autobus")
public class Autobus extends Mezzo {
}
