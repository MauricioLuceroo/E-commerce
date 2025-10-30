package modelo;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private static int contadorId = 1;
    private final int id;
    private List<LineaPedido> lineas; // Colección de LineaPedido
    private double costoTotal;

    public Pedido() {
        this.id = contadorId++;
        this.lineas = new ArrayList<>();
        this.costoTotal = 0.0;
    }

    // Agregar una línea al pedido y actualizar el costo total
    public void agregarLinea(Producto producto, int cantidad) {
        LineaPedido linea = new LineaPedido(producto, cantidad);
        this.lineas.add(linea);
        this.costoTotal += linea.calcularSubtotal();
    }

    // Getters
    public int getId() { return id; }
    public List<LineaPedido> getLineas() { return lineas; }
    public double getCostoTotal() { return costoTotal; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("PEDIDO #%d | COSTO TOTAL: $%.2f\n", id, costoTotal));
        sb.append("Detalle:\n");
        for (LineaPedido linea : lineas) {
            sb.append(linea.toString()).append("\n");
        }
        sb.append("--------------------------------------------------");
        return sb.toString();
    }
}
