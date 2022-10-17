package org.bdd.twig.backend;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

public class TwigServiceProvider implements SLF4JServiceProvider
{
    private static TwigFactory factory;
    private IMarkerFactory markerFactory;
    private MDCAdapter mdcAdapter;

    @Override
    public void initialize()
    {
        factory = new TwigFactory();
        markerFactory = new BasicMarkerFactory();
        mdcAdapter = new BasicMDCAdapter();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                TwigWriter.flush();
            }
        });
    }

    @Override
    public ILoggerFactory getLoggerFactory()
    {
        return factory;
    }

    @Override
    public MDCAdapter getMDCAdapter()
    {
        return mdcAdapter;
    }

    @Override
    public IMarkerFactory getMarkerFactory()
    {
        return markerFactory;
    }

    @Override
    public String getRequestedApiVersion()
    {
        return "2.0.99";
    }
}
