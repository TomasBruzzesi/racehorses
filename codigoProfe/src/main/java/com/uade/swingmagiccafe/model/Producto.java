package com.uade.swingmagiccafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private double precio;

    @Column(nullable = false, length = 10)
    private String emoji;

    protected Producto() {
        // Constructor requerido por JPA/Hibernate
    }

    public Producto(String nombre, double precio, String emoji) {
        this.nombre = nombre;
        this.precio = precio;
        this.emoji = emoji;
    }

    public Producto(int id, String nombre, double precio, String emoji) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.emoji = emoji;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getEmoji() { return emoji; }

    @Override
    public String toString() {
        return emoji + " " + nombre + " - $" + precio;
    }
}
