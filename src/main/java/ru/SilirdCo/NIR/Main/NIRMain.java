package ru.SilirdCo.NIR.Main;

import ru.SilirdCo.NIR.Entities.Element;
import ru.SilirdCo.NIR.Entities.Multioperation;
import ru.SilirdCo.NIR.Util.EnumerationUtil;
import ru.SilirdCo.NIR.Util.WriteUtil;

import java.util.ArrayList;
import java.util.List;

public class NIRMain {
    public static void main(String[] args) {
        List<Element> list = new ArrayList<>();
        list.add(Element.EMPTY);
        list.add(Element.E1);
        list.add(Element.E2);
        list.add(Element.FULL);

        class Count {
            public int count;
        }

        List<Multioperation> multioperations = new ArrayList<>();

        Count count = new Count();
        count.count = 0;


        EnumerationUtil.enumerate(4, list, elements -> {
            Multioperation multioperation = new Multioperation(
                    elements.get(0),
                    elements.get(1),
                    elements.get(2),
                    elements.get(3)
            );
            multioperations.add(multioperation);
        });


        List<List<Multioperation>> printList = new ArrayList<>();
        EnumerationUtil.enumerate(2, multioperations, printList::add);

        WriteUtil.print(printList);
    }
}
