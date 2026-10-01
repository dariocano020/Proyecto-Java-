package inventario.modelo;

import inventario.utilidades.*;

import java.io.Serializable;

public class ProductoDigital extends Producto implements Serializable {
    private double gigas;

    public ProductoDigital(String id, String nombre, double precio, int stock, double gigas) {
        super(id, nombre, precio, stock);
        this.gigas = gigas;
    }

    @Override
    public String obtenerDetalles() {
        return " [Digital] " + getNombre() + " (Descarga: " + gigas + "GB) - " + getPrecio() + "";
    }
}




