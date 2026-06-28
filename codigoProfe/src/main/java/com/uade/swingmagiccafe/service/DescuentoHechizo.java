package com.uade.swingmagiccafe.service;

public class DescuentoHechizo implements DescuentoStrategy {
    @Override
    public String getNombre() { return "Cupón mágico $500"; }

    @Override
    public double aplicar(double subtotal) { return Math.max(0, subtotal - 500); }

    @Override
    public String toString() { return getNombre(); }
}
