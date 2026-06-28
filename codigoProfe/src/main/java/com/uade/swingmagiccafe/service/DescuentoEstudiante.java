package com.uade.swingmagiccafe.service;

public class DescuentoEstudiante implements DescuentoStrategy {
    @Override
    public String getNombre() { return "Descuento estudiante 15%"; }

    @Override
    public double aplicar(double subtotal) { return subtotal * 0.85; }

    @Override
    public String toString() { return getNombre(); }
}
