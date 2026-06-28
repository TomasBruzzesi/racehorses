package com.uade.swingmagiccafe.service;

public class SinDescuento implements DescuentoStrategy {
    @Override
    public String getNombre() { return "Sin descuento"; }

    @Override
    public double aplicar(double subtotal) { return subtotal; }

    @Override
    public String toString() { return getNombre(); }
}
