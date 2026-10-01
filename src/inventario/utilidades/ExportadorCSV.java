package inventario.utilidades;

import inventario.utilidades.*;

import inventario.modelo.Pedido;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportadorCSV {
    public static void exportar(List<Pedido> pedidos, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write("ID_Pedido;Fecha;Correo_Usuario;Total");
            bw.newLine();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Pedido p : pedidos) {
                String linea = String.format("%s;%s;%s;%.2f", 
                        p.getIdPedido(), 
                        p.getFecha().format(formatter), 
                        p.getUsuario().getCorreo(), 
                        p.getTotal());
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(" Error al exportar a CSV: " + e.getMessage());
        }
    }
}





