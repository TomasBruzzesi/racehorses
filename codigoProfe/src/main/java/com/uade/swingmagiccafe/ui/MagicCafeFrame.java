package com.uade.swingmagiccafe.ui;

import com.uade.swingmagiccafe.controller.MagicCafeController;
import com.uade.swingmagiccafe.dto.ItemPedidoDTO;
import com.uade.swingmagiccafe.dto.PedidoConfirmadoDTO;
import com.uade.swingmagiccafe.dto.PedidoListadoDTO;
import com.uade.swingmagiccafe.dto.ProductoDTO;
import com.uade.swingmagiccafe.service.DescuentoEstudiante;
import com.uade.swingmagiccafe.service.DescuentoHechizo;
import com.uade.swingmagiccafe.service.DescuentoStrategy;
import com.uade.swingmagiccafe.service.SinDescuento;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dimension;

public class MagicCafeFrame extends JFrame {

    private final MagicCafeController controller = new MagicCafeController();

    private final JComboBox<ProductoDTO> productosCombo;
    private final JTextField cantidadField;
    private final JComboBox<DescuentoStrategy> descuentosCombo;
    private final DefaultTableModel tableModel;
    private final JTable tablaPedido;
    private final DefaultTableModel pedidosDbTableModel;
    private final JTable tablaPedidosDb;
    private final JLabel subtotalLabel;
    private final JLabel totalLabel;
    private final JLabel mensajeLabel;

