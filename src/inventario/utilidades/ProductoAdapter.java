package inventario.utilidades;

import inventario.utilidades.*;

import com.google.gson.*;
import inventario.modelo.*;

import java.lang.reflect.Type;

public class ProductoAdapter implements JsonDeserializer<Producto> {
    @Override
    public Producto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String tipo = jsonObject.has("tipo") ? jsonObject.get("tipo").getAsString() : "";

        if (tipo.equals("ProductoFisico")) {
            return context.deserialize(json, ProductoFisico.class);
        } else if (tipo.equals("ProductoDigital")) {
            return context.deserialize(json, ProductoDigital.class);
        }
        throw new JsonParseException("Tipo de producto desconocido: " + tipo);
    }
}