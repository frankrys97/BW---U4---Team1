package francescocristiano.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException(UUID id) {
        super("Il record con l'id " + id + " non è stato trovato!");
    }
}