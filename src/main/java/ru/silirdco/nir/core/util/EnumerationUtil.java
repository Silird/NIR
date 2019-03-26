package ru.silirdco.nir.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class EnumerationUtil {
    private static final Logger logger = LoggerFactory.getLogger(EnumerationUtil.class);

    // Просмотреть все возможные комбинации с элементами list и выполнить для каждой такой комбинации action
    // TODO имхо очень криво сделано
    public static <T> void enumerate(final List<T> list, Consumer<List<T>> action) {
        for (int i = 0; i < list.size(); i++) {
            enumerate(i, list, action);
        }
    }

    // Просмотреть все возможные комбинации с элементами list в количестве элементов не большем limit
    // и выполнить для каждой такой комбинации action
    public static <T> void enumerate(int limit, final Collection<T> list, Consumer<List<T>> action) {
        generateList(0, limit, list, new ArrayList<>(), action);
    }

    private static <T> void generateList(int depth, int max,
                                         Collection<T> list, List<T> generated,
                                         Consumer<List<T>> action) {
        if (depth == max) {
            if (action == null) {
                logger.warn("Действие не найдено");
            }
            else {
                action.accept(generated);
            }
        }
        else {
            for (T t : list) {
                List<T> newList = new ArrayList<>(generated);
                newList.add(t);
                generateList(depth + 1, max, list, newList, action);
            }
        }
    }

    // Тоже самое что и выше, только в комбинациях элементы не могут повторяться
    private static <T> void generateListWithOutDuplication(int depth, int max,
                                         Collection<T> list, List<T> generated,
                                         Consumer<List<T>> action) {
        if (depth == max) {
            if (action == null) {
                logger.warn("Действие не найдено");
            }
            else {
                action.accept(generated);
            }
        }
        else {
            for (T t : list) {
                if (!generated.contains(t)) {
                    List<T> newList = new ArrayList<>(generated);
                    newList.add(t);
                    generateList(depth + 1, max, list, newList, action);
                }
            }
        }
    }
}
