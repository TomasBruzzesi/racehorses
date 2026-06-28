package com.uade.swingmagiccafe.service;

import com.uade.swingmagiccafe.dao.DatabaseInitializerDAO;
import com.uade.swingmagiccafe.dao.PedidoDAO;
import com.uade.swingmagiccafe.dao.ProductoDAO;
import com.uade.swingmagiccafe.dto.PedidoConfirmadoDTO;
import com.uade.swingmagiccafe.dto.PedidoListadoDTO;
import com.uade.swingmagiccafe.model.Pedido;
import com.uade.swingmagiccafe.model.Producto;

import java.util.List;

public class PedidoService {

    private final ProductoDAO productoDAO;
    private final PedidoDAO pedidoDAO;
    private final DatabaseInitializerDAO databaseInitializerDAO;

    public PedidoService() {
        this.productoDAO = new ProductoDAO();
        this.pedidoDAO = new PedidoDAO();
        this.databaseInitializerDAO = new DatabaseInitializerDAO(productoDAO);
        this.databaseInitializerDAO.inicializarBaseDeDatos();
    }

    public double calcularTotal(Pedido pedido, DescuentoStrategy descuento) {
        return descuento.aplicar(pedido.calcularSubtotal());
    }

    public List<Producto> listarProductos() {
        return productoDAO.listarTodos();
    }

    public List<PedidoListadoDTO> listarPedidos() {
        return pedidoDAO.listarTodos();
    }

    public PedidoConfirmadoDTO confirmarPedido(Pedido pedido, DescuentoStrategy descuento) {
        if (pedido.estaVacio()) {
            throw new RuntimeException("No se puede confirmar un pedido vacio");
        }

        double subtotal = pedido.calcularSubtotal();
        double total = calcularTotal(pedido, descuento);

        return pedidoDAO.guardar(pedido, descuento.getNombre(), subtotal, total);
    }
}
