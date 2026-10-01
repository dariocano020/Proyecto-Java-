package inventario.modelo;

import inventario.utilidades.*;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

public class Carrito implements Serializable {
    private Map<Producto, Integer> productos;

    public Carrito() {
        this.productos = new HashMap<>();
    }

    public void anadirProducto(Producto p, int cantidad) {
        productos.put(p, productos.getOrDefault(p, 0) + cantidad);
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
        for (Map.Entry<Producto, Integer> entry : productos.entrySet()) {
            total += entry.getKey().getPrecio() * entry.getValue();
        }
        return total;
    }
}




