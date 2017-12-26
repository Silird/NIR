package ru.SilirdCo.NIR.Tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.SilirdCo.NIR.Entities.Element;
import ru.SilirdCo.NIR.Entities.Multioperation;
import ru.SilirdCo.NIR.Util.MultioperationUtil;

public class MultioperationUtilTest {
    private final static Logger logger = LoggerFactory.getLogger(SetUtilTest.class);

    @BeforeClass
    public void start() {
        logger.info(" [#] Начало тестирования утилит мультиопераций");
    }

    @AfterClass
    public void end() {
        logger.info(" [#] Конец тестирования утилит мультиопераций");
    }

    @Test
    public void testFirstMegaoperation() {
        Assert.assertEquals(
                MultioperationUtil.getFirstMegaoperation(
                        new Multioperation(Element.EMPTY, Element.FULL, Element.FULL, Element.E2)),
                new Multioperation(Element.E2, Element.E1, Element.E2, Element.FULL));
    }

    @Test
    public void testSecondMegaoperation() {
        Assert.assertEquals(
                MultioperationUtil.getSecondMegaoperation(
                        new Multioperation(Element.EMPTY, Element.FULL, Element.FULL, Element.E2)),
                new Multioperation(Element.E2, Element.E2, Element.E1, Element.FULL));
    }

    @Test
    public void testSuperposition() {
        Assert.assertEquals(
                MultioperationUtil.getSuperposition(
                        new Multioperation(Element.FULL, Element.E1, Element.EMPTY, Element.E2),
                        new Multioperation(Element.EMPTY, Element.FULL, Element.E2, Element.FULL),
                        new Multioperation(Element.E1, Element.E2, Element.FULL, Element.E2)),
                new Multioperation(Element.EMPTY, Element.FULL, Element.E2, Element.FULL));
    }
}
