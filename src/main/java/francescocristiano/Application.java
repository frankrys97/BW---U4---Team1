package francescocristiano;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Application {

    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("u4bwteam1");

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
