package modelo;

public class LineaPedido {
    private Producto producto;
    private int cantidad;

    public LineaPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // Getters
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }

    // Calcular subtotal de esta línea
    public double calcularSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    @Override
    public String toString() {
        return String.format("- %s (ID %d) x %d unidades @ $%.2f c/u | Subtotal: $%.2f",
                producto.getNombre(), producto.getId(), cantidad,
                producto.getPrecio(), calcularSubtotal());
    }
}