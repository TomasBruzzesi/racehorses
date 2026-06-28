package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Singleton para centralizar la creacion del EntityManagerFactory.
 * Reemplaza a DBConnection: la configuracion JDBC vive en persistence.xml.
 */
public class JPAUtil {

    private static JPAUtil instance;
    private final EntityManagerFactory entityManagerFactory;

    private JPAUtil() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("horseRacePU");
    }

    public static JPAUtil getInstance() {
        if (instance == null) {
            instance = new JPAUtil();
        }
        return instance;
    }

    public EntityManager crearEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void cerrar() {
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
