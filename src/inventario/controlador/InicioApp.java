package inventario.controlador;

import inventario.utilidades.*;
import inventario.modelo.*;
import inventario.vista.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InicioApp {
    private ConsolaVista vista;
    private GestionDatos gestionDatos;
    private Usuario usuarioLogueado;
    private Carrito carritoActual;

    public InicioApp(ConsolaVista vista, GestionDatos gestionDatos) {
        this.vista = vista;
        this.gestionDatos = gestionDatos;
    }

    public void iniciar() {
        vista.mostrarBanner();
        boolean salirMenuPrincipal = false;

        while (!salirMenuPrincipal) {
            vista.mostrarMenuLogin();
            String op = vista.pedirTexto("Elige una opcion: ");

            switch (op) {
                case "1":
                    // ------------------ LOGIN ------------------
                    String username = vista.pedirTexto("Usuario: ");
                    String pass = vista.pedirTexto("Contrasena: ");
                    
                    Usuario u = gestionDatos.autenticar(username, pass);
                    if (u != null) {
                        int otp = ServicioOTP.generarOTP();
                        vista.mostrarCargando("Enviando correo OTP...");
                        try {
                            ServicioOTP.enviarCorreoOTP(u.getCorreo(), otp);
                            vista.mostrarExito("Correo enviado.");
                        } catch(Exception e) {
                            vista.mostrarError("Error enviando correo OTP: " + e.getMessage());
                        }
                        
                        int otpUsuario = vista.pedirEntero("Introduce el codigo OTP que hemos enviado a tu correo: ");
                        if (otpUsuario == otp) {
                            usuarioLogueado = u;
                            carritoActual = new Carrito();
                            vista.mostrarExito("Autenticacion exitosa! Bienvenido " + u.getNombre());
                            
                            // SUBMENU DEPENDE DEL ROL
                            if (u.esAdmin()) {
                                // ------------------ MENU ADMIN ------------------
                                boolean salirAdmin = false;
                                while (!salirAdmin) {
                                    vista.mostrarMenuAdmin(usuarioLogueado.getNombre());
                                    String opAdmin = vista.pedirTexto("Elige opcion: ");
                                    
                                    switch (opAdmin) {
                                        case "1":
                                            // ALTA PRODUCTO
                                            String tipo = vista.pedirTexto("Tipo de producto (F = Fisico, D = Digital): ");
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
                                            String nombreProd = vista.pedirTexto("Nombre: ");
                                            double precio = vista.pedirDouble("Precio: ");
                                            int stock = vista.pedirEntero("Stock: ");
                                            
                                            Producto p;
                                            if (tipo.equalsIgnoreCase("F")) {
                                                double peso = vista.pedirDouble("Peso (kg): ");
                                                p = new ProductoFisico(id, nombreProd, precio, stock, peso);
                                            } else {
                                                double gigas = vista.pedirDouble("Tamano de descarga (GB): ");
                                                p = new ProductoDigital(id, nombreProd, precio, stock, gigas);
                                            }
                                            
                                            if (gestionDatos.registrarProducto(p)) {
                                                vista.mostrarExito("Producto anadido al CATALOGO.");
                                                try {
                                                    inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
                                                } catch(Exception e) {
                                                    vista.mostrarError("Error al guardar: " + e.getMessage());
                                                }
                                            } else {
                                                vista.mostrarError("Ya existe un producto con ese ID.");
                                            }
                                            break;
                                        case "2":
                                            // BAJA PRODUCTO
                                            String idBaja = vista.pedirTexto("ID del producto a dar de baja: ");
                                            if (gestionDatos.eliminarProducto(idBaja)) {
                                                vista.mostrarExito("Producto eliminado.");
                                                try {
                                                    inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
                                                } catch(Exception e) {
                                                    vista.mostrarError("Error al guardar: " + e.getMessage());
                                                }
                                            } else {
                                                vista.mostrarError("No se encontro el producto.");
                                            }
                                            break;
                                        case "3":
                                            // LISTAR PRODUCTOS
                                            List<Producto> lista = gestionDatos.listarProductos();
                                            if (lista.isEmpty()) {
                                                vista.mostrarMensaje("El CATALOGO esta vacio.");
                                            } else {
                                                vista.mostrarMensaje("--- CATALOGO ---");
                                                for (Producto prod : lista) {
                                                    vista.mostrarMensaje(prod.getId() + " | " + prod.obtenerDetalles() + " | Stock: " + prod.getStock());
                                                }
                                            }
                                            break;
                                        case "4":
                                            // ESTADISTICAS
                                            vista.mostrarMensaje("--- ESTADISTICAS ---");
                                            vista.mostrarMensaje("Ticket Medio: " + Math.round(gestionDatos.obtenerTicketMedio() * 100.0) / 100.0 + " EUR");
                                            vista.mostrarMensaje("Usuario Top: " + gestionDatos.obtenerTopUsuario());
                                            break;
                                        case "5":
                                            // EXPORTAR CSV
                                            try {
                                                ExportadorCSV.exportar(gestionDatos.getHistorialPedidos(), "pedidos.csv");
                                                vista.mostrarExito("Historial exportado a pedidos.csv");
                                            } catch(Exception e) {
                                                vista.mostrarError("Error al exportar CSV: " + e.getMessage());
                                            }
                                            break;
                                        case "6":
                                            // CERRAR SESION
                                            salirAdmin = true;
                                            usuarioLogueado = null;
                                            try {
                                                inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
                                            } catch(Exception e) {}
                                            vista.mostrarMensaje("sesion cerrada.");
                                            break;
                                        default:
                                            vista.mostrarError("Opcion no valida.");
                                    }
                                }
                            } else {
                                // ------------------ MENU CLIENTE ------------------
                                boolean salirCliente = false;
                                while (!salirCliente) {
                                    vista.mostrarMenuUsuario(usuarioLogueado.getNombre());
                                    String opCliente = vista.pedirTexto("Elige opcion: ");
                                    
                                    switch (opCliente) {
                                        case "1":
                                            // LISTAR PRODUCTOS
                                            List<Producto> lista = gestionDatos.listarProductos();
                                            if (lista.isEmpty()) {
                                                vista.mostrarMensaje("El CATALOGO esta vacio.");
                                            } else {
                                                vista.mostrarMensaje("--- CATALOGO ---");
                                                for (Producto prod : lista) {
                                                    vista.mostrarMensaje(prod.getId() + " | " + prod.obtenerDetalles() + " | Stock: " + prod.getStock());
                                                }
                                            }
                                            break;
                                        case "2":
                                            // ANADIR CARRITO
                                            String idProd = vista.pedirTexto("ID del producto: ");
                                            Producto pEncontrado = gestionDatos.buscarProducto(idProd);
                                            if (pEncontrado == null) {
                                                vista.mostrarError("Producto no encontrado.");
                                                break;
                                            }
                                            int cant = vista.pedirEntero("Cantidad: ");
                                            if (cant > pEncontrado.getStock()) {
                                                vista.mostrarError("No hay suficiente stock. Disponible: " + pEncontrado.getStock());
                                                break;
                                            }
                                            carritoActual.anadirProducto(pEncontrado, cant);
                                            vista.mostrarExito("Producto anadido al carrito.");
                                            break;
                                        case "3":
                                            // QUITAR CARRITO
                                            String idQuitar = vista.pedirTexto("ID del producto: ");
                                            Producto pQuitar = gestionDatos.buscarProducto(idQuitar);
                                            if (pQuitar != null) {
                                                carritoActual.quitarProducto(pQuitar);
                                                vista.mostrarExito("Producto retirado del carrito.");
                                            }
                                            break;
                                        case "4":
                                            // VER CARRITO
                                            Map<Producto, Integer> cont = carritoActual.getProductos();
                                            if (cont.isEmpty()) {
                                                vista.mostrarMensaje("Tu carrito esta vacio.");
                                            } else {
                                                vista.mostrarMensaje("--- TU CARRITO ---");
                                                for (Producto prodCarrito : cont.keySet()) {
                                                    
                                                    int c = cont.get(prodCarrito);
                                                    vista.mostrarMensaje(c + "x " + prodCarrito.getNombre() + " (" + prodCarrito.getPrecio() + " EUR/ud)");
                                                }
                                                vista.mostrarMensaje("TOTAL: " + carritoActual.getTotal() + " EUR");
                                            }
                                            break;
                                        case "5":
                                            // CERRAR PEDIDO (PAGAR)
                                            Map<Producto, Integer> contPago = carritoActual.getProductos();
                                            if (contPago.isEmpty()) {
                                                vista.mostrarError("El carrito esta vacio, no se puede comprar.");
                                                break;
                                            }
                                            
                                            List<LineaPedido> lineas = new ArrayList<>();
                                            boolean errorStock = false;
                                            
                                            for (Producto prodPago : contPago.keySet()) {
                                                
                                                int cantPago = contPago.get(prodPago);
                                                if (prodPago.getStock() < cantPago) {
                                                    vista.mostrarError("Stock insuficiente para " + prodPago.getNombre() + " durante el pago.");
                                                    errorStock = true;
                                                    break;
                                                }
                                            }
                                            
                                            if (!errorStock) {
                                                for (Producto prodPago : contPago.keySet()) {
                                                    
                                                    int cantPago = contPago.get(prodPago);
                                                    prodPago.setStock(prodPago.getStock() - cantPago);
                                                    lineas.add(new LineaPedido(prodPago, cantPago));
                                                }
                                                
                                                String idGen = "PED-" + (int)(Math.random() * 1000000);
                                                Pedido nuevoPedido = new Pedido(idGen, usuarioLogueado, lineas);
                                                gestionDatos.agregarPedido(nuevoPedido);
                                                
                                                carritoActual.vaciar();
                                                vista.mostrarExito("Compra realizada con exito! ID Pedido: " + idGen);
                                                vista.mostrarFactura(nuevoPedido);
                                                
                                                vista.mostrarCargando("Enviando factura por correo...");
                                                try {
                                                    ServicioOTP.enviarFactura(nuevoPedido, usuarioLogueado.getCorreo());
                                                    vista.mostrarExito("Factura enviada al correo.");
                                                } catch(Exception e) {
                                                    vista.mostrarError("Error enviando factura: " + e.getMessage());
                                                }
                                                
                                                try {
                                                    inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
                                                } catch(Exception e) {}
                                            }
                                            break;
                                        case "6":
                                            // CERRAR SESION
                                            salirCliente = true;
                                            usuarioLogueado = null;
                                            try {
                                                inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
                                            } catch(Exception e) {}
                                            vista.mostrarMensaje("sesion cerrada.");
                                            break;
                                        default:
                                            vista.mostrarError("Opcion no valida.");
                                    }
                                }
                            }
                        } else {
                            vista.mostrarError("codigo OTP incorrecto. Acceso denegado.");
                        }
                    } else {
                        vista.mostrarError("Correo o Contrasena incorrectos.");
                    }
                    break;

                case "2":
                    // ------------------ REGISTRO ------------------
                    vista.mostrarMensaje("--- REGISTRO DE NUEVO USUARIO ---");
                    String user = vista.pedirTexto("Nombre de Usuario (Login): ");
                    String correo = vista.pedirTexto("Correo para OTP: ");
                    String passw = vista.pedirTexto("Contrasena: ");
                    String nombreReal = vista.pedirTexto("Nombre real: ");
                    String rol = vista.pedirTexto("Sera administrador? (S/N): ");
                    
                    Usuario nuevo;
                    if (rol.equalsIgnoreCase("S")) {
                        nuevo = new UsuarioAdmin(user, correo, passw, nombreReal);
                    } else {
                        nuevo = new Usuario(user, correo, passw, nombreReal);
                    }
                    
                    if (gestionDatos.registrarUsuario(nuevo)) {
                        vista.mostrarExito("Usuario registrado correctamente. Ya puedes iniciar sesion.");
                        try {
                            inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
                        } catch(Exception e) {}
                    } else {
                        vista.mostrarError("Ese usuario ya existe.");
                    }
                    break;

                case "3":
                    // ------------------ SALIR ------------------
                    salirMenuPrincipal = true;
                    try {
                        inventario.utilidades.PersistenciaDatos.guardar(gestionDatos);
                    } catch(Exception e) {}
                    vista.mostrarExito("Gracias por usar el sistema! Hasta pronto.");
                    break;
                    
                default:
                    vista.mostrarError("Opcion no valida.");
            }
        }
    }
}