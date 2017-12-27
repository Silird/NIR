package ru.SilirdCo.NIR.Main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.SilirdCo.NIR.Entities.Element;
import ru.SilirdCo.NIR.Entities.Multioperation;
import ru.SilirdCo.NIR.Util.EnumerationUtil;
import ru.SilirdCo.NIR.Util.MultioperationUtil;
import ru.SilirdCo.NIR.Util.WriteUtil;

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
        findAlgebra(start, multioperations, new HashMap<>());

        WriteUtil.print(resultList);
    }

    private static List<Multioperation> findAlgebra(List<Multioperation> startList, List<Multioperation> multioperations,
                                    Map<Multioperation, List<Multioperation>> knownDependencies) {
        logger.info("Новый поиск по множеству с количеством: " + startList.size());

        List<Multioperation> result = checkAlgebra(startList, knownDependencies);

        addToResult(startList);

        if (startList.size() != 256) {
            if (!testContainList(checkedList, startList)) {
                Map<Multioperation, List<Multioperation>> kD = new HashMap<>();
                for (Multioperation multioperation : multioperations) {
                    if (!startList.contains(multioperation)) {
                        List<Multioperation> newList = new ArrayList<>(startList);
                        newList.add(multioperation);
                        List<Multioperation> k = findAlgebra(newList, multioperations, kD);

                        kD.put(multioperation, k);
                    }
                }

                checkedList.add(startList);
            }
        }

        return result;
    }

    private static List<Multioperation> checkAlgebra(List<Multioperation> startList,
                                     Map<Multioperation, List<Multioperation>> knownDependencies) {
        List<Multioperation> result = new ArrayList<>();
        if (startList.size() != 256) {
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
                result.addAll(newMultioperations);
                addNonContain(startList, checkKnown(newMultioperations, knownDependencies));
                result.addAll(checkAlgebra(startList, knownDependencies));
            }
        }

        return result;
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

    private static Collection<Multioperation> checkKnown(Collection<Multioperation> newMultioperations,
                                                   Map<Multioperation, List<Multioperation>> knownDependencies) {
        Collection<Multioperation> result = new TreeSet<>();

        for (Multioperation multioperation : newMultioperations) {
            result.add(multioperation);
            for (Multioperation key : knownDependencies.keySet()) {
                if (multioperation.equals(key)) {
                    result.addAll(knownDependencies.get(key));
                }
            }
            if (result.size() == 256) {
                break;
            }
        }

        return result;
    }

    private static void addNonContain(Collection<Multioperation> list, Collection<Multioperation> newList) {
        for (Multioperation multioperation : newList) {
            if (!list.contains(multioperation)) {
                list.add(multioperation);
                if (list.size() == 256) {
                    break;
                }
            }
        }
    }
}
