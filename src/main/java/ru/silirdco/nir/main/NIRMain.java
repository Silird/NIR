package ru.silirdco.nir.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.silirdco.nir.core.entities.Element;
import ru.silirdco.nir.core.entities.Multioperation;
import ru.silirdco.nir.core.util.EnumerationUtil;
import ru.silirdco.nir.core.util.MultioperationUtil;
import ru.silirdco.nir.core.util.WriteUtil;

import java.util.*;

public class NIRMain {
    private static final Logger logger = LoggerFactory.getLogger(NIRMain.class);

    private static List<List<Multioperation>> resultList = new ArrayList<>();
    private static List<List<Multioperation>> checkedList = new ArrayList<>();

    public static void main(String[] args) {
        List<Element> list = new ArrayList<>();
        list.add(Element.EMPTY);
        list.add(Element.E1);
        list.add(Element.E2);
        list.add(Element.FULL);

        List<Multioperation> multioperations = new ArrayList<>();

        EnumerationUtil.enumerate(4, list, elements -> {
            Multioperation multioperation = new Multioperation(
                    elements.get(0),
                    elements.get(1),
                    elements.get(2),
                    elements.get(3)
            );
            multioperations.add(multioperation);
        });

        List<Multioperation> start = new ArrayList<>();
        start.add(Multioperation.EMPTY);
        start.add(Multioperation.E12);
        start.add(Multioperation.E22);
        start.add(Multioperation.FULL);
        findAlgebra(start, multioperations);

        WriteUtil.print(resultList);
    }

    private static void findAlgebra(List<Multioperation> startList, List<Multioperation> multioperations) {
        logger.info("Новый поиск по множеству с количеством: " + startList.size());
        
        checkAlgebra(startList);

        addToResult(startList);

        if (startList.size() != 256) {
            if (!testContainList(checkedList, startList)) {
                for (Multioperation multioperation : multioperations) {
                    if (!startList.contains(multioperation)) {
                        List<Multioperation> newList = new ArrayList<>(startList);
                        newList.add(multioperation);
                        findAlgebra(newList, multioperations);
                    }
                }

                checkedList.add(startList);
            }
        }
    }

    private static void checkAlgebra(List<Multioperation> startList) {
        TreeSet<Multioperation> newMultioperations = new TreeSet<>();

        for (Multioperation multioperation : startList) {
            Multioperation firstMegaoperation = MultioperationUtil.getFirstMegaoperation(multioperation);
            if (!startList.contains(firstMegaoperation)) {
                newMultioperations.add(firstMegaoperation);
            }

            Multioperation secondMegaoperation = MultioperationUtil.getSecondMegaoperation(multioperation);
            if (!startList.contains(secondMegaoperation)) {
                newMultioperations.add(secondMegaoperation);
            }
        }

        EnumerationUtil.enumerate(3, startList, multioperations -> {
            Multioperation superposition = MultioperationUtil
                    .getSuperposition(multioperations.get(0), multioperations.get(1), multioperations.get(2));
            if (!startList.contains(superposition)) {
                newMultioperations.add(superposition);
            }
        });

        if (newMultioperations.size() != 0) {
            startList.addAll(newMultioperations);
            checkAlgebra(startList);
        }
    }

    private static void addToResult(List<Multioperation> multioperations) {
        boolean isAdd = !testContainList(resultList, multioperations);

        if (isAdd) {
            resultList.add(multioperations);
            logger.info("Алгебра найдена(размер = " + multioperations.size() + "): " + Arrays.toString(multioperations.toArray()));
        }
    }

    private static boolean testEqualsLists(List<Multioperation> mL1, List<Multioperation> mL2) {
        if (mL1.size() == mL2.size()) {
            boolean equals = true;
            for (Multioperation multioperation : mL1) {
                if (!mL2.contains(multioperation)) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    private static boolean testContainList(List<List<Multioperation>> list, List<Multioperation> mL2) {
        for (List<Multioperation> mL : list) {
            if (testEqualsLists(mL, mL2)) {
                return true;
            }
        }

        return false;
    }
}
