package org.bdd.twig.branch;

import org.bdd.twig.backend.TwigFormat;

public abstract class Branch
{
    private TwigFormat format;

    public void setFormat(String format)
    {
        this.format.setFormat(format);
    }

    public Branch(String format)
    {
        this.format = new TwigFormat(format);
    }

    public Branch()
    {
        this.format = new TwigFormat();
    }

    public TwigFormat getFormat()
    {
        return this.format;
    }

    public abstract void output(String msg);

    public abstract void flush();
}
