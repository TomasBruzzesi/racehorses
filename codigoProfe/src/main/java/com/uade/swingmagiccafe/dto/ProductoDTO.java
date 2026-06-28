package com.uade.swingmagiccafe.dto;

public class ProductoDTO {
    private final int id;
    private final String nombre;
    private final double precio;
    private final String emoji;

    public ProductoDTO(int id, String nombre, double precio, String emoji) {
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
