package inventario.utilidades;

import inventario.utilidades.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inventario.controlador.GestionDatos;
import inventario.modelo.Producto;
import inventario.modelo.Usuario;

import java.io.*;
import java.time.LocalDateTime;

public class PersistenciaDatos {
    private static final String ARCHIVO = "datos_inventario.json";
    
    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Producto.class, new ProductoAdapter())
                .registerTypeAdapter(Usuario.class, new UsuarioAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static void guardar(GestionDatos datos) {
        try (Writer writer = new FileWriter(ARCHIVO)) {
            getGson().toJson(datos, writer);
        } catch (IOException e) {
            System.out.println(" Error guardando JSON: " + e.getMessage());
        }
    }

    public static GestionDatos cargar() {
        File f = new File(ARCHIVO);
        if (f.exists()) {
            try (Reader reader = new FileReader(ARCHIVO)) {
                return getGson().fromJson(reader, GestionDatos.class);
            } catch (Exception e) {
                System.out.println(" Error cargando JSON. Se iniciar desde cero: " + e.getMessage());
            }
        }
        return null;
    }
}





