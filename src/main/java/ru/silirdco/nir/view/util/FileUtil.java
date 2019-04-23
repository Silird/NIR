package ru.silirdco.nir.view.util;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;
import ru.silirdco.nir.view.frames.MainJavaFX;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    private static FileChooser fileChooser;
    static {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    }
    private static Window window;

    public static File openFile() {
        File file = fileChooser.showOpenDialog(getWindow());
        if (file != null) {
            fileChooser.setInitialDirectory(file.getParentFile());
        }
        return file;
    }

    public static File getSaveFile() {
        File file = fileChooser.showSaveDialog(getWindow());
        if (file != null) {
            fileChooser.setInitialDirectory(file.getParentFile());
        }
        return file;
    }

    public static void writeText(File file, String content) throws FileNotFoundException {
        //PrintWriter обеспечит возможности записи в файл
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), StandardCharsets.UTF_8))) {
            //Записываем текст в файл
            out.print(content);
        }
    }

    public static String readText(File file) throws IOException {
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    private static Window getWindow() {
        if (window == null) {
            window = MainJavaFX.getStage();
        }
        return window;
    }
}
