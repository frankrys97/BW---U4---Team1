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
        /*TrattaDAO trattaDAO = new TrattaDAO(em);*/
        Service service = new Service(em);
        service.startApp();

        /*Tratta trattaTrovata = trattaDAO.findById("7e2ed687-8026-49b8-8ed5-ca08d80d5e53");

        trattaDAO.trovaMezziPerTratta(trattaTrovata).forEach(System.out::println);*/
    }
}
