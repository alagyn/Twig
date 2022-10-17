# Twig
A tiny logging backend for SLF4J

This project came about simply to spite the terribly over-complicated logging libraries like Log4J
logback, or whatever floats your goat.

Stuff Twig Does:
* Consistent, no frills format strings
* Programatic configuration. You want a config file? Do it yourself
* COLORS
* Multiple output-streams

Stuff Twig Doesn't Do:
* Make your brain hurt
* Complain


### Basic Setup
```java
import org.bdd.twig.TwigConfig;
import org.bdd.twig.TwigConfig.Level;
import org.bdd.twig.branch.Branch;
import org.bdd.twig.branch.StreamBranch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog
{
    public static void main(String[] args)
    {
        // Get a logger with this name
        Logger log = LoggerFactory.getLogger("testLogger");

        log.info("This should not output, no branches have been added");

        // Setup a custom format
        String format = "{event.time} [{color.level}{event.level}{color.end}] {event.message}\n";
        // Create a branch
        Branch b = new StreamBranch(System.out, format);
        // Add it to the TwigConfig
        TwigConfig.addBranch(b);
        // Set the global level
        TwigConfig.setLevel(Level.Trace);

        log.trace("This is a trace");
        log.debug("This is a debug");
        log.info("This is an info");
        log.warn("This is a warn");
        log.error("This is an error");
    }
}
```

Better docs to come soon, lol
