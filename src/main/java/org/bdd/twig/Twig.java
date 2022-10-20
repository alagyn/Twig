package org.bdd.twig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bdd.twig.branch.Branch;

public class Twig
{
    public static enum Level
    {
        Trace, Debug, Info, Warn, Error;
    }

    private static Level loglevel = Level.Info;
    private static ArrayList<Branch> branches = new ArrayList<>();
    private static ArrayList<String> blocked = new ArrayList<>();

    public static Level getLevel()
    {
        return loglevel;
    }

    public static void setLevel(Level level)
    {
        loglevel = level;
    }

    public static void setLevel(String level)
    {
        switch(level.toLowerCase())
        {
        case "trace":
            setLevel(Level.Trace);
            break;
        case "debug":
            setLevel(Level.Debug);
            break;
        case "info":
            setLevel(Level.Info);
            break;
        case "warn":
            setLevel(Level.Warn);
            break;
        case "error":
            setLevel(Level.Error);
            break;
        default:
            throw new IllegalArgumentException("Twig::setLevel() Invalid level string: \"" + level + "\"");
        }
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

    private Twig()
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

    public static void addBlock(String name)
    {
        blocked.add(name);
    }

    public static List<String> getBlocks()
    {
        return blocked;
    }

}
