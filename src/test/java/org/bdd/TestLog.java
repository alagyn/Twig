package org.bdd;

import org.bdd.twig.Twig;
import org.bdd.twig.Twig.Level;
import org.bdd.twig.backend.TwigLogger;
import org.bdd.twig.branch.Branch;
import org.bdd.twig.branch.StreamBranch;
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
        Logger log = LoggerFactory.getLogger("testLogger");

        if(!log.getClass().getName().equals(TwigLogger.class.getName()))
        {
            throw new RuntimeException("Didn't load the TwigLogger, loaded " + log.getClass().getName());
        }

        log.info("This should not output");

        String format = "{event.time} [{color.level}{event.level}{color.end}] {event.name}: {event.message}\n";
        Branch b = new StreamBranch(System.out, format);

        Twig.addBranch(b);
        Twig.setLevel(Level.Trace);

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
    }
}
