package org.bdd.twig;

import org.bdd.twig.TwigConfig.Level;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class Twig implements Logger
{
    public final String name;

    public Twig(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    // TODO markers?

    private void log(Level level, String format, Object[] objs)
    {
        TwigWriter.output(name, level, format, objs);
    }

    private void log(Level level, String format, Object arg0, Object arg1)
    {
        TwigWriter.output(name, level, format, arg0, arg1);
    }

    private void log(Level level, String format, Object arg0)
    {
        TwigWriter.output(name, level, format, arg0);
    }

    private void log(Level level, String format, Throwable arg0)
    {
        TwigWriter.output(name, level, format, arg0);
    }

    private void log(Level level, String msg)
    {
        TwigWriter.output(name, level, msg);
    }

    // #region Debug
    @Override
    public void debug(String arg0)
    {
        log(Level.Debug, arg0);
    }

    @Override
    public void debug(String arg0, Object arg1)
    {
        log(Level.Debug, arg0, arg1);
    }

    @Override
    public void debug(String arg0, Object... arg1)
    {
        log(Level.Debug, arg0, arg1);
    }

    @Override
    public void debug(String arg0, Throwable arg1)
    {
        log(Level.Debug, arg0, arg1);
    }

    @Override
    public void debug(Marker arg0, String arg1)
    {
        log(Level.Debug, arg1);
    }

    @Override
    public void debug(String arg0, Object arg1, Object arg2)
    {
        log(Level.Debug, arg0, arg1, arg2);
    }

    @Override
    public void debug(Marker arg0, String arg1, Object arg2)
    {
        log(Level.Debug, arg1, arg2);
    }

    @Override
    public void debug(Marker arg0, String arg1, Object... arg2)
    {
        log(Level.Debug, arg1, arg2);
    }

    @Override
    public void debug(Marker arg0, String arg1, Throwable arg2)
    {
        log(Level.Debug, arg1, arg2);
    }

    @Override
    public void debug(Marker arg0, String arg1, Object arg2, Object arg3)
    {
        log(Level.Debug, arg1, arg2, arg3);
    }

    @Override
    public boolean isDebugEnabled()
    {
        return TwigConfig.checkLevel(Level.Debug);
    }

    @Override
    public boolean isDebugEnabled(Marker arg0)
    {
        return TwigConfig.checkLevel(Level.Debug);
    }
    // #endregion

    // #region Error
    @Override
    public void error(String arg0)
    {
        log(Level.Error, arg0);
    }

    @Override
    public void error(String arg0, Object arg1)
    {
        log(Level.Error, arg0, arg1);
    }

    @Override
    public void error(String arg0, Object... arg1)
    {
        log(Level.Error, arg0, arg1);
    }

    @Override
    public void error(String arg0, Throwable arg1)
    {
        log(Level.Error, arg0, arg1);
    }

    @Override
    public void error(Marker arg0, String arg1)
    {
        log(Level.Error, arg1);
    }

    @Override
    public void error(String arg0, Object arg1, Object arg2)
    {
        log(Level.Error, arg0, arg1, arg2);
    }

    @Override
    public void error(Marker arg0, String arg1, Object arg2)
    {
        log(Level.Error, arg1, arg2);
    }

    @Override
    public void error(Marker arg0, String arg1, Object... arg2)
    {
        log(Level.Error, arg1, arg2);
    }

    @Override
    public void error(Marker arg0, String arg1, Throwable arg2)
    {
        log(Level.Error, arg1, arg2);
    }

    @Override
    public void error(Marker arg0, String arg1, Object arg2, Object arg3)
    {
        log(Level.Error, arg1, arg2, arg3);
    }

    @Override
    public boolean isErrorEnabled()
    {
        return TwigConfig.checkLevel(Level.Error);
    }

    @Override
    public boolean isErrorEnabled(Marker arg0)
    {
        return TwigConfig.checkLevel(Level.Error);
    }
    // #endregion Error

    // #region Info
    @Override
    public void info(String arg0)
    {
        log(Level.Info, arg0);
    }

    @Override
    public void info(String arg0, Object arg1)
    {
        log(Level.Info, arg0, arg1);
    }

    @Override
    public void info(String arg0, Object... arg1)
    {
        log(Level.Info, arg0, arg1);
    }

    @Override
    public void info(String arg0, Throwable arg1)
    {
        log(Level.Info, arg0, arg1);
    }

    @Override
    public void info(Marker arg0, String arg1)
    {
        log(Level.Info, arg1);
    }

    @Override
    public void info(String arg0, Object arg1, Object arg2)
    {
        log(Level.Info, arg0, arg1, arg2);
    }

    @Override
    public void info(Marker arg0, String arg1, Object arg2)
    {
        log(Level.Info, arg1, arg2);
    }

    @Override
    public void info(Marker arg0, String arg1, Object... arg2)
    {
        log(Level.Info, arg1, arg2);
    }

    @Override
    public void info(Marker arg0, String arg1, Throwable arg2)
    {
        log(Level.Info, arg1, arg2);
    }

    @Override
    public void info(Marker arg0, String arg1, Object arg2, Object arg3)
    {
        log(Level.Info, arg1, arg2, arg3);
    }

    @Override
    public boolean isInfoEnabled()
    {
        return TwigConfig.checkLevel(Level.Info);
    }

    @Override
    public boolean isInfoEnabled(Marker arg0)
    {
        return TwigConfig.checkLevel(Level.Info);
    }
    // #endregion Info

    // #region Trace
    @Override
    public void trace(String arg0)
    {
        log(Level.Trace, arg0);
    }

    @Override
    public void trace(String arg0, Object arg1)
    {
        log(Level.Trace, arg0, arg1);
    }

    @Override
    public void trace(String arg0, Object... arg1)
    {
        log(Level.Trace, arg0, arg1);
    }

    @Override
    public void trace(String arg0, Throwable arg1)
    {
        log(Level.Trace, arg0, arg1);
    }

    @Override
    public void trace(Marker arg0, String arg1)
    {
        log(Level.Trace, arg1);
    }

    @Override
    public void trace(String arg0, Object arg1, Object arg2)
    {
        log(Level.Trace, arg0, arg1, arg2);
    }

    @Override
    public void trace(Marker arg0, String arg1, Object arg2)
    {
        log(Level.Trace, arg1, arg2);
    }

    @Override
    public void trace(Marker arg0, String arg1, Object... arg2)
    {
        log(Level.Trace, arg1, arg2);
    }

    @Override
    public void trace(Marker arg0, String arg1, Throwable arg2)
    {
        log(Level.Trace, arg1, arg2);
    }

    @Override
    public void trace(Marker arg0, String arg1, Object arg2, Object arg3)
    {
        log(Level.Trace, arg1, arg2, arg3);
    }

    @Override
    public boolean isTraceEnabled()
    {
        return TwigConfig.checkLevel(Level.Trace);
    }

    @Override
    public boolean isTraceEnabled(Marker arg0)
    {
        return TwigConfig.checkLevel(Level.Trace);
    }

    // #endregion Trace

    // #region Warn
    @Override
    public void warn(String arg0)
    {
        log(Level.Warn, arg0);
    }

    @Override
    public void warn(String arg0, Object arg1)
    {
        log(Level.Warn, arg0, arg1);
    }

    @Override
    public void warn(String arg0, Object... arg1)
    {
        log(Level.Warn, arg0, arg1);
    }

    @Override
    public void warn(String arg0, Throwable arg1)
    {
        log(Level.Warn, arg0, arg1);
    }

    @Override
    public void warn(Marker arg0, String arg1)
    {
        log(Level.Warn, arg1);
    }

    @Override
    public void warn(String arg0, Object arg1, Object arg2)
    {
        log(Level.Warn, arg0, arg1, arg2);
    }

    @Override
    public void warn(Marker arg0, String arg1, Object arg2)
    {
        log(Level.Warn, arg1, arg2);
    }

    @Override
    public void warn(Marker arg0, String arg1, Object... arg2)
    {
        log(Level.Warn, arg1, arg2);
    }

    @Override
    public void warn(Marker arg0, String arg1, Throwable arg2)
    {
        log(Level.Warn, arg1, arg2);
    }

    @Override
    public void warn(Marker arg0, String arg1, Object arg2, Object arg3)
    {
        log(Level.Warn, arg1, arg2, arg3);
    }

    @Override
    public boolean isWarnEnabled()
    {
        return TwigConfig.checkLevel(Level.Warn);
    }

    @Override
    public boolean isWarnEnabled(Marker arg0)
    {
        return TwigConfig.checkLevel(Level.Warn);
    }
    // #endregion Warn
}
