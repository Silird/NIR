package ru.silirdco.nir.core.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.silirdco.nir.core.entities.Element;
import ru.silirdco.nir.core.entities.Multioperation;
import ru.silirdco.nir.core.util.entity.Operations;

import java.util.*;

public class ClosedSetUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClosedSetUtil.class);

    private static Collection<Collection<Multioperation>> resultList = new ArrayList<>();
    private static Collection<Collection<Multioperation>> checkedList = new ArrayList<>();
    private static Operations operations;

    private static boolean first = true;
    public static DoubleProperty progressProperty = new SimpleDoubleProperty(0);

    public static Collection<Collection<Multioperation>> getClosedSets(Operations operations) {
        progressProperty.setValue(0);
        first = true;
        resultList.clear();
        checkedList.clear();
        ClosedSetUtil.operations = operations;

        Collection<Element> list = new ArrayList<>();
        list.add(Element.EMPTY);
        list.add(Element.E1);
        list.add(Element.E2);
        list.add(Element.FULL);

        Collection<Multioperation> multioperations = new ArrayList<>();

        EnumerationUtil.enumerate(4, list, elements -> {
            Multioperation multioperation = new Multioperation(
                    elements.get(0),
                    elements.get(1),
                    elements.get(2),
                    elements.get(3)
            );
            multioperations.add(multioperation);
        });

        TreeSet<Multioperation> start = new TreeSet<>();

        if (operations.isNulll()) {
            start.add(Multioperation.EMPTY);
        }
        if (operations.isOne()) {
            start.add(Multioperation.E12);
            start.add(Multioperation.E22);
        }
        if (operations.isUniversal()) {
            start.add(Multioperation.FULL);
        }
        findAlgebra(start, multioperations, new HashMap<>());

        // WriteUtil.print(resultList);
        return resultList;
    }

    private static Collection<Multioperation> findAlgebra(TreeSet<Multioperation> startList, Collection<Multioperation> multioperations,
                                                    Map<Multioperation, Collection<Multioperation>> knownDependencies) {

        boolean first_ = false;
        if (first) {
            first_ = true;
            first = false;
        }
        Collection<Multioperation> result = new ArrayList<>();
        if (!startList.isEmpty()) {
            result = checkAlgebra(startList, knownDependencies);

            addToResult(startList);
        }

        if (startList.size() != 256) {
            if (!testContainList(checkedList, startList)) {
                Map<Multioperation, Collection<Multioperation>> kD = new HashMap<>();
                int i = 0;
                for (Multioperation multioperation : multioperations) {
                    i++;
                    if (first_) {
                        double d = ((double) i)/256;
                        progressProperty.setValue(d);
                    }
                    if (!startList.contains(multioperation)) {
                        TreeSet<Multioperation> newList = new TreeSet<>(startList);
                        newList.add(multioperation);
                        logger.info("Новый поиск [(" + i + ") " + multioperation.toStringVector() + "] по множеству с количеством: " + newList.size());
                        Collection<Multioperation> k = findAlgebra(newList, multioperations, kD);

                        kD.put(multioperation, k);
                    }
                }

                checkedList.add(startList);
            }
        }

        return result;
    }

    private static Collection<Multioperation> checkAlgebra(TreeSet<Multioperation> startList,
                                                     Map<Multioperation, Collection<Multioperation>> knownDependencies) {
        Collection<Multioperation> result = new ArrayList<>();
        if (startList.size() != 256) {
            TreeSet<Multioperation> newMultioperations = new TreeSet<>();

            if (operations.isTransposition()) {
                EnumerationUtil.enumerate(1, startList, oneList -> {
                    Multioperation newMultioperation = MultioperationUtil.getTransposition(oneList.get(0));
                    if (!startList.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    result.addAll(newMultioperations);
                    startList.addAll(checkKnown(newMultioperations, knownDependencies));
                    return checkAlgebra(startList, knownDependencies);
                }
            }

            if (operations.isAddition()) {
                EnumerationUtil.enumerate(1, startList, oneList -> {
                    Multioperation newMultioperation = MultioperationUtil.getAddition(oneList.get(0));
                    if (!startList.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    result.addAll(newMultioperations);
                    startList.addAll(checkKnown(newMultioperations, knownDependencies));
                    return checkAlgebra(startList, knownDependencies);
                }
            }

            if (operations.isMegaoperation()) {
                EnumerationUtil.enumerate(1, startList, oneList -> {
                    Multioperation newMultioperation = MultioperationUtil.getFirstMegaoperation(oneList.get(0));
                    if (!startList.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    result.addAll(newMultioperations);
                    startList.addAll(checkKnown(newMultioperations, knownDependencies));
                    return checkAlgebra(startList, knownDependencies);
                }
            }

            if (operations.isCross()) {
                EnumerationUtil.enumerate(2, startList, twoList -> {
                    Multioperation newMultioperation = MultioperationUtil.getCross(twoList.get(0), twoList.get(1));
                    if (!startList.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    result.addAll(newMultioperations);
                    startList.addAll(checkKnown(newMultioperations, knownDependencies));
                    return checkAlgebra(startList, knownDependencies);
                }
            }

            if (operations.isUnion()) {
                EnumerationUtil.enumerate(2, startList, twoList -> {
                    Multioperation newMultioperation = MultioperationUtil.getUnion(twoList.get(0), twoList.get(1));
                    if (!startList.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    result.addAll(newMultioperations);
                    startList.addAll(checkKnown(newMultioperations, knownDependencies));
                    return checkAlgebra(startList, knownDependencies);
                }
            }

            if (operations.isSubstitution()) {
                EnumerationUtil.enumerate(2, startList, twoList -> {
                    Multioperation newMultioperation = MultioperationUtil.getSubstitution(twoList.get(0), twoList.get(1));
                    if (!startList.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    result.addAll(newMultioperations);
                    startList.addAll(checkKnown(newMultioperations, knownDependencies));
                    return checkAlgebra(startList, knownDependencies);
                }
            }

            if (operations.isSuperposition()) {
                EnumerationUtil.enumerate(3, startList, threeList -> {
                    Multioperation newMultioperation = MultioperationUtil.getSuperposition(
                            threeList.get(0),
                            threeList.get(1),
                            threeList.get(2));
                    if (!startList.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    result.addAll(newMultioperations);
                    startList.addAll(checkKnown(newMultioperations, knownDependencies));
                    return checkAlgebra(startList, knownDependencies);
                }
            }
        }

        return result;
    }

    /*
    Добавить алгебру в результат
     */
    private static void addToResult(Collection<Multioperation> multioperations) {
        boolean isAdd = !testContainList(resultList, multioperations);

        if (isAdd) {
            resultList.add(multioperations);
            logger.info("Алгебра найдена(размер = " + multioperations.size() + "): " + Arrays.toString(multioperations.toArray()));
        }
    }

    /*
    Проверка на эквивалентность листов
     */
    private static boolean testEqualsLists(Collection<Multioperation> mL1, Collection<Multioperation> mL2) {
        if (mL1.size() == mL2.size()) {
            for (Multioperation multioperation : mL1) {
                if (!mL2.contains(multioperation)) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    /*
    Проверка содержится ли в листе данный лист
     */
    private static boolean testContainList(Collection<Collection<Multioperation>> list, Collection<Multioperation> mL2) {
        for (Collection<Multioperation> mL : list) {
            if (testEqualsLists(mL, mL2)) {
                return true;
            }
        }

        return false;
    }

    /*
    Добавить в новые мультиоперации все известные зависимости
     */
    private static Collection<Multioperation> checkKnown(Collection<Multioperation> newMultioperations,
                                                         Map<Multioperation, Collection<Multioperation>> knownDependencies) {
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

    /*
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
    */
}
