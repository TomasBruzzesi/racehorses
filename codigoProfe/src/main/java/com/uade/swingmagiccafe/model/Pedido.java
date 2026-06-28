package com.uade.swingmagiccafe.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private double subtotal;

    @Column(name = "descuento_aplicado", nullable = false, length = 100)
    private String descuentoAplicado;

    @Column(nullable = false)
    private double total;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items = new ArrayList<>();

    public Pedido() {
        // Constructor requerido por JPA/Hibernate
    }

    public void agregarItem(Producto producto, int cantidad) {
        ItemPedido item = new ItemPedido(producto, cantidad);
        item.asignarPedido(this);
        items.add(item);
    }

    public void eliminarItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public void vaciar() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public List<ItemPedido> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double calcularSubtotal() {
        double subtotalCalculado = 0;
        for (ItemPedido item : items) {
            subtotalCalculado += item.getSubtotal();
        }
        return subtotalCalculado;
    }

    public void confirmar(String descuentoAplicado, double subtotal, double total) {
        if (estaVacio()) {
            throw new IllegalStateException("No se puede confirmar un pedido vacio");
        }
        this.descuentoAplicado = descuentoAplicado;
        this.subtotal = subtotal;
        this.total = total;
        this.fechaCreacion = LocalDateTime.now();
    }

    public int getId() { return id; }
    public double getSubtotal() { return subtotal; }
    public String getDescuentoAplicado() { return descuentoAplicado; }
    public double getTotal() { return total; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
