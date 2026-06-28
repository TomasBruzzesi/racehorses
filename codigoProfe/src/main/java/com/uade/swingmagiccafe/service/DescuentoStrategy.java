package com.uade.swingmagiccafe.service;

public interface DescuentoStrategy {
    String getNombre();
    double aplicar(double subtotal);
}
