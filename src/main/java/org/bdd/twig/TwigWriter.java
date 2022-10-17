package org.bdd.twig;

import java.time.OffsetDateTime;

import org.bdd.twig.TwigConfig.Level;
import org.bdd.twig.branch.Branch;

public class TwigWriter
{

    public static void output(String name, Level level, String msg)
    {
        if(TwigConfig.checkLevel(level))
        {
            finalOutput(name, level, msg);
        }
    }

    public static void output(String name, Level level, String format, Object arg0)
    {
        if(TwigConfig.checkLevel(level))
        {
            // TODO format
            String formatted = format;

            finalOutput(name, level, formatted);
        }
    }

    public static void output(String name, Level level, String format, Object arg0, Object arg1)
    {
        if(TwigConfig.checkLevel(level))
        {
            // TODO format
            String formatted = format;

            finalOutput(name, level, formatted);
        }
    }

    public static void output(String name, Level level, String format, Object[] objs)
    {
        if(TwigConfig.checkLevel(level))
        {
            // TODO format
            String formatted = format;

            finalOutput(name, level, formatted);
        }
    }

    public static void output(String name, Level level, String format, Throwable arg0)
    {
        if(TwigConfig.checkLevel(level))
        {
            // TODO format
            String formatted = format;

            finalOutput(name, level, formatted);
        }
    }

    /**
     * Formats the message and sends it to each branch.
     * Does not check if the log level is enabled
     * @param name The logger's name
     * @param level The log level
     * @param msg The SLF4J formatted message
     */
    private static void finalOutput(String name, Level level, String msg)
    {
        OffsetDateTime now = OffsetDateTime.now();

        for(Branch branch : TwigConfig.getBranches())
        {
            String formatted = branch.getFormat().format(name, level, now, msg);
            synchronized(branch)
            {
                branch.output(formatted);
            }
        }
    }

    protected static void flush()
    {
        for(Branch branch : TwigConfig.getBranches())
        {
            synchronized(branch)
            {
                branch.flush();
            }
        }
    }
}
