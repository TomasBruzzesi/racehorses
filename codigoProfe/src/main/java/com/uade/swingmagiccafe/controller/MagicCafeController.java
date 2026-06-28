package com.uade.swingmagiccafe.controller;

import com.uade.swingmagiccafe.dto.ItemPedidoDTO;
import com.uade.swingmagiccafe.dto.PedidoConfirmadoDTO;
import com.uade.swingmagiccafe.dto.PedidoListadoDTO;
import com.uade.swingmagiccafe.dto.ProductoDTO;
import com.uade.swingmagiccafe.model.ItemPedido;
import com.uade.swingmagiccafe.model.Pedido;
import com.uade.swingmagiccafe.model.Producto;
import com.uade.swingmagiccafe.service.DescuentoStrategy;
import com.uade.swingmagiccafe.service.PedidoService;
import com.uade.swingmagiccafe.service.SinDescuento;

import java.util.ArrayList;
import java.util.List;

public class MagicCafeController {

    private Pedido pedido;
    private final PedidoService pedidoService;
    private DescuentoStrategy descuentoSeleccionado;
    private List<Producto> productos;

    public MagicCafeController() {
        this.pedido = new Pedido();
        this.pedidoService = new PedidoService();
        this.descuentoSeleccionado = new SinDescuento();
        this.productos = pedidoService.listarProductos();
    }

    public List<ProductoDTO> obtenerProductos() {
        productos = pedidoService.listarProductos();

        List<ProductoDTO> productosDTO = new ArrayList<>();
        for (Producto producto : productos) {
            productosDTO.add(new ProductoDTO(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getEmoji()
            ));
        }
        return productosDTO;
    }

    public List<PedidoListadoDTO> obtenerPedidosCreados() {
        return pedidoService.listarPedidos();
    }

    public void agregarProducto(ProductoDTO productoDTO, int cantidad) {
        Producto producto = buscarProductoPorId(productoDTO.getId());

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        pedido.agregarItem(producto, cantidad);
    }

    public List<ItemPedidoDTO> obtenerItemsPedido() {
        List<ItemPedidoDTO> itemsDTO = new ArrayList<>();

        for (ItemPedido item : pedido.getItems()) {
            itemsDTO.add(new ItemPedidoDTO(
                    item.getProducto().getNombre(),
                    item.getCantidad(),
                    item.getProducto().getPrecio(),
                    item.getSubtotal()
            ));
        }
        return itemsDTO;
    }

    public double calcularSubtotal() {
        return pedido.calcularSubtotal();
    }

    public double calcularTotal() {
        return pedidoService.calcularTotal(pedido, descuentoSeleccionado);
    }

    public void seleccionarDescuento(DescuentoStrategy descuento) {
        this.descuentoSeleccionado = descuento;
    }

    public void eliminarItem(int index) {
        pedido.eliminarItem(index);
    }

    public void vaciarPedido() {
        pedido.vaciar();
    }

    public boolean pedidoEstaVacio() {
        return pedido.estaVacio();
    }

    public PedidoConfirmadoDTO confirmarPedido() {
        PedidoConfirmadoDTO pedidoConfirmadoDTO =
                pedidoService.confirmarPedido(pedido, descuentoSeleccionado);

        this.pedido = new Pedido();

        return pedidoConfirmadoDTO;
    }

    private Producto buscarProductoPorId(int productoId) {
        for (Producto producto : productos) {
            if (producto.getId() == productoId) {
                return producto;
            }
        }
        return null;
    }
}
