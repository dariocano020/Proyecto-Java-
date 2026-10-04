package inventario.utilidades;

import inventario.modelo.Pedido;
import inventario.modelo.LineaPedido;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportadorCSV {
    public static void exportar(List<Pedido> historial, String archivo) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("ID_PEDIDO,FECHA,CLIENTE,PRODUCTO,CANTIDAD,SUBTOTAL");
            bw.newLine();
            
            for (Pedido p : historial) {
                for (LineaPedido lp : p.getLineas()) {
                    bw.write(String.format("%s,%s,%s,%s,%d,%.2f",
                            p.getIdPedido(),
                            p.getFecha().format(formatter),
                            p.getUsuario().getUsername(),
                            lp.getProducto().getNombre(),
                            lp.getCantidad(),
                            lp.getSubtotal()
                    ));
                    bw.newLine();
                }
            }
        }
    }
}