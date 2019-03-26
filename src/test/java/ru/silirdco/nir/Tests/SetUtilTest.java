package ru.silirdco.nir.Tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.silirdco.nir.core.entities.Element;
import ru.silirdco.nir.core.util.SetUtil;

public class SetUtilTest {
    private final static Logger logger = LoggerFactory.getLogger(SetUtilTest.class);

    @BeforeClass
    public void start() {
        logger.info(" [#] Начало тестирования утилит множеств");
    }

    @AfterClass
    public void end() {
        logger.info(" [#] Конец тестирования утилит множеств");
    }

    @Test
    public void testEmpty() {
        Assert.assertEquals(SetUtil.union(Element.EMPTY, Element.EMPTY), Element.EMPTY);
    }

    @Test
    public void testWithEmpty1() {
        Assert.assertEquals(SetUtil.union(Element.EMPTY, Element.E1), Element.E1);
    }

    @Test
    public void testWithEmpty2() {
        Assert.assertEquals(SetUtil.union(Element.EMPTY, Element.E2), Element.E2);
    }

    @Test
    public void testWithEmptyFull() {
        Assert.assertEquals(SetUtil.union(Element.EMPTY, Element.FULL), Element.FULL);
    }

    @Test
    public void testFull() {
        Assert.assertEquals(SetUtil.union(Element.FULL, Element.FULL), Element.FULL);
    }

    @Test
    public void testFull1() {
        Assert.assertEquals(SetUtil.union(Element.FULL, Element.E1), Element.FULL);
    }

    @Test
    public void testFull2() {
        Assert.assertEquals(SetUtil.union(Element.FULL, Element.E2), Element.FULL);
    }
}
