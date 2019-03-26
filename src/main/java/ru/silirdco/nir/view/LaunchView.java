package ru.silirdco.nir.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.silirdco.nir.core.util.ExceptionHandler;
import ru.silirdco.nir.view.frames.MainJavaFX;

public class LaunchView {
    private static final Logger logger = LoggerFactory.getLogger(LaunchView.class);

    public static boolean launch() {
        logger.info("\n\nЗапуск визуальной формы\n\n");

        try {

            logger.info("\n\nЗапуск формы..\n\n");
            MainJavaFX.show(null);
        }
        catch (Exception ex) {
            ExceptionHandler.handle(logger, ex);

            return false;
        }

        return true;
    }
}
