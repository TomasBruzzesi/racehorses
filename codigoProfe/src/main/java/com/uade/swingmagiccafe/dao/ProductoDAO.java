package com.uade.swingmagiccafe.dao;

import com.uade.swingmagiccafe.config.JPAUtil;
import com.uade.swingmagiccafe.model.Producto;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO: encapsula el acceso a datos de productos.
 * Ahora ya no usa SQL manual ni JDBC: usa JPA/Hibernate.
 */
public class ProductoDAO {

    public List<Producto> listarTodos() {
        EntityManager entityManager = JPAUtil.getInstance().crearEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT p FROM Producto p ORDER BY p.id", Producto.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Producto buscarPorId(int id) {
        EntityManager entityManager = JPAUtil.getInstance().crearEntityManager();
        try {
            return entityManager.find(Producto.class, id);
        } finally {
            entityManager.close();
        }
    }

    public long contarProductos() {
        EntityManager entityManager = JPAUtil.getInstance().crearEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT COUNT(p) FROM Producto p", Long.class)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    public void insertarProductosInicialesSiHaceFalta() {
        if (contarProductos() > 0) {
            return;
        }

        EntityManager entityManager = JPAUtil.getInstance().crearEntityManager();
        try {
            entityManager.getTransaction().begin();

            entityManager.persist(new Producto("Cafe de invisibilidad", 1200, "☕"));
            entityManager.persist(new Producto("Medialuna voladora", 900, "🥐"));
            entityManager.persist(new Producto("Torta encantada", 1600, "🍰"));
            entityManager.persist(new Producto("Limonada anti-dementores", 1100, "🍋"));
            entityManager.persist(new Producto("Combo mago hambriento", 3500, "🧙"));

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
