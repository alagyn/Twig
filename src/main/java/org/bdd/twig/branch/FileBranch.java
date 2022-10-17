package org.bdd.twig.branch;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileBranch extends StreamBranch
{
    public FileBranch(String filename, boolean append) throws FileNotFoundException
    {
        super();
        FileOutputStream stream = new FileOutputStream(filename, append);
        setStream(stream);
    }

    public FileBranch(String filename, boolean append, String format) throws FileNotFoundException
    {
        super(format);
        FileOutputStream stream = new FileOutputStream(filename, append);
        setStream(stream);
    }
}
