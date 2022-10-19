package org.bdd.twig.backend;

import org.bdd.twig.Twig;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class TwigFactory implements ILoggerFactory
{

    public TwigFactory()
    {
    }

    @Override
    public Logger getLogger(String name)
    {
        for(String x : Twig.getBlocks())
        {
            if(name.startsWith(x))
            {
                return new TwigNoop(name);
            }
        }

        return new TwigLogger(name);
    }

}
