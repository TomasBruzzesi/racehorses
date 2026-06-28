package com.uade.swingmagiccafe.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Singleton para centralizar la creacion del EntityManagerFactory.
 *
 * Antes teniamos DBConnection como Singleton para JDBC.
 * Ahora, con JPA/Hibernate, centralizamos el EntityManagerFactory.
 */
public class JPAUtil {

    private static JPAUtil instance;
    private final EntityManagerFactory entityManagerFactory;

    private JPAUtil() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("magiccafePU");
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
