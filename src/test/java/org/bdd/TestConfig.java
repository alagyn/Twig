package org.bdd;

import org.bdd.twig.Twig;
import org.bdd.twig.TwigConfig;
import org.bdd.twig.Twig.Level;
import org.bdd.twig.branch.Branch;
import org.bdd.twig.branch.StreamBranch;

public class TestConfig implements TwigConfig
{

    @Override
    public void config()
    {
        String format = "{event.time} [{color.level}{event.level}{color.end}] {event.name}: {event.message}\n";
        Branch b = new StreamBranch(System.out, format);

        Twig.addBranch(b);
        Twig.setLevel(Level.Trace);
        Twig.addBlock("blockedLog");
    }

}
