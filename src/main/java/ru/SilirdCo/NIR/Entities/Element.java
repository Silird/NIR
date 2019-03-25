package ru.SilirdCo.NIR.Entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public enum Element {
    EMPTY,
    E1,
    E2,
    FULL;

    private static final Logger logger = LoggerFactory.getLogger(Element.class);

    // Получить перечисление какие операции есть в множестве для вычисления
    public Set<Integer> getSet() {
        Set<Integer> result = new HashSet<>();
        switch (this) {
            case EMPTY:
                return result;
            case E1:
                result.add(1);
                return result;
            case E2:
                result.add(2);
                return result;
            case FULL:
                result.add(1);
                result.add(2);
                return result;
            default:
                logger.warn("Не найдено множество для: " + this);
                return result;
        }
    }

    // Получить элемент по данному перечислению
    public static Element getElement(Set<Integer> set) {
        if (set.isEmpty()) {
            return Element.EMPTY;
        }
        else {
            if (set.size() == 1) {
                if (set.contains(1)) {
                    return Element.E1;
                }
                else {
                    return Element.E2;
                }
            }
            else {
                return Element.FULL;
            }
        }
    }

    // Получить элемент по векторной записи
    public static Element getElement(String str) {
        switch(str) {
            case "0":
                return EMPTY;
            case "1":
                return E1;
            case "2":
                return E2;
            case "3":
                return FULL;
            default:
                logger.warn("Не найдено множество для: " + str);
                return EMPTY;
        }
    }

    // Получить векторную запись элемента
    public String getVector() {
        switch (this) {
            case EMPTY:
                return "0";
            case E1:
                return "1";
            case E2:
                return "2";
            case FULL:
                return "3";
            default:
                logger.warn("Не найдено множество для: " + this);
                return "";
        }
    }

    // Получить полную запись элемента
    public String getFullRecord() {
        switch (this) {
            case EMPTY:
                return "/O/";
            case E1:
                return "{1}";
            case E2:
                return "{2}";
            case FULL:
                return "{1, 2}";
            default:
                logger.warn("Не найдено множество для: " + this);
                return "";
        }
    }

    public boolean contains(int value) {
        return this.getSet().contains(value);
    }

    @Override
    public String toString() {
        return getVector() + " " + getFullRecord();
    }
}
