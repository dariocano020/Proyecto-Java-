package inventario.utilidades;

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

    public static void guardar(GestionDatos datos) throws IOException {
        try (Writer writer = new FileWriter(ARCHIVO)) {
            getGson().toJson(datos, writer);
        }
    }

    public static GestionDatos cargar() throws IOException {
        File file = new File(ARCHIVO);
        if (!file.exists()) return null;
        
        try (Reader reader = new FileReader(ARCHIVO)) {
            return getGson().fromJson(reader, GestionDatos.class);
        }
    }
}