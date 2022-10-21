package org.bdd.twig.backend;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;

import org.bdd.twig.Twig;
import org.bdd.twig.Twig.Level;
import org.bdd.twig.branch.Branch;

public class TwigWriter
{
    public static void output(String name, Level level, String msg)
    {
        if(Twig.checkLevel(level))
        {
            finalOutput(name, level, msg);
        }
    }

    public static void output(String name, Level level, String format, Object arg0)
    {
        if(Twig.checkLevel(level))
        {
            String[] values = format.split("\\{\\}", 2);
            if(values.length == 1)
            {
                finalOutput(name, level, format);
                return;
            }

            StringBuilder out = new StringBuilder();
            out.append(values[0]);
            if(arg0 == null)
            {
                out.append("null");
            }
            else
            {
                out.append(arg0.toString());
            }
            out.append(values[1]);

            finalOutput(name, level, out.toString());
        }
    }

    public static void output(String name, Level level, String format, Object arg0, Object arg1)
    {
        if(Twig.checkLevel(level))
        {
            String[] values = format.split("\\{\\}", 3);

            if(values.length == 1)
            {
                finalOutput(name, level, format);
                return;
            }

            StringBuilder out = new StringBuilder();
            out.append(values[0]);
            if(arg0 == null)
            {
                out.append("null");
            }
            else
            {
                out.append(arg0.toString());
            }

            out.append(values[1]);

            if(values.length == 3)
            {
                if(arg1 == null)
                {
                    out.append("null");
                }
                else
                {
                    out.append(arg1.toString());
                }

                out.append(values[2]);
            }

            finalOutput(name, level, out.toString());
        }
    }

    /**
     * Outputs a variable number of objects according to the format
     * Silently ignores if objs.length != number of format anchors
     */
    public static void output(String name, Level level, String format, Object[] objs)
    {
        if(Twig.checkLevel(level))
        {
            char[] rawfmt = format.toCharArray();
            StringBuilder out = new StringBuilder();
            int startMark = 0, endMark = 0;
            int objIdx = 0;
            for(int i = 0; i < rawfmt.length; ++i)
            {
                char c = rawfmt[i];
                if(c == '{' && i < rawfmt.length - 1 && rawfmt[i + 1] == '}')
                {
                    endMark = i;
                    out.append(rawfmt, startMark, endMark - startMark);
                    if(objs[objIdx] == null)
                    {
                        out.append("null");
                    }
                    else
                    {
                        out.append(objs[objIdx].toString());
                    }
                    startMark = i + 2;

                    ++objIdx;
                    if(objIdx >= objs.length)
                    {
                        out.append(rawfmt, startMark, rawfmt.length - startMark);
                        startMark = rawfmt.length;
                        break;
                    }
                }
            }

            if(startMark < rawfmt.length)
            {
                out.append(rawfmt, startMark, rawfmt.length - startMark);
            }

            finalOutput(name, level, out.toString());
        }
    }

    public static void output(String name, Level level, String format, Throwable arg0)
    {
        if(Twig.checkLevel(level))
        {
            StringBuilder out = new StringBuilder();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            arg0.printStackTrace(pw);
            out.append(format).append("\n");
            out.append(sw.toString());
            out.append(arg0.getMessage());

            finalOutput(name, level, out.toString());
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

        for(Branch branch : Twig.getBranches())
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
        for(Branch branch : Twig.getBranches())
        {
            synchronized(branch)
            {
                branch.flush();
            }
        }
    }
}
