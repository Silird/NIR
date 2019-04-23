package ru.silirdco.nir.view.util.gson;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.silirdco.nir.core.entities.Multioperation;

import java.lang.reflect.Type;

public class MultioperationDeserializer implements JsonDeserializer<Multioperation> {

    @Override
    public Multioperation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        //JsonObject jsonObject = jsonElement.getAsJsonObject();

        String vector = jsonElement.getAsString();

        return new Multioperation(vector);
    }
}
