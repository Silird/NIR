package ru.SilirdCo.NIR.Main;

import ru.SilirdCo.NIR.Entities.Multioperation;

public class Main {
    public static void main(String[] args) {
        Multioperation multioperation = new Multioperation("123");
        System.out.println(multioperation.toStringFull());
        System.out.println(multioperation.toStringVector());
    }
}
