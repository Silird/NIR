package ru.silirdco.nir.core.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.silirdco.nir.core.util.ExceptionHandler;

import java.util.Objects;

@SuppressWarnings("WeakerAccess")
public class Multioperation implements Comparable<Multioperation> {
    private final static Logger logger = LoggerFactory.getLogger(Multioperation.class);

    public final static Multioperation EMPTY = new Multioperation("0000");
    public final static Multioperation FULL = new Multioperation("3333");
    public final static Multioperation E12 = new Multioperation("1122");
    public final static Multioperation E22 = new Multioperation("1212");

    // oneOne (1) | oneTwo (2)
    // -----------------------
    // twoOne (3) | twoTwo (4)
    private final Element oneOne;
    private final Element oneTwo;
    private final Element twoOne;
    private final Element twoTwo;

    // Конструктор по 4 элеменам
    public Multioperation(Element oneOne, Element oneTwo, Element twoOne, Element twoTwo) {
        this.oneOne = oneOne;
        this.oneTwo = oneTwo;
        this.twoOne = twoOne;
        this.twoTwo = twoTwo;
    }

    //Конструктор по векторной записи (Так сложно так как финал переменные в классе)
    public Multioperation(String str) {
        String oneOneStr = null;
        String oneTwoStr = null;
        String twoOneStr = null;
        String twoTwoStr = null;
        try {
            oneOneStr = str.substring(0, 1);
            oneTwoStr = str.substring(1, 2);
            twoOneStr = str.substring(2, 3);
            twoTwoStr = str.substring(3, 4);
        }
        catch (StringIndexOutOfBoundsException ex) {
            ExceptionHandler.handle(logger, ex);
        }
        if (oneOneStr != null) {
            this.oneOne = Element.getElement(oneOneStr);
        }
        else {
            this.oneOne = Element.EMPTY;
        }
        if (oneTwoStr != null) {
            this.oneTwo = Element.getElement(oneTwoStr);
        }
        else {
            this.oneTwo = Element.EMPTY;
        }
        if (twoOneStr != null) {
            this.twoOne = Element.getElement(twoOneStr);
        }
        else {
            this.twoOne = Element.EMPTY;
        }
        if (twoTwoStr != null) {
            this.twoTwo = Element.getElement(twoTwoStr);
        }
        else {
            this.twoTwo = Element.EMPTY;
        }
    }

    public Element get(int pos) {
        switch (pos) {
            case 1:
                return oneOne;
            case 2:
                return oneTwo;
            case 3:
                return twoOne;
            case 4:
                return twoTwo;
            default:
                logger.warn("Нет позиции " + pos + " в мультиоперации!");
                return null;
        }
    }

    public String toStringVector() {
        return oneOne.getVector() + oneTwo.getVector() + twoOne.getVector() + twoTwo.getVector();
    }

    public String toStringFull() {
        return "(" + oneOne.getFullRecord() + ", " + oneTwo.getFullRecord() + ", " +
                twoOne.getFullRecord() + ", " + twoTwo.getFullRecord() + ")";
    }

    @Override
    public String toString() {
        return toStringFull();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if((object == null) || (getClass()!= object.getClass())) return  false;

        Multioperation other = (Multioperation) object;
        if (!Objects.equals(this.oneOne, other.oneOne)) return false;
        if (!Objects.equals(this.oneTwo, other.oneTwo)) return false;
        if (!Objects.equals(this.twoOne, other.twoOne)) return false;
        if (!Objects.equals(this.twoTwo, other.twoTwo)) return false;

        return true;
    }

    @Override
    public int compareTo(Multioperation o) {
        int result = oneOne.getVector().compareTo(o.oneOne.getVector());
        if (result == 0) {
            result = oneTwo.getVector().compareTo(o.oneTwo.getVector());
            if (result == 0) {
                result = twoOne.getVector().compareTo(o.twoOne.getVector());
                if (result == 0) {
                    result = twoTwo.getVector().compareTo(o.twoTwo.getVector());
                }
            }
        }
        return result;
    }
}
