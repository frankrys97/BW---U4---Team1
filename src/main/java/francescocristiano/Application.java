package francescocristiano;

import jakarta.persistence.EntityManagerFactory;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

public class Application {

    private static final EntityManagerFactory emf = createEntityManagerFactory("u4bwteam1");

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
