package org.bdd;

import org.bdd.twig.backend.TwigLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog
{
    private static class TestObj
    {
        public final String msg;

        public TestObj(String msg)
        {
            this.msg = msg;
        }

        public static void fail()
        {
            throw new RuntimeException("this is a potato message");
        }

        @Override
        public String toString()
        {
            return this.msg;
        }
    }

    public static void main(String[] args)
    {
        Logger log = LoggerFactory.getLogger(TestLog.class.getName());

        if(!log.getClass().getName().equals(TwigLogger.class.getName()))
        {
            throw new RuntimeException("Didn't load the TwigLogger, loaded " + log.getClass().getName());
        }

        log.trace("This is a trace");
        log.debug("This is a debug");
        log.info("This is an info");
        log.warn("This is a warn");
        log.error("This is an error");

        log.info("This is an {}", "Insert");
        log.info("This is not {} {}", new TestObj("looking"), new TestObj("bad"));
        log.info("This is {} {} {} {}", new TestObj("many"), new TestObj("things"), new TestObj("at"),
                new TestObj("once"));

        log.info("This is {} {} {} end", new TestObj("many"), new TestObj("things"), new TestObj("at"),
                new TestObj("once"));

        log.info("This is {} {} {} {}", new TestObj("many"), new TestObj("things"), new TestObj("at"));

        log.error("This is a throwable", new RuntimeException("This is a message"));

        try
        {
            TestObj.fail();
        }
        catch(RuntimeException err)
        {
            log.warn("What's all this then", err);
        }

        Logger blocked = LoggerFactory.getLogger("blockedLog.asdf.Classname");
        blocked.info("This should not print");
        log.info("This should be fine");
        blocked.warn("This, however, will print");

        log.info("Hmmmmmm");

        Logger longer = LoggerFactory.getLogger("org.bdd.ThisIsAReallyLongClass");
        longer.info("Hey look at me");

        log.info("Wow everything is different now");
        log.warn("Wait how do we go back");
        longer.debug("You can't");
    }
}
