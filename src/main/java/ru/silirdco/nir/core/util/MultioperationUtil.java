package ru.silirdco.nir.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.silirdco.nir.core.entities.Element;
import ru.silirdco.nir.core.entities.Multioperation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class MultioperationUtil {
    private static final Logger logger = LoggerFactory.getLogger(MultioperationUtil.class);

    // Подстановка на 1 элемент
    public static Multioperation getSubstitution(Multioperation multioperation0,
                                              Multioperation multioperation1) {
        return getSuperposition(multioperation0, multioperation1, Multioperation.E22);
    }

    // Оператор разрешимости по первому аргументу (см лекцию)
    public static Multioperation getFirstMegaoperation(Multioperation m) {
        Set<Integer> set = new HashSet<>();

        if (m.get(1).contains(1)) {
            set.add(1);
        }
        if (m.get(3).contains(1)) {
            set.add(2);
        }
        Element oneOne = Element.getElement(set);

        set.clear();
        if (m.get(2).contains(1)) {
            set.add(1);
        }
        if (m.get(4).contains(1)) {
            set.add(2);
        }
        Element oneTwo = Element.getElement(set);


        set.clear();
        if (m.get(1).contains(2)) {
            set.add(1);
        }
        if (m.get(3).contains(2)) {
            set.add(2);
        }
        Element twoOne = Element.getElement(set);

        set.clear();
        if (m.get(2).contains(2)) {
            set.add(1);
        }
        if (m.get(4).contains(2)) {
            set.add(2);
        }
        Element twoTwo = Element.getElement(set);

        return new Multioperation(oneOne, oneTwo, twoOne, twoTwo);
    }

    // Пересечение
    public static Multioperation getCross(Multioperation m1, Multioperation m2) {
        Element oneOne = Element.getElement(cross(m1.get(1).getSet(), m2.get(1).getSet()));
        Element oneTwo = Element.getElement(cross(m1.get(2).getSet(), m2.get(2).getSet()));
        Element twoOne = Element.getElement(cross(m1.get(3).getSet(), m2.get(3).getSet()));
        Element twoTwo = Element.getElement(cross(m1.get(4).getSet(), m2.get(4).getSet()));

        return new Multioperation(oneOne, oneTwo, twoOne, twoTwo);
    }

    // Пересечение
    public static Multioperation getUnion(Multioperation m1, Multioperation m2) {
        Element oneOne = Element.getElement(union(m1.get(1).getSet(), m2.get(1).getSet()));
        Element oneTwo = Element.getElement(union(m1.get(2).getSet(), m2.get(2).getSet()));
        Element twoOne = Element.getElement(union(m1.get(3).getSet(), m2.get(3).getSet()));
        Element twoTwo = Element.getElement(union(m1.get(4).getSet(), m2.get(4).getSet()));

        return new Multioperation(oneOne, oneTwo, twoOne, twoTwo);
    }

    // Дополнение
    public static Multioperation getAddition(Multioperation m) {
        Set<Integer> fullSet = Element.FULL.getSet();
        Element oneOne = Element.getElement(deletion(fullSet, m.get(1).getSet()));
        Element oneTwo = Element.getElement(deletion(fullSet, m.get(2).getSet()));
        Element twoOne = Element.getElement(deletion(fullSet, m.get(3).getSet()));
        Element twoTwo = Element.getElement(deletion(fullSet, m.get(4).getSet()));

        return new Multioperation(oneOne, oneTwo, twoOne, twoTwo);
    }

    // Транспозиция
    public static Multioperation getTransposition(Multioperation m) {
        return new Multioperation(
                m.get(1),
                m.get(3),
                m.get(2),
                m.get(4)
        );
    }

    // util
    // ====================================================
    private static Set<Integer> cross(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> result = new HashSet<>();
        for (Integer i : s1) {
            if ((i != null) && (s2.contains(i))) {
                result.add(i);
            }
        }
        return result;
    }

    private static Set<Integer> union(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    /**
     * Вычитание одного множества из другого
     * @param s1 множество, из которого надо вычесть
     * @param s2 вычитаемое множество
     * @return результирующее множество
     */
    private static Set<Integer> deletion(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> result = new HashSet<>(s1);
        for (Integer i : s1) {
            if ((i != null) && (s2.contains(i))) {
                result.remove(i);
            }
        }
        return result;
    }

    public static Multioperation getSecondMegaoperation(Multioperation m) {
        Set<Integer> set = new HashSet<>();

        if (m.get(1).contains(1)) {
            set.add(1);
        }
        if (m.get(2).contains(1)) {
            set.add(2);
        }
        Element oneOne = Element.getElement(set);

        set.clear();
        if (m.get(1).contains(2)) {
            set.add(1);
        }
        if (m.get(2).contains(2)) {
            set.add(2);
        }
        Element oneTwo = Element.getElement(set);


        set.clear();
        if (m.get(3).contains(1)) {
            set.add(1);
        }
        if (m.get(4).contains(1)) {
            set.add(2);
        }
        Element twoOne = Element.getElement(set);

        set.clear();
        if (m.get(3).contains(2)) {
            set.add(1);
        }
        if (m.get(4).contains(2)) {
            set.add(2);
        }
        Element twoTwo = Element.getElement(set);

        return new Multioperation(oneOne, oneTwo, twoOne, twoTwo);
    }

    // Суперпозиция
    public static Multioperation getSuperposition(Multioperation m1,
                                                  Multioperation m2,
                                                  Multioperation m3) {
        Set<Integer> set = new HashSet<>();
        for (Integer integer : generateCheckListSuperposition(1, m2, m3)) {
            set.addAll(m1.get(integer).getSet());
        }
        Element oneOne = Element.getElement(set);

        set.clear();
        for (Integer integer : generateCheckListSuperposition(2, m2, m3)) {
            set.addAll(m1.get(integer).getSet());
        }
        Element oneTwo = Element.getElement(set);

        set.clear();
        for (Integer integer : generateCheckListSuperposition(3, m2, m3)) {
            set.addAll(m1.get(integer).getSet());
        }
        Element twoOne = Element.getElement(set);

        set.clear();
        for (Integer integer : generateCheckListSuperposition(4, m2, m3)) {
            set.addAll(m1.get(integer).getSet());
        }
        Element twoTwo = Element.getElement(set);

        return new Multioperation(oneOne, oneTwo, twoOne, twoTwo);
    }

    private static List<Integer> generateCheckListSuperposition(int pos,
                                                                Multioperation m1,
                                                                Multioperation m2) {
        List<Integer> checkList = new ArrayList<>();
        switch (m1.get(pos)) {
            case EMPTY:
                break;
            case E1:
                checkList.add(1);
                checkList.add(2);
                break;
            case E2:
                checkList.add(3);
                checkList.add(4);
                break;
            case FULL:
                checkList.add(1);
                checkList.add(2);
                checkList.add(3);
                checkList.add(4);
                break;
        }


        switch (m2.get(pos)) {
            case EMPTY:
                checkList.remove(new Integer(1));
                checkList.remove(new Integer(2));
                checkList.remove(new Integer(3));
                checkList.remove(new Integer(4));
                break;
            case E1:
                checkList.remove(new Integer(2));
                checkList.remove(new Integer(4));
                break;
            case E2:
                checkList.remove(new Integer(1));
                checkList.remove(new Integer(3));
                break;
            case FULL:
                break;
        }

        return checkList;
    }
}