    public MagicCafeFrame() {
        setTitle("Swing Magic Cafe - MVC + DTO + JDBC puro");
        setSize(1100, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        productosCombo = new JComboBox<>(controller.obtenerProductos().toArray(new ProductoDTO[0]));
        cantidadField = new JTextField("1", 5);
        descuentosCombo = new JComboBox<>(crearDescuentos());

        tableModel = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPedido = new JTable(tableModel);

        pedidosDbTableModel = new DefaultTableModel(new Object[]{"ID", "Subtotal", "Descuento", "Total", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPedidosDb = new JTable(pedidosDbTableModel);
        tablaPedidosDb.setRowHeight(26);
        tablaPedidosDb.setFillsViewportHeight(true);
        tablaPedidosDb.setPreferredScrollableViewportSize(new Dimension(500, 190));

        subtotalLabel = new JLabel("Subtotal: $0.00");
        totalLabel = new JLabel("Total: $0.00");
        mensajeLabel = new JLabel("Elegi productos magicos y arma tu pedido", SwingConstants.CENTER);

        construirPantalla();
        registrarEventos();
        actualizarTablaPedidosDb();
    }

    private void construirPantalla() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        root.setBackground(new Color(245, 241, 255));

        JLabel titulo = new JLabel("☕✨ Swing Magic Cafe ✨☕", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(80, 45, 120));
        root.add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setPreferredSize(new Dimension(520, 650));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Armar pedido"));
        panelFormulario.setBackground(new Color(250, 248, 255));

        JPanel filaProducto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaProducto.setBackground(new Color(250, 248, 255));
        productosCombo.setPreferredSize(new Dimension(280, 35));
        cantidadField.setPreferredSize(new Dimension(80, 35));

        filaProducto.add(new JLabel("Producto:"));
        filaProducto.add(productosCombo);

        filaProducto.add(new JLabel("Cantidad:"));
        filaProducto.add(cantidadField);

        JButton recargarPedidosButton = new JButton("Recargar pedidos creados desde DB 🔄");
        JPanel filaPedidosDb = new JPanel(new BorderLayout(5, 5));
        filaPedidosDb.setBackground(new Color(250, 248, 255));
        filaPedidosDb.setBorder(BorderFactory.createTitledBorder("Pedidos creados leidos desde la DB con JDBC"));

        JScrollPane scrollPedidosDb = new JScrollPane(tablaPedidosDb);
        scrollPedidosDb.setPreferredSize(new Dimension(500, 190));
        filaPedidosDb.add(scrollPedidosDb, BorderLayout.CENTER);
        filaPedidosDb.add(recargarPedidosButton, BorderLayout.SOUTH);

        JButton agregarButton = new JButton("Agregar al pedido 🛒");
        JButton eliminarButton = new JButton("Eliminar seleccionado 🗑️");
        JButton vaciarButton = new JButton("Vaciar pedido 🧹");

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaBotones.setBackground(new Color(250, 248, 255));
        filaBotones.add(agregarButton);
        filaBotones.add(eliminarButton);
        filaBotones.add(vaciarButton);

        JPanel filaDescuento = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaDescuento.setBackground(new Color(250, 248, 255));
        filaDescuento.add(new JLabel("Descuento:"));
        filaDescuento.add(descuentosCombo);

        mensajeLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        mensajeLabel.setForeground(new Color(95, 55, 135));

        panelFormulario.add(filaProducto);
        panelFormulario.add(filaPedidosDb);
        panelFormulario.add(filaBotones);
        panelFormulario.add(filaDescuento);
        panelFormulario.add(mensajeLabel);

        root.add(panelFormulario, BorderLayout.WEST);
        root.add(new JScrollPane(tablaPedido), BorderLayout.CENTER);

        JPanel panelTotales = new JPanel(new GridLayout(1, 3, 10, 10));
        panelTotales.setBackground(new Color(245, 241, 255));
        subtotalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalLabel.setForeground(new Color(40, 130, 70));

        JButton confirmarButton = new JButton("Confirmar pedido y guardar en DB ✅");
        panelTotales.add(subtotalLabel);
        panelTotales.add(totalLabel);
        panelTotales.add(confirmarButton);

        root.add(panelTotales, BorderLayout.SOUTH);
        setContentPane(root);

        agregarButton.addActionListener(e -> agregarProducto());
        eliminarButton.addActionListener(e -> eliminarSeleccionado());
        vaciarButton.addActionListener(e -> vaciarPedido());
        confirmarButton.addActionListener(e -> confirmarPedido());
        recargarPedidosButton.addActionListener(e -> recargarPedidosDesdeDb());
    }

    private void registrarEventos() {
        descuentosCombo.addActionListener(e -> actualizarTablaYTotales());
    }

    private DescuentoStrategy[] crearDescuentos() {
        return new DescuentoStrategy[]{
                new SinDescuento(),
                new DescuentoEstudiante(),
                new DescuentoHechizo()
        };
    }

    private void agregarProducto() {
        try {
            ProductoDTO productoSeleccionado = (ProductoDTO) productosCombo.getSelectedItem();
            int cantidad = Integer.parseInt(cantidadField.getText());

            if (productoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto.");
                return;
            }

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
                return;
            }

            controller.agregarProducto(productoSeleccionado, cantidad);

            mensajeLabel.setText("Producto agregado: " + productoSeleccionado.getNombre());
            actualizarTablaYTotales();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa una cantidad numerica valida.");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void eliminarSeleccionado() {
        int fila = tablaPedido.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.");
            return;
        }

        controller.eliminarItem(fila);
        mensajeLabel.setText("Item eliminado del pedido.");
        actualizarTablaYTotales();
    }

    private void vaciarPedido() {
        controller.vaciarPedido();
        mensajeLabel.setText("Pedido vacio. La magia empezo de nuevo.");
        actualizarTablaYTotales();
    }

    private void confirmarPedido() {
        if (controller.pedidoEstaVacio()) {
            JOptionPane.showMessageDialog(this, "No podes confirmar un pedido vacio.");
            return;
        }

        DescuentoStrategy descuento = (DescuentoStrategy) descuentosCombo.getSelectedItem();
        controller.seleccionarDescuento(descuento);

        try {
            PedidoConfirmadoDTO pedidoConfirmado = controller.confirmarPedido();

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido guardado en DB 🎉\n"
                            + "ID generado: " + pedidoConfirmado.getPedidoId() + "\n"
                            + "Subtotal: $" + String.format("%.2f", pedidoConfirmado.getSubtotal()) + "\n"
                            + "Descuento aplicado: " + pedidoConfirmado.getDescuentoAplicado() + "\n"
                            + "Total final: $" + String.format("%.2f", pedidoConfirmado.getTotal()),
                    "Ticket magico",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mensajeLabel.setText("Pedido guardado con ID " + pedidoConfirmado.getPedidoId());
            actualizarTablaYTotales();
            actualizarTablaPedidosDb();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaPedidosDb() {
        pedidosDbTableModel.setRowCount(0);

        for (PedidoListadoDTO pedido : controller.obtenerPedidosCreados()) {
            pedidosDbTableModel.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getSubtotal(),
                    pedido.getDescuentoAplicado(),
                    pedido.getTotal(),
                    pedido.getFechaCreacion()
            });
        }
    }

    private void recargarPedidosDesdeDb() {
        try {
            actualizarTablaPedidosDb();
            mensajeLabel.setText("Pedidos creados recargados desde la base de datos.");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaYTotales() {
        DescuentoStrategy descuento = (DescuentoStrategy) descuentosCombo.getSelectedItem();
        controller.seleccionarDescuento(descuento);

        tableModel.setRowCount(0);

        for (ItemPedidoDTO item : controller.obtenerItemsPedido()) {
            tableModel.addRow(new Object[]{
                    item.getProducto(),
                    item.getCantidad(),
                    item.getPrecioUnitario(),
                    item.getSubtotal()
            });
        }

        subtotalLabel.setText("Subtotal: $" + String.format("%.2f", controller.calcularSubtotal()));
        totalLabel.setText("Total: $" + String.format("%.2f", controller.calcularTotal()));
    }
}
