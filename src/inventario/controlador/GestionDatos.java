package inventario.controlador;

import inventario.utilidades.*;
import inventario.modelo.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        if (historialPedidos.isEmpty()) return "N/A";
        
        Map<Usuario, Integer> conteo = new HashMap<>();
        for (Pedido p : historialPedidos) {
            Usuario u = p.getUsuario();
            int contadorActual = 0;
            if (conteo.containsKey(u)) {
                contadorActual = conteo.get(u);
            }
            conteo.put(u, contadorActual + 1);
        }
        
        Usuario topUsuario = null;
        int maxPedidos = 0;
        for (Usuario u : conteo.keySet()) {
            int pedidos = conteo.get(u);
            if (pedidos > maxPedidos) {
                maxPedidos = pedidos;
                topUsuario = u;
            }
        }
        
        if (topUsuario == null) return "N/A";
        return topUsuario.getNombre() + " (" + maxPedidos + " pedidos)";
    }

    public double obtenerTicketMedio() {
        if (historialPedidos.isEmpty()) return 0.0;
        double suma = 0;
        for (Pedido p : historialPedidos) {
            suma = suma + p.getTotal();
        }
        return suma / historialPedidos.size();
    }
}