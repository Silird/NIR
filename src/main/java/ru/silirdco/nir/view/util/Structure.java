package ru.silirdco.nir.view.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.silirdco.nir.core.entities.Multioperation;
import ru.silirdco.nir.view.util.gson.MultioperationDeserializer;
import ru.silirdco.nir.view.util.gson.MultioperationSerializer;

import java.text.SimpleDateFormat;

@SuppressWarnings("unused")
public class Structure {
    public final static SimpleDateFormat formatTimeMillisecondTZ = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSSZ");
    public final static SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    private static Gson gson;
    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Multioperation.class, new MultioperationSerializer())
                    .registerTypeAdapter(Multioperation.class, new MultioperationDeserializer())
                    .create();
        }
        return gson;
    }
}
