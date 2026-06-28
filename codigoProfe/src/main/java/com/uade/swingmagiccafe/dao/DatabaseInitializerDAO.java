package com.uade.swingmagiccafe.dao;

import com.uade.swingmagiccafe.config.JPAUtil;

/**
 * DAO auxiliar para inicializar la base de datos.
 * Con Hibernate ya no escribimos CREATE TABLE manual:
 * las tablas se generan/actualizan desde las @Entity segun persistence.xml.
 */
public class DatabaseInitializerDAO {

    private final ProductoDAO productoDAO;

    public DatabaseInitializerDAO(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    public void inicializarBaseDeDatos() {
        // Fuerza la inicializacion del EntityManagerFactory.
        JPAUtil.getInstance();

        // Inserta datos de prueba si la tabla productos esta vacia.
        productoDAO.insertarProductosInicialesSiHaceFalta();
    }
}
