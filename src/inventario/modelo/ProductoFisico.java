package inventario.modelo;

import inventario.utilidades.*;

import java.io.Serializable;

public class ProductoFisico extends Producto implements Serializable {
    private double peso;

    public ProductoFisico(String id, String nombre, double precio, int stock, double peso) {
        super(id, nombre, precio, stock);
        this.peso = peso;
    }

    @Override
    public String obtenerDetalles() {
        return " [Fisico] " + getNombre() + " (Peso: " + peso + "kg) - " + getPrecio() + "";
    }
}




