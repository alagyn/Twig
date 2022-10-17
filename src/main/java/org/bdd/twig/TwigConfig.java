package org.bdd.twig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bdd.twig.branch.Branch;

public class TwigConfig
{
    public static enum Level
    {
        Trace, Debug, Info, Warn, Error;
    }

    private static Level loglevel = Level.Info;
    private static ArrayList<Branch> branches = new ArrayList<>();

    public static Level getLevel()
    {
        return loglevel;
    }

    public static void setLevel(Level level)
    {
        loglevel = level;
    }

    public static String getLevelStr(Level level)
    {
        switch(level)
        {
        case Info:
            return "Info ";
        case Warn:
            return "Warn ";
        default:
            return level.toString();
        }
    }

    private TwigConfig()
    {

    }

    public static void addBranch(Branch... branch)
    {
        branches.addAll(Arrays.asList(branch));
    }

    public static boolean checkLevel(Level l)
    {
        return l.compareTo(loglevel) >= 0;
    }

    public static List<Branch> getBranches()
    {
        return branches;
    }

}
