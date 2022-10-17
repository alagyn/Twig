package org.bdd.twig.backend;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class TwigFactory implements ILoggerFactory
{

    @Override
    public Logger getLogger(String name)
    {
        return new TwigLogger(name);
    }

}
