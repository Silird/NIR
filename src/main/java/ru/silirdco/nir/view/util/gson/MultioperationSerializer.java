package ru.silirdco.nir.view.util.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.silirdco.nir.core.entities.Multioperation;

import java.lang.reflect.Type;

public class MultioperationSerializer implements JsonSerializer<Multioperation> {
    @Override
    public JsonElement serialize(Multioperation multioperation, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(multioperation.toStringVector());
    }
}
