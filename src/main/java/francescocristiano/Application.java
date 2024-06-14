package francescocristiano;

/*
import francescocristiano.dao.TrattaDAO;
*/

import francescocristiano.entities.service.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Application {

    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("u4bwteam1");

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        Service service = new Service(em);
        service.startApp();
    }
}
