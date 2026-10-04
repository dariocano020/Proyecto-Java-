package inventario.modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Carrito implements Serializable {
    private Map<Producto, Integer> productos;

    public Carrito() {
        this.productos = new HashMap<>();
    }

    public void anadirProducto(Producto p, int cantidad) {
        int actual = 0;
        if (productos.containsKey(p)) {
            actual = productos.get(p);
        }
        productos.put(p, actual + cantidad);
    }

    public void quitarProducto(Producto p) {
        productos.remove(p);
    }

    public void vaciar() {
        productos.clear();
    }

    public Map<Producto, Integer> getProductos() {
        return productos;
    }

    public double getTotal() {
        double total = 0;
        for (Producto p : productos.keySet()) {
            double precio = p.getPrecio();
            int cantidad = productos.get(p);
            total = total + (precio * cantidad);
        }
        return total;
    }
}