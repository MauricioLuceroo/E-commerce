package modelo;

public class Producto {
    private static int contadorId = 1;
    private final int id;
    private String nombre;
    private double precio;
    private int stock;

    // Constructor
    public Producto(String nombre, double precio, int stock) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }

    // Método para manejar el stock (positivo para sumar, negativo para restar)
    public void ajustarStock(int cambio) {
        this.stock += cambio;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Nombre: %s | Precio: $%.2f | Stock: %d",
                id, nombre, precio, stock);
    }
}