package francescocristiano.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String id) {
        super("Il record con l'id " + id + " non è stato trovato!");
    }
}