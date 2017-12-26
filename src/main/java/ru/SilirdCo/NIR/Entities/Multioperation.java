package ru.SilirdCo.NIR.Entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Multioperation {
    private final static Logger logger = LoggerFactory.getLogger(Multioperation.class);

    public final static Multioperation EMPTY = new Multioperation(
            Element.EMPTY, Element.EMPTY, Element.EMPTY, Element.EMPTY);
    public final static Multioperation FULL = new Multioperation(
            Element.FULL, Element.FULL, Element.FULL, Element.FULL);
    public final static Multioperation E11 = new Multioperation(
            Element.E1, Element.E1, Element.E2, Element.E2);
    public final static Multioperation E22 = new Multioperation(
            Element.E1, Element.E2, Element.E1, Element.E2);

    private final Element oneOne;
    private final Element oneTwo;
    private final Element twoOne;
    private final Element twoTwo;

    public Multioperation(Element oneOne, Element oneTwo, Element twoOne, Element twoTwo) {
        this.oneOne = oneOne;
        this.oneTwo = oneTwo;
        this.twoOne = twoOne;
        this.twoTwo = twoTwo;
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

    @Override
    public String toString() {
        return "(" + oneOne.getFullRecord() + ", " + oneTwo.getFullRecord() + ", " +
                twoOne.getFullRecord() + ", " + twoTwo.getFullRecord() + ")";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if((object == null) || (getClass()!= object.getClass())) return  false;

        Multioperation other = (Multioperation) object;
        if ((this.oneOne == null) && (other.oneOne != null)) return false;
        if  ((this.oneOne != null) && (!this.oneOne.equals(other.oneOne))) return false;

        if ((this.oneTwo == null) && (other.oneTwo != null)) return false;
        if  ((this.oneTwo != null) && (!this.oneTwo.equals(other.oneTwo))) return false;

        if ((this.twoOne == null) && (other.twoOne != null)) return false;
        if  ((this.twoOne != null) && (!this.twoOne.equals(other.twoOne))) return false;

        if ((this.twoTwo == null) && (other.twoTwo != null)) return false;
        if  ((this.twoTwo != null) && (!this.twoTwo.equals(other.twoTwo))) return false;

        return true;
    }
}
