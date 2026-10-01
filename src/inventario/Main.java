package inventario;

import inventario.utilidades.*;

import inventario.modelo.*;
import inventario.vista.*;
import inventario.controlador.*;
import inventario.controlador.GestionDatos;
import inventario.controlador.TiendaControlador;
import inventario.utilidades.PersistenciaDatos;
import inventario.vista.ConsolaVista;

public class Main {
    public static void main(String[] args) {
        ConsolaVista vista = new ConsolaVista();
        
        // 1. Cargar datos si existen
        GestionDatos gestion = PersistenciaDatos.cargar();
        if (gestion == null) {
            gestion = new GestionDatos();
        }
        
        final GestionDatos datosAsalvar = gestion;
        
        // 2. Guardar automticamente al cerrar el programa
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            PersistenciaDatos.guardar(datosAsalvar);
            System.out.println("\n [AUTO-SAVE] Datos guardados correctamente al salir.");
        }));
        
        TiendaControlador app = new TiendaControlador(vista, gestion);
        app.iniciar();
    }
}




