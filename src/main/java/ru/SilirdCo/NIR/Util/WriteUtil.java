package ru.SilirdCo.NIR.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.SilirdCo.NIR.Entities.Multioperation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class WriteUtil {
    private static final Logger logger = LoggerFactory.getLogger(WriteUtil.class);

    public static void print(List<List<Multioperation>> list) {
        try {
            File myPath = new File(System.getProperty("user.dir") + "\\Print\\");

            if (!myPath.exists() && !myPath.mkdirs()) {
                logger.error("Ошибка при создании папки пути для файла:" + myPath);
                return;
            }

            File file = new File(System.getProperty("user.dir") + "\\Print\\out.txt");

            //проверяем, что если файл не существует то создаем его
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    logger.error("Ошибка при создании файла пути для файла:");
                    return;
                }
            }

            //PrintWriter обеспечит возможности записи в файл
            try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), StandardCharsets.UTF_8))) {
                //Записываем текст в файл
                StringBuilder stringBuilder = new StringBuilder();
                for (List<Multioperation> multioperations : list) {
                    stringBuilder.append(Arrays.toString(multioperations.toArray()));
                    stringBuilder.append("\n");
                }

                out.print(stringBuilder.toString());
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
