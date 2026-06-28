package com.uade.swingmagiccafe.dto;

public class ItemPedidoDTO {
    private final String producto;
    private final int cantidad;
    private final double precioUnitario;
    private final double subtotal;

    public ItemPedidoDTO(String producto, int cantidad, double precioUnitario, double subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public String getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double getSubtotal() { return subtotal; }
}
