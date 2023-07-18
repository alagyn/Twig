<img alt="Twig Logo" src="docs/TwigLogoCircleCleared.png" height="200" align="right"/>

A tiny logging backend for [SLF4J](https://www.slf4j.org/manual.html)

This project came about simply to spite the terribly over-complicated logging libraries like Log4J,
logback, or whatever floats your goat.

Stuff Twig Does:
* Consistent, no frills format strings
* Programatic configuration. You want a config file? Do it yourself
* COLORS
* Multiple output-streams

Stuff Twig Doesn't Do:
* Give you up
* Let you down
* Run around
* Desert you

---

## Basic Setup
```java
import org.bdd.twig.Twig;
import org.bdd.twig.Twig.Level;
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
        String format = "{event.time} [{color.level}{event.level}{color.end}] {event.name}: {event.message}\n";
        // Create a branch
        Branch b = new StreamBranch(System.out, format);
        // Add it to the TwigConfig
        Twig.addBranch(b);
        // Set the global level
        Twig.setLevel(Level.Trace);

        log.trace("This is a trace");
        log.debug("This is a debug");
        log.info("This is an info");
        log.warn("This is a warn");
        log.error("This is an error");
    }
}
```

In the simplest case, you can setup Twig in your main function.
However, things get sticky quickly when you start having loggers that are static variables, 
since we cannot guarantee when these will be instantiated (most likely *before* main executes).

Therefore, the optimal solution is to create a TwigConfig Service Provider.
This is done in two steps:
1. Create a class that implements the `org.bdd.twig.TwigConfig` interface.
    This interface provides a single function `void config()` that is guaranteed to be called
    before any loggers are created.
    ```java
    public class TestConfig implements TwigConfig
    {
        @Override
        public void config()
        {
            String format = "{event.time} [{color.level}{event.level}{color.end}] {event.name}: {event.message}\n";
            Branch b = new StreamBranch(System.out, format);

            Twig.addBranch(b);
            Twig.setLevel(Level.Trace);
        }
    }
    ```

2. Add a "provider-configuration" file (which sounds more complicated than it is):
    1. Assuming a standard java folder structure, create folders `src/main/resources/META-INF/services`
    2. Create a file in this folder named `org.bdd.twig.TwigConfig`
    3. In this file, add the fully qualified classname of your TwigConfig implementation.
        E.G. `org.yourgroup.yourpackage.YourConfig`

For more information, look at [the Service Loader JavaDocs](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html).

---

## Twig Configuratoring

It is important to note that Twig consists of a **single root logger, with a single logging level**.
Formatting, however, is typically handled on a per-branch basis so that you can have a separate formats for different logging outputs.

### Format Strings

Twig format string variables are surrounded with curly brackets and contain a category and a key.  
 E.G. `"{event.message}"`  category = "event", key = "message"  
 Curly brackets are therefore **invalid** in any other case.

### Available format variables

`event`: Logging event details
* `message`: The SLF4J formatted message. See [the SLF4J docs](https://www.slf4j.org/manual.html#typical_usage)
* `time`: The formatted time string (see below)
* `level`: The log level string, formatted to 5 char width
* `name`: The logger's name

`color`: COLORS
* `red` `green` `blue` `yellow` `cyan`: Start coloring output
* `end`: Stop coloring
* `level`: Start coloring according to the log level
    * `Trace`: `cyan`
    * `Debug`: `green`
    * `Info`: None
    * `Warn`: `yellow`
    * `Error`: `red`

### Timestamp Formatting
Twig timestamps are formatted per branch and accept `java.time.format.DateTimeFormatter` format strings.

```java
Branch b = new StreamBranch(System.out, "{event.time} {event.message}\n");
b.setTimeFormat("dd-MM-yyyy kk:mm:ss");
Twig.addBranch(b);
```

### Logger Name Formatting (Global Config)
The typical paradigm for Java logger names is to use the full class name.  
E.G. `org.bdd.testPackage.TestClass` via
```java
Logger log = LoggerFactory.getLogger(TestClass.getClass().getName());
```

Twig offers three methods of presenting these names in your logs (with `{event.name}`)
Set the mode with:
```java
Twig.setNameMode(NameMode.ShortPath)
```
This defaults to the `ClassName` mode.

* `ClassName`: Print only the classname  
 E.G. `TestClass`
* `ShortPath`: Shorten each package name to its first letter  
 E.G. `o.b.t.TestClass`
* `None`: User the full name as-is  
E.G. `org.bdd.twig.TestClass`

You can also configure the alignment of logger names with:
```java
Twig.setNameAlign(NameAlign.Center);
```
Names will be padded to fit in the space taken by the longest logger name that has been created.
This defaults to the `Center` mode.

Options: `Left`, `Right`, `Center`, `None`  
Setting the alignment to `None` disables padding

### Blocking loggers
Sometimes you only want to see your own logs. Add a log block to stop all logs of level Info or below.
```java
Twig.addBlock("org.otherPackage");
```

This will stop all loggers with names that start with the passed string.
