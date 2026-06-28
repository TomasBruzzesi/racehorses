package com.uade.swingmagiccafe.dto;

public class PedidoConfirmadoDTO {
    private final int pedidoId;
    private final double subtotal;
    private final String descuentoAplicado;
    private final double total;

    public PedidoConfirmadoDTO(int pedidoId, double subtotal, String descuentoAplicado, double total) {
        this.pedidoId = pedidoId;
        this.subtotal = subtotal;
        this.descuentoAplicado = descuentoAplicado;
        this.total = total;
    }

    public int getPedidoId() { return pedidoId; }
    public double getSubtotal() { return subtotal; }
    public String getDescuentoAplicado() { return descuentoAplicado; }
    public double getTotal() { return total; }
}
