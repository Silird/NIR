package ru.SilirdCo.NIR.Util;

import ru.SilirdCo.NIR.Entities.Element;

import java.util.HashSet;
import java.util.Set;

public class SetUtil {
    public static Element union(Element first, Element second) {
        Set<Integer> resultSet = new HashSet<>();
        resultSet.addAll(first.getSet());
        resultSet.addAll(second.getSet());

        return Element.getElement(resultSet);
    }
}
