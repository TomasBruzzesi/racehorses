package com.uade.swingmagiccafe.dto;

public class PedidoListadoDTO {

    private final int id;
    private final double subtotal;
    private final String descuentoAplicado;
    private final double total;
    private final String fechaCreacion;

    public PedidoListadoDTO(int id, double subtotal, String descuentoAplicado, double total, String fechaCreacion) {
        this.id = id;
        this.subtotal = subtotal;
        this.descuentoAplicado = descuentoAplicado;
        this.total = total;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() {
        return id;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public String getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public double getTotal() {
        return total;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }
}
