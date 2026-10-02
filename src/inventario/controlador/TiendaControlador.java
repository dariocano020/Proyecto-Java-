package inventario.controlador;

import inventario.utilidades.*;

import inventario.modelo.*;
import inventario.vista.*;
import inventario.modelo.*;
import inventario.utilidades.ExportadorCSV;
import inventario.utilidades.ServicioOTP;
import inventario.vista.ConsolaVista;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TiendaControlador {
    private ConsolaVista vista;
    private GestionDatos gestionDatos;
    private Usuario usuarioLogueado;
    private Carrito carritoActual;

    public TiendaControlador(ConsolaVista vista, GestionDatos gestionDatos) {
        this.vista = vista;
        this.gestionDatos = gestionDatos;
    }

    public void iniciar() {
        vista.mostrarBanner();
        boolean salir = false;
        while (!salir) {
            vista.mostrarMenuLogin();
            String op = vista.pedirTexto("Elige una opcion: ");
            
            switch (op) {
                case "1":
                    login();
                    break;
                case "2":
                    registro();
                    break;
                case "3":
                    salir = true;
                    inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
vista.mostrarExito("Gracias por usar el sistema! Hasta pronto.");
                    break;
                default:
                    vista.mostrarError("Opcion no valida.");
            }
        }
    }

    private void login() {
        String username = vista.pedirTexto("Usuario: ");
        String pass = vista.pedirTexto("Contrasena: ");
        
        Usuario u = gestionDatos.autenticar(username, pass);
        if (u != null) {
            int otp = ServicioOTP.generarOTP();
            ServicioOTP.enviarCorreoOTP(u.getCorreo(), otp);
            
            int otpUsuario = vista.pedirEntero("Introducodigo OTP que hemos enviado a tu correo: ");
            if (otpUsuario == otp) {
                usuarioLogueado = u;
                carritoActual = new Carrito();
vista.mostrarExito("Autenticacion exitosa! Bienvenido " + u.getNombre());
                
                if (u.esAdmin()) {
                    menuAdmin();
                } else {
                    menuCliente();
                }
            } else {
                vista.mostrarError("codigo OTP incorrecto. Acceso denegado.");
            }
        } else {
            vista.mostrarError("Correo o Contrasena incorrectos.");
        }
    }

    private void registro() {
        vista.mostrarMensaje("--- REGISTRO DE NUEVO USUARIO ---");
        String username = vista.pedirTexto("Nombre de Usuario (Login): ");
        String correo = vista.pedirTexto("Correo para OTP: ");
        String pass = vista.pedirTexto("Contrasena: ");
        String nombre = vista.pedirTexto("Nombre real: ");
        String rol = vista.pedirTexto("Sera administrador? (S/N): ");
        
        Usuario nuevo;
        if (rol.equalsIgnoreCase("S")) {
            nuevo = new UsuarioAdmin(username, correo, pass, nombre);
        } else {
            nuevo = new Usuario(username, correo, pass, nombre);
        }
        
        if (gestionDatos.registrarUsuario(nuevo)) {
vista.mostrarExito("Usuario registrado correctamente. Ya puedes iniciar sesion.");
            inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
        } else {
            vista.mostrarError("Ese usuario ya existe.");
        }
    }

    private void menuAdmin() {
        boolean salir = false;
        while (!salir) {
            vista.mostrarMenuAdmin(usuarioLogueado.getNombre());
            String op = vista.pedirTexto("Elige opcion: ");
            
            switch (op) {
                case "1":
                    altaProducto();
                    break;
                case "2":
                    bajaProducto();
                    break;
                case "3":
                    listarProductos();
                    break;
                case "4":
                    mostrarESTADISTICAS();
                    break;
                case "5":
                    ExportadorCSV.exportar(gestionDatos.getHistorialPedidos(), "pedidos.csv");
vista.mostrarExito("Historial exportado a pedidos.csv");
                    break;
                case "6":
                    salir = true;
                    usuarioLogueado = null;
                    inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
                    vista.mostrarMensaje("sesion cerrada.");
                    break;
                default:
                    vista.mostrarError("Opcion no valida.");
            }
        }
    }

    private void menuCliente() {
        boolean salir = false;
        while (!salir) {
            vista.mostrarMenuUsuario(usuarioLogueado.getNombre());
            String op = vista.pedirTexto("Elige opcion: ");
            
            switch (op) {
                case "1":
                    listarProductos();
                    break;
                case "2":
                    anadirCarrito();
                    break;
                case "3":
                    quitarCarrito();
                    break;
                case "4":
                    verCarrito();
                    break;
                case "5":
                    cerrarPedido();
                    break;
                case "6":
                    salir = true;
                    usuarioLogueado = null;
                    inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
                    vista.mostrarMensaje("sesion cerrada.");
                    break;
                default:
                    vista.mostrarError("Opcion no valida.");
            }
        }
    }

    private void altaProducto() {
        String tipo = vista.pedirTexto("Tipo de producto (Fisico, D = Digital): ");
        int maxId = 0;
        for (Producto prod : gestionDatos.listarProductos()) {
            if (prod.getId().startsWith("P")) {
                try {
                    int num = Integer.parseInt(prod.getId().substring(1));
                    if (num > maxId) maxId = num;
                } catch(Exception e) {}
            }
        }
        String id = "P" + (maxId + 1);
        String nombre = vista.pedirTexto("Nombre: ");
        double precio = vista.pedirDouble("Precio: ");
        int stock = vista.pedirEntero("Stock: ");
        
        Producto p;
        if (tipo.equalsIgnoreCase("F")) {
            double peso = vista.pedirDouble("Peso (kg): ");
            p = new ProductoFisico(id, nombre, precio, stock, peso);
        } else {
            double gigas = vista.pedirDouble("Tamano de descarga (GB): ");
            p = new ProductoDigital(id, nombre, precio, stock, gigas);
        }
        
        if (gestionDatos.registrarProducto(p)) {
            vista.mostrarExito("Producto anadido al CATALOGO.");
            inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
        } else {
            vista.mostrarError("Ya existe un producto con ese ID.");
        }
    }

    private void bajaProducto() {
        String id = vista.pedirTexto("ID del producto a dar de baja: ");
        if (gestionDatos.eliminarProducto(id)) {
vista.mostrarExito("Producto eliminado.");
            inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
        } else {
            vista.mostrarError("No se encontro el producto.");}
    }

    private void listarProductos() {
        List<Producto> lista = gestionDatos.listarProductos();
        if (lista.isEmpty()) {
            vista.mostrarMensaje("El CATALOGO esta vacio.");
        } else {
            vista.mostrarMensaje("--- CATALOGO ---");
            for (Producto p : lista) {
                vista.mostrarMensaje(p.getId() + " | " + p.obtenerDetalles() + " | Stock: " + p.getStock());
            }
        }
    }

    private void mostrarESTADISTICAS() {
        vista.mostrarMensaje("--- ESTADISTICAS ---");
        vista.mostrarMensaje("Ticket Medio: " + String.format("%.2f", gestionDatos.obtenerTicketMedio()) + "");
        vista.mostrarMensaje("Usuario Top: " + gestionDatos.obtenerTopUsuario());
    }

    private void anadirCarrito() {
        String id = vista.pedirTexto("ID del producto: ");
        Producto p = gestionDatos.buscarProducto(id);
        if (p == null) {
            vista.mostrarError("Producto no encontrado.");return;
        }
        int cant = vista.pedirEntero("Cantidad: ");
        if (cant > p.getStock()) {
            vista.mostrarError("No hay suficiente stock. Disponible: " + p.getStock());
            return;
        }
        carritoActual.anadirProducto(p, cant);
        vista.mostrarExito("Producto anadido al carrito.");
    }

    private void quitarCarrito() {
        String id = vista.pedirTexto("ID del producto: ");
        Producto p = gestionDatos.buscarProducto(id);
        if (p != null) {
            carritoActual.quitarProducto(p);
vista.mostrarExito("Producto retirado del carrito.");
        }
    }

    private void verCarrito() {
        Map<Producto, Integer> cont = carritoActual.getProductos();
        if (cont.isEmpty()) {
            vista.mostrarMensaje("Tu carrito esta vacio.");
            return;
        }
        vista.mostrarMensaje("--- TU CARRITO ---");
        cont.forEach((p, cant) -> {
            vista.mostrarMensaje(cant + "x " + p.getNombre() + " (" + p.getPrecio() + "/ud)");
        });
        vista.mostrarMensaje("TOTAL: " + carritoActual.getTotal() + "");
    }

    private void cerrarPedido() {
        Map<Producto, Integer> cont = carritoActual.getProductos();
        if (cont.isEmpty()) {
            vista.mostrarError("El carrito esta vacio, no se puede comprar.");
            return;
        }
        
        List<LineaPedido> lineas = new ArrayList<>();
        boolean errorStock = false;
        for (Map.Entry<Producto, Integer> entrada : cont.entrySet()) {
            Producto p = entrada.getKey();
            int cant = entrada.getValue();
            if (p.getStock() < cant) {
                vista.mostrarError("Stock insuficiente para " + p.getNombre() + " durante el pago.");
                errorStock = true;
                break;
            }
        }
        
        if (!errorStock) {
            for (Map.Entry<Producto, Integer> entrada : cont.entrySet()) {
                Producto p = entrada.getKey();
                int cant = entrada.getValue();
                p.setStock(p.getStock() - cant);
                lineas.add(new LineaPedido(p, cant));
            }
            
            String idGen = "PED-" + UUID.randomUUID().toString().substring(0,6).toUpperCase();
            Pedido nuevoPedido = new Pedido(idGen, usuarioLogueado, lineas);
            gestionDatos.agregarPedido(nuevoPedido);
            
            carritoActual.vaciar();
vista.mostrarExito("Compra realizada con exito! ID Pedido: " + idGen);
            nuevoPedido.imprimirFactura();
            inventario.utilidades.ServicioOTP.enviarFactura(nuevoPedido, usuarioLogueado.getCorreo());
            inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
        }
    }
}




