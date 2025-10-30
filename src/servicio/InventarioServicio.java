package servicio;

import modelo.Producto;
import excepciones.ProductoNoEncontradoException;
import java.util.ArrayList;
import java.util.List;

public class InventarioServicio {
    private final List<Producto> productos; // Colección principal

    public InventarioServicio() {
        this.productos = new ArrayList<>();
        // Productos iniciales para prueba
        productos.add(new Producto("Café Premium", 15.50, 50));
        productos.add(new Producto("Té Verde", 8.75, 100));
        productos.add(new Producto("Tostadora", 45.99, 10));
    }

    public void agregarProducto(String nombre, double precio, int stock) {
        if (precio <= 0 || stock < 0) {
            System.err.println("Error: El precio debe ser positivo y el stock no negativo.");
            return;
        }
        productos.add(new Producto(nombre, precio, stock));
        System.out.println("Producto agregado exitosamente.");
    }

    public List<Producto> listarProductos() {
        return productos;
    }

    // Polimorfismo por sobrecarga: Buscar por ID (int)
    public Producto buscarProducto(int id) throws ProductoNoEncontradoException {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new ProductoNoEncontradoException("Producto con ID " + id + " no encontrado.");
    }

    // Polimorfismo por sobrecarga: Buscar por nombre (String)
    public Producto buscarProducto(String nombre) throws ProductoNoEncontradoException {
        return productos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto con nombre '" + nombre + "' no encontrado."));
    }

    public void actualizarProducto(int id, double nuevoPrecio, int nuevoStock) throws ProductoNoEncontradoException {
        Producto p = buscarProducto(id);

        if (nuevoPrecio > 0) {
            p.setPrecio(nuevoPrecio);
        } else {
            System.err.println("Advertencia: Precio no actualizado (debe ser > 0).");
        }

        if (nuevoStock >= 0) {
            // Ajustar el stock a la nueva cantidad
            int cambio = nuevoStock - p.getStock();
            p.ajustarStock(cambio);
        } else {
            System.err.println("Advertencia: Stock no actualizado (no puede ser negativo).");
        }
        System.out.println("Producto actualizado: " + p);
    }

    public void eliminarProducto(int id) throws ProductoNoEncontradoException {
        boolean eliminado = productos.removeIf(p -> p.getId() == id);

        if (!eliminado) {
            throw new ProductoNoEncontradoException("No se pudo eliminar. Producto con ID " + id + " no encontrado.");
        }
        System.out.println("Producto eliminado con éxito.");
    }
}