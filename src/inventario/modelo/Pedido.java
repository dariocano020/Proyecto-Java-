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


    private static final String YELLOW = "\u001B[33m";



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

    }