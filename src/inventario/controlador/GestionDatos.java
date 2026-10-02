package inventario.controlador;

import inventario.utilidades.*;

import inventario.modelo.*;
import inventario.vista.*;
import inventario.modelo.Pedido;
import inventario.modelo.Producto;
import inventario.modelo.Usuario;
import inventario.modelo.UsuarioAdmin;
import inventario.modelo.ProductoFisico;
import inventario.modelo.ProductoDigital;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.io.Serializable;

public class GestionDatos implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Usuario> usuarios;
    private Map<String, Producto> productos;
    private List<Pedido> historialPedidos;

    public GestionDatos() {
        usuarios = new HashMap<>();
        productos = new HashMap<>();
        historialPedidos = new ArrayList<>();

    }



    public boolean registrarUsuario(Usuario u) {
        if (usuarios.containsKey(u.getUsername())) return false;
        usuarios.put(u.getUsername(), u);
        return true;
    }

    public Usuario autenticar(String username, String password) {
        Usuario u = usuarios.get(username);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }

    public boolean registrarProducto(Producto p) {
        if (productos.containsKey(p.getId())) return false;
        productos.put(p.getId(), p);
        return true;
    }

    public boolean eliminarProducto(String id) {
        return productos.remove(id) != null;
    }

    public Producto buscarProducto(String id) {
        return productos.get(id);
    }

    public List<Producto> listarProductos() {
        return new ArrayList<>(productos.values());
    }

    public void agregarPedido(Pedido p) {
        historialPedidos.add(p);
    }

    public List<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }

    public String obtenerTopUsuario() {
        return historialPedidos.stream()
                .collect(Collectors.groupingBy(Pedido::getUsuario, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().getNombre() + " (" + e.getValue() + " pedidos)")
                .orElse("N/A");
    }

    public double obtenerTicketMedio() {
        return historialPedidos.stream()
                .mapToDouble(Pedido::getTotal)
                .average()
                .orElse(0.0);
    }
}




