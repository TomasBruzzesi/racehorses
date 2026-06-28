package com.uade.swingmagiccafe.dao;

import com.uade.swingmagiccafe.config.JPAUtil;
import com.uade.swingmagiccafe.dto.PedidoConfirmadoDTO;
import com.uade.swingmagiccafe.dto.PedidoListadoDTO;
import com.uade.swingmagiccafe.model.ItemPedido;
import com.uade.swingmagiccafe.model.Pedido;
import com.uade.swingmagiccafe.model.Producto;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO: encapsula el acceso a datos de pedidos.
 * Antes vivia aca el SQL de pedidos y pedido_items.
 * Ahora usamos EntityManager para persistir objetos mapeados con JPA.
 */
public class PedidoDAO {

    public List<PedidoListadoDTO> listarTodos() {
        EntityManager entityManager = JPAUtil.getInstance().crearEntityManager();
        try {           
            List<Pedido> pedidos = entityManager
            		.createQuery("FROM Pedido", Pedido.class)
                    .getResultList();

            List<PedidoListadoDTO> resultado = new ArrayList<>();
            for (Pedido pedido : pedidos) {
                resultado.add(new PedidoListadoDTO(
                        pedido.getId(),
                        pedido.getSubtotal(),
                        pedido.getDescuentoAplicado(),
                        pedido.getTotal(),
                        pedido.getFechaCreacion().toString()
                ));
            }
            return resultado;
        } finally {
            entityManager.close();
        }
    }

    public PedidoConfirmadoDTO guardar(Pedido pedido, String nombreDescuento, double subtotal, double total) {
        EntityManager entityManager = JPAUtil.getInstance().crearEntityManager();
        try {
            entityManager.getTransaction().begin();

            pedido.confirmar(nombreDescuento, subtotal, total);

            // Los productos que vienen desde la UI pueden estar detachados.
            // Pedimos una referencia administrada por este EntityManager para que Hibernate pueda guardar la FK.
            for (ItemPedido item : pedido.getItems()) {
                Producto productoPersistente = entityManager.getReference(
                        Producto.class,
                        item.getProducto().getId()
                );
                item.asignarProductoPersistente(productoPersistente);
                item.asignarPedido(pedido);
            }

            entityManager.persist(pedido);
            entityManager.getTransaction().commit();

            return new PedidoConfirmadoDTO(pedido.getId(), subtotal, nombreDescuento, total);
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar pedido con JPA/Hibernate", e);
        } finally {
            entityManager.close();
        }
    }
}
