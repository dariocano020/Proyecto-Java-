package inventario;

import inventario.modelo.*;
import inventario.vista.*;
import inventario.controlador.*;
import inventario.utilidades.PersistenciaDatos;

public class Main {
    public static void main(String[] args) {
        ConsolaVista vista = new ConsolaVista();
        
        GestionDatos gestion = null;
        try {
            gestion = PersistenciaDatos.cargar();
        } catch(Exception e) {
            vista.mostrarError("Error cargando JSON. Se iniciara desde cero: " + e.getMessage());
        }
        
        if (gestion == null) {
            gestion = new GestionDatos();
        }
        
        final GestionDatos datosAsalvar = gestion;
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                PersistenciaDatos.guardar(datosAsalvar);
                vista.mostrarMensaje("[AUTO-SAVE] Datos guardados correctamente al salir.");
            } catch(Exception e) {
                vista.mostrarError("Error al guardar datos automaticamente.");
            }
        }));
        
        InicioApp app = new InicioApp(vista, gestion);
        app.iniciar();
    }
}