package org.bdd;

import org.bdd.twig.TwigConfig;
import org.bdd.twig.TwigConfig.Level;
import org.bdd.twig.branch.Branch;
import org.bdd.twig.branch.StreamBranch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog
{
    public static void main(String[] args)
    {
        Logger log = LoggerFactory.getLogger("testLogger");

        if(!log.getClass().getName().equals("org.bdd.twig.Twig"))
        {
            throw new RuntimeException();
        }

        log.info("This should not output");

        String format = "{event.time} [{color.level}{event.level}{color.end}] {event.message}\n";
        Branch b = new StreamBranch(System.out, format);

        TwigConfig.addBranch(b);
        TwigConfig.setLevel(Level.Trace);

        log.trace("This is a trace");
        log.debug("This is a debug");
        log.info("This is an info");
        log.warn("This is a warn");
        log.error("This is an error");

    }
}
