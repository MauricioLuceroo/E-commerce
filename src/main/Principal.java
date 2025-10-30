package main;

import java.util.InputMismatchException;
import java.util.Scanner;
import servicio.InventarioServicio;
import servicio.PedidoServicio;
import modelo.Producto;
import excepciones.ProductoNoEncontradoException;
import excepciones.StockInsuficienteException;
import java.util.Arrays;
import java.util.List;

public class Principal {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InventarioServicio inventario = new InventarioServicio();
    private static final PedidoServicio pedidos;

    // Inicializa PedidoServicio con la instancia de InventarioServicio
    static {
        pedidos = new PedidoServicio(inventario);
    }

    public static void main(String[] args) {
        int opcion = 0;

        System.out.println("Sistema de Gestión de Inventario y Pedidos");

        do {
            mostrarMenu();
            try {
                System.out.print("Seleccione una opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1: agregarProducto(); break;
                    case 2: listarProductos(); break;
                    case 3: buscarYActualizarProducto(); break;
                    case 4: eliminarProducto(); break;
                    case 5: crearPedido(); break;
                    case 6: listarPedidos(); break;
                    case 7: System.out.println("¡Gracias por usar el sistema! Saliendo..."); break;
                    default: System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                // Manejo de excepción de entrada no numérica (Robustez)
                System.err.println("¡ERROR! Ingrese solo números para las opciones.");
                scanner.nextLine();
                opcion = 0;
            } catch (Exception e) {
                System.err.println("Ha ocurrido un error inesperado: " + e.getMessage());
            }
            System.out.println("\nPresione ENTER para continuar...");
            scanner.nextLine();
        } while (opcion != 7);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Agregar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar/Actualizar producto");
        System.out.println("4. Eliminar producto");
        System.out.println("5. Crear un pedido");
        System.out.println("6. Listar pedidos");
        System.out.println("7. Salir");
        System.out.println("==========================");
    }

    // --- Métodos de Funcionalidad ---

    private static void agregarProducto() {
        System.out.println("\n--- AGREGAR PRODUCTO ---");
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Precio (Ej: 15.50): ");
            double precio = scanner.nextDouble();
            System.out.print("Stock inicial: ");
            int stock = scanner.nextInt();
            scanner.nextLine();

            inventario.agregarProducto(nombre, precio, stock);
        } catch (InputMismatchException e) {
            System.err.println("¡ERROR! Entrada inválida. Asegúrese de usar números para precio y stock.");
            scanner.nextLine();
        }
    }

    private static void listarProductos() {
        System.out.println("\n--- LISTA DE PRODUCTOS ---");
        List<Producto> lista = inventario.listarProductos();
        if (lista.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private static void buscarYActualizarProducto() {
        System.out.println("\n--- BUSCAR/ACTUALIZAR PRODUCTO ---");
        System.out.print("Ingrese ID o Nombre del producto: ");
        String busqueda = scanner.nextLine();

        try {
            Producto p;
            try {
                // Intenta buscar por ID
                int id = Integer.parseInt(busqueda);
                p = inventario.buscarProducto(id);
            } catch (NumberFormatException e) {
                // Si falla, busca por Nombre
                p = inventario.buscarProducto(busqueda);
            }

            System.out.println("\nProducto Encontrado:");
            System.out.println(p);

            System.out.print("\n¿Desea actualizar (S/N)? ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                System.out.print("Nuevo Precio (actual: " + p.getPrecio() + ") -> ");
                String nuevoPrecioStr = scanner.nextLine();
                System.out.print("Nuevo Stock (actual: " + p.getStock() + ") -> ");
                String nuevoStockStr = scanner.nextLine();

                double nuevoPrecio = nuevoPrecioStr.isEmpty() ? p.getPrecio() : Double.parseDouble(nuevoPrecioStr);
                int nuevoStock = nuevoStockStr.isEmpty() ? p.getStock() : Integer.parseInt(nuevoStockStr);

                inventario.actualizarProducto(p.getId(), nuevoPrecio, nuevoStock);
            }

        } catch (ProductoNoEncontradoException | NumberFormatException e) {
            System.err.println("Error en la operación: " + e.getMessage());
        }
    }

    private static void eliminarProducto() {
        System.out.println("\n--- ELIMINAR PRODUCTO ---");
        try {
            System.out.print("Ingrese el ID del producto a eliminar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("¿Confirma la eliminación del producto ID " + id + " (S/N)? ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                inventario.eliminarProducto(id);
            } else {
                System.out.println("Eliminación cancelada.");
            }

        } catch (InputMismatchException e) {
            System.err.println("¡ERROR! Ingrese solo números para el ID.");
            scanner.nextLine();
        } catch (ProductoNoEncontradoException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void crearPedido() {
        System.out.println("\n--- CREAR PEDIDO ---");
        System.out.println("Productos disponibles (ID | Nombre | Stock):");
        inventario.listarProductos().forEach(p -> System.out.printf("  %d | %s | Stock: %d\n", p.getId(), p.getNombre(), p.getStock()));

        System.out.println("\nIngrese los productos y cantidades separados por coma (Ej: ID:Cant, ID:Cant):");
        System.out.print("Su pedido: ");
        String entradaPedido = scanner.nextLine();

        List<String> itemsPedido = Arrays.asList(entradaPedido.split(","));

        try {
            System.out.print("¿Confirma el pedido (S/N)? ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                pedidos.crearPedido(itemsPedido);
                System.out.println("\n¡PEDIDO CREADO Y STOCK ACTUALIZADO CON ÉXITO!");
                // Mostrar el último pedido
                if (!pedidos.listarPedidos().isEmpty()) {
                    System.out.println(pedidos.listarPedidos().get(pedidos.listarPedidos().size() - 1));
                }
            } else {
                System.out.println("Creación de pedido cancelada.");
            }
        } catch (StockInsuficienteException e) {
            System.err.println("¡ERROR EN EL PEDIDO! " + e.getMessage());
        } catch (ProductoNoEncontradoException e) {
            System.err.println("¡ERROR EN EL PEDIDO! " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error general al procesar el pedido: " + e.getMessage());
        }
    }

    private static void listarPedidos() {
        System.out.println("\n--- LISTA DE PEDIDOS REALIZADOS ---");
        List<modelo.Pedido> listaPedidos = pedidos.listarPedidos();
        if (listaPedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        for (modelo.Pedido p : listaPedidos) {
            System.out.println(p);
        }
    }
}