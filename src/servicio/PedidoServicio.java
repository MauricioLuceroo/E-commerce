package servicio;

import modelo.Producto;
import modelo.Pedido;
import excepciones.StockInsuficienteException;
import excepciones.ProductoNoEncontradoException;
import java.util.ArrayList;
import java.util.List;

public class PedidoServicio {
    private final List<Pedido> pedidos; // Colección de pedidos
    private final InventarioServicio inventario; // Dependencia para stock

    public PedidoServicio(InventarioServicio inventario) {
        this.pedidos = new ArrayList<>();
        this.inventario = inventario;
    }

    // itemsPedido es una lista de strings con formato "ID:Cantidad"
    public Pedido crearPedido(List<String> itemsPedido) throws StockInsuficienteException, ProductoNoEncontradoException {
        Pedido nuevoPedido = new Pedido();

        // 1. Pre-verificación y construcción del pedido
        for (String item : itemsPedido) {
            String[] partes = item.split(":");
            if (partes.length != 2) continue;

            try {
                int idProducto = Integer.parseInt(partes[0].trim());
                int cantidad = Integer.parseInt(partes[1].trim());

                Producto producto = inventario.buscarProducto(idProducto);

                if (cantidad <= 0) {
                    throw new IllegalArgumentException("La cantidad debe ser positiva.");
                }

                // Validación de stock (Lanza StockInsuficienteException)
                if (producto.getStock() < cantidad) {
                    throw new StockInsuficienteException("Stock insuficiente para " + producto.getNombre()
                            + ". Disponible: " + producto.getStock() + ", Solicitado: " + cantidad);
                }

                nuevoPedido.agregarLinea(producto, cantidad);
            } catch (NumberFormatException e) {
                // Captura error si el ID o Cantidad no son números
                throw new ProductoNoEncontradoException("Error de formato: ID o Cantidad no válidos en la línea: " + item);
            }
        }

        // 2. Si no hubo excepciones, se confirma el pedido y se ajusta el inventario
        for (modelo.LineaPedido linea : nuevoPedido.getLineas()) {
            // Disminuir el stock (usamos un número negativo en ajustarStock)
            linea.getProducto().ajustarStock(-linea.getCantidad());
        }

        pedidos.add(nuevoPedido);
        return nuevoPedido;
    }

    public List<Pedido> listarPedidos() {
        return pedidos;
    }
}
