package ru.SilirdCo.NIR.Main;

import ru.SilirdCo.NIR.Entities.Multioperation;
import ru.SilirdCo.NIR.Util.MultioperationUtil;

public class Main {
    public static void main(String[] args) {
        Multioperation multioperation1 = new Multioperation("3023");
        Multioperation multioperation2 = new Multioperation("2301");
        Multioperation multioperation = MultioperationUtil.getSubstitution(multioperation1, multioperation2);
        System.out.println(multioperation.toStringVector());
    }
}
