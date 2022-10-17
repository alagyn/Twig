package org.bdd.twig;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class TwigFactory implements ILoggerFactory
{

    @Override
    public Logger getLogger(String name)
    {
        return new Twig(name);
    }

}
