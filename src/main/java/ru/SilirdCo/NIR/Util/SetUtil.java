package ru.SilirdCo.NIR.Util;

import ru.SilirdCo.NIR.Entities.Element;

import java.util.HashSet;
import java.util.Set;

public class SetUtil {
    // Объединение двух элементов
    // 0 + 1 = 1
    // 1 + 2 = 3
    // 1 + 1 = 1
    // 0 + 0 = 0
    public static Element union(Element first, Element second) {
        Set<Integer> resultSet = new HashSet<>();
        resultSet.addAll(first.getSet());
        resultSet.addAll(second.getSet());

        return Element.getElement(resultSet);
    }
}
