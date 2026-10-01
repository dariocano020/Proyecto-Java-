package inventario.utilidades;

import inventario.utilidades.*;

import com.google.gson.*;
import inventario.modelo.*;

import java.lang.reflect.Type;

public class UsuarioAdapter implements JsonDeserializer<Usuario> {
    @Override
    public Usuario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String tipo = jsonObject.has("tipo") ? jsonObject.get("tipo").getAsString() : "Usuario";

        if (tipo.equals("UsuarioAdmin")) {
            return context.deserialize(json, UsuarioAdmin.class);
        } else {
            // Usamos un Gson nuevo para que no vuelva a llamar a este mismo Adapter y crear un bucle
            return new Gson().fromJson(json, Usuario.class);
        }
    }
}





