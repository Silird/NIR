package ru.SilirdCo.NIR.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.SilirdCo.NIR.Entities.Element;
import ru.SilirdCo.NIR.Entities.Multioperation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultioperationUtil {
    private static final Logger logger = LoggerFactory.getLogger(MultioperationUtil.class);

    public static Multioperation getFirstMegaoperation(Multioperation multioperation) {
        Set<Integer> set = new HashSet<>();

        if (multioperation.get(1).getSet().contains(1)) {
            set.add(1);
        }
        if (multioperation.get(3).getSet().contains(1)) {
            set.add(2);
        }
        Element oneOne = Element.getElement(set);

        set.clear();
        if (multioperation.get(2).getSet().contains(1)) {
            set.add(1);
        }
        if (multioperation.get(4).getSet().contains(1)) {
            set.add(2);
        }
        Element oneTwo = Element.getElement(set);


        set.clear();
        if (multioperation.get(1).getSet().contains(2)) {
            set.add(1);
        }
        if (multioperation.get(3).getSet().contains(2)) {
            set.add(2);
        }
        Element twoOne = Element.getElement(set);

        set.clear();
        if (multioperation.get(2).getSet().contains(2)) {
            set.add(1);
        }
        if (multioperation.get(4).getSet().contains(2)) {
            set.add(2);
        }
        Element twoTwo = Element.getElement(set);

        return new Multioperation(oneOne, oneTwo, twoOne, twoTwo);
    }

    public static Multioperation getSecondMegaoperation(Multioperation multioperation) {
        Set<Integer> set = new HashSet<>();

        if (multioperation.get(1).getSet().contains(1)) {
            set.add(1);
        }
        if (multioperation.get(2).getSet().contains(1)) {
            set.add(2);
        }
        Element oneOne = Element.getElement(set);

        set.clear();
        if (multioperation.get(1).getSet().contains(2)) {
            set.add(1);
        }
        if (multioperation.get(2).getSet().contains(2)) {
            set.add(2);
        }
        Element oneTwo = Element.getElement(set);


        set.clear();
        if (multioperation.get(3).getSet().contains(1)) {
            set.add(1);
        }
        if (multioperation.get(4).getSet().contains(1)) {
            set.add(2);
        }
        Element twoOne = Element.getElement(set);

        set.clear();
        if (multioperation.get(3).getSet().contains(2)) {
            set.add(1);
        }
        if (multioperation.get(4).getSet().contains(2)) {
            set.add(2);
        }
        Element twoTwo = Element.getElement(set);

        return new Multioperation(oneOne, oneTwo, twoOne, twoTwo);
    }

    public static Multioperation getSuperposition(Multioperation multioperation0,
                                                  Multioperation multioperation1,
                                                  Multioperation multioperation2) {
        Set<Integer> set = new HashSet<>();
        for (Integer integer : generateCheckListSuperposition(1, multioperation1, multioperation2)) {
            set.addAll(multioperation0.get(integer).getSet());
        }
        Element oneOne = Element.getElement(set);

        set.clear();
        for (Integer integer : generateCheckListSuperposition(2, multioperation1, multioperation2)) {
            set.addAll(multioperation0.get(integer).getSet());
        }
        Element oneTwo = Element.getElement(set);

        set.clear();
        for (Integer integer : generateCheckListSuperposition(3, multioperation1, multioperation2)) {
            set.addAll(multioperation0.get(integer).getSet());
        }
        Element twoOne = Element.getElement(set);

        set.clear();
        for (Integer integer : generateCheckListSuperposition(4, multioperation1, multioperation2)) {
            set.addAll(multioperation0.get(integer).getSet());
        }
        Element twoTwo = Element.getElement(set);

        return new Multioperation(oneOne, oneTwo, twoOne, twoTwo);
    }

    public static List<Integer> generateCheckListSuperposition(int pos,
                                                               Multioperation multioperation1,
                                                               Multioperation multioperation2) {
        List<Integer> checkList = new ArrayList<>();
        switch (multioperation1.get(pos)) {
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


        switch (multioperation2.get(pos)) {
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
