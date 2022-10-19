package org.bdd.twig.branch;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StreamBranch extends Branch
{
    private OutputStreamWriter os;

    public StreamBranch(OutputStream stream)
    {
        os = new OutputStreamWriter(stream);
    }

    public StreamBranch(OutputStream stream, String format)
    {
        super(format);
        os = new OutputStreamWriter(stream);
    }

    protected StreamBranch()
    {
        os = null;
    }

    protected StreamBranch(String format)
    {
        super(format);
        os = null;
    }

    protected void setStream(OutputStream stream)
    {
        os = new OutputStreamWriter(stream);
    }

    @Override
    public void output(String msg)
    {
        try
        {
            os.write(msg);
            os.flush();
        }
        catch(IOException err)
        {
            System.err.println("Twig Logging Error: " + err.getMessage());
        }

    }

    @Override
    public void flush()
    {
        try
        {
            os.flush();
        }
        catch(IOException e)
        {
            System.err.println("Twig Logging Error: " + e.getMessage());
        }
    }

}
