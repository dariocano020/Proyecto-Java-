package inventario.modelo;

import inventario.utilidades.*;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Pedido implements Serializable {
    private String idPedido;
    private LocalDateTime fecha;
    private Usuario usuario;
    private List<LineaPedido> lineas;

    // Colores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    public Pedido(String idPedido, Usuario usuario, List<LineaPedido> lineas) {
        this.idPedido = idPedido;
        this.fecha = LocalDateTime.now();
        this.usuario = usuario;
        this.lineas = lineas;
    }

    public String getIdPedido() { return idPedido; }
    public LocalDateTime getFecha() { return fecha; }
    public Usuario getUsuario() { return usuario; }
    public List<LineaPedido> getLineas() { return lineas; }
    
    public double getTotal() {
        return lineas.stream().mapToDouble(LineaPedido::getSubtotal).sum();
    }

    public void imprimirFactura() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        System.out.println(CYAN + "\n=======================================================");
        System.out.println(CYAN + "=======================================================" + RESET);
        System.out.println(BOLD + "  N Pedido: " + RESET + idPedido);
        System.out.println(BOLD + "  Fecha:    " + RESET + fecha.format(formatter));
        System.out.println(BOLD + "  Cliente:  " + RESET + usuario.getNombre() + " (" + usuario.getCorreo() + ")");
        System.out.println(CYAN + "-------------------------------------------------------" + RESET);
        System.out.printf(BOLD + " %-5s | %-30s | %-10s \n" + RESET, "CANT", "PRODUCTO", "SUBTOTAL");
        System.out.println(CYAN + "-------------------------------------------------------" + RESET);
        
        for (LineaPedido linea : lineas) {
            System.out.printf(" %-5d | %-30s | %-10.2f \n", 
                linea.getCantidad(), 
                linea.getProducto().getNombre(), 
                linea.getSubtotal());
        }
        
        System.out.println(CYAN + "-------------------------------------------------------" + RESET);
        System.out.printf(GREEN + BOLD + "  TOTAL A PAGAR: %33.2f \n" + RESET, getTotal());
        System.out.println(CYAN + "=======================================================\n" + RESET);
    }
}




