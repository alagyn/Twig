package org.bdd.twig.format;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bdd.twig.TwigConfig;
import org.bdd.twig.TwigConfig.Level;

public class TwigFormat
{
    public static final String DEFAULT_FORMAT = "[{event.level}] {event.message}\n";

    private static final String COLOR_BASE_TAG = "color", EVENT_BASE_TAG = "event";

    private static final String LEVEL_TAG = "level", MSG_TAG = "message", TIME_TAG = "time", NAME_TAG = "name";

    private static final String COLOR_END_TAG = "end", COLOR_RED_TAG = "red", COLOR_GRN_TAG = "green",
            COLOR_BLU_TAG = "blue", COLOR_YLW_TAG = "yellow", COLOR_CYAN_TAG = "cyan";

    private static final String COLOR_END = "\033[0m", COLOR_RED = "\033[31m", COLOR_GRN = "\033[32m",
            COLOR_BLU = "\033[34m", COLOR_YLW = "\033[33m", COLOR_CYAN = "\033[36m";

    private static class StringWrap
    {
        public String value = "";
        public final String name;

        public StringWrap(String name)
        {
            this.name = name;
        }
    }

    private static interface FmtObj
    {
        String get();
    }

    private static class FmtLiteral implements FmtObj
    {
        private final String lit;

        public FmtLiteral(String lit)
        {
            this.lit = lit;
        }

        @Override
        public String get()
        {
            return lit;
        }

        @Override
        public String toString()
        {
            return "FmtLiteral: \"" + get() + "\"";
        }
    }

    private static class FmtColor extends FmtLiteral
    {
        public FmtColor(String lit)
        {
            super(lit);
        }

        @Override
        public String toString()
        {
            return "FmtColor: \"" + get() + "color" + COLOR_END + "\"";
        }
    }

    private static class FmtWrap implements FmtObj
    {
        public final StringWrap wrapper;

        public FmtWrap(StringWrap wrap)
        {
            this.wrapper = wrap;
        }

        @Override
        public String get()
        {
            return wrapper.value;
        }

        @Override
        public String toString()
        {
            return "FmtWrap: " + wrapper.name;
        }
    }

    private static final String WRAP_MSG = "event.message", WRAP_NAME = "event.name", WRAP_LVL = "event.level",
            WRAP_LVL_C = "color.level", WRAP_TIME = "event.time";
    private StringWrap msgWrap = new StringWrap(WRAP_MSG);
    private StringWrap nameWrap = new StringWrap(WRAP_NAME);
    private StringWrap levelWrap = new StringWrap(WRAP_LVL);
    private StringWrap levelColorWrap = new StringWrap(WRAP_LVL_C);
    private StringWrap timeWrap = new StringWrap(WRAP_TIME);

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private ArrayList<FmtObj> format;

    public TwigFormat(String format)
    {
        compile(format);
    }

    public TwigFormat()
    {
        this(DEFAULT_FORMAT);
    }

    public void setFormat(String format)
    {
        compile(format);
    }

    public void setTimeFormat(String format)
    {
        timeFormatter = DateTimeFormatter.ofPattern(format);
    }

    public String format(String loggerName, Level level, OffsetDateTime now, String msg)
    {
        msgWrap.value = msg;
        nameWrap.value = loggerName;
        levelWrap.value = TwigConfig.getLevelStr(level);

        switch(level)
        {
        case Trace:
            levelColorWrap.value = COLOR_CYAN;
            break;
        case Debug:
            levelColorWrap.value = COLOR_GRN;
            break;
        case Warn:
            levelColorWrap.value = COLOR_YLW;
            break;
        case Error:
            levelColorWrap.value = COLOR_RED;
            break;
        case Info:
        default:
            levelColorWrap.value = "";
            break;
        }

        timeWrap.value = timeFormatter.format(now);

        StringBuilder out = new StringBuilder();
        for(FmtObj obj : format)
        {
            out.append(obj.get());
        }
        return out.toString();
    }

    private void compile(String formatString)
    {
        char[] rawfmt = formatString.toCharArray();

        format = new ArrayList<>();

        int startMark = 0;
        int endMark = 0;
        boolean readKey = false;

        for(int i = 0; i < rawfmt.length; ++i)
        {
            char c = rawfmt[i];
            switch(c)
            {
            case '{':
                if(readKey)
                {
                    throw new RuntimeException(
                            "Twig: Bad format \"" + formatString + "\": Missing closing brace at index " + i);
                }
                endMark = i;
                format.add(new FmtLiteral(formatString.substring(startMark, endMark)));
                startMark = i + 1;
                readKey = true;
                break;
            case '}':
                if(!readKey)
                {
                    throw new RuntimeException(
                            "Twig: Bad format \"" + formatString + "\": Missing opening brace at index " + startMark);
                }
                endMark = i;

                String rawkey = formatString.substring(startMark, endMark);

                String[] keys = rawkey.split("\\.", 2);
                if(keys.length != 2)
                {
                    throw new RuntimeException("Twig: Invalid format key: \"" + rawkey + "\"");
                }

                String base = keys[0];
                String key = keys[1];

                FmtObj obj = null;
                switch(base)
                {
                case EVENT_BASE_TAG:
                    obj = getEventObj(key);
                    break;
                case COLOR_BASE_TAG:
                    obj = getColorObj(key);
                    break;
                default:
                    obj = null;
                    break;
                }

                if(obj != null)
                {
                    format.add(obj);
                }

                readKey = false;
                startMark = i + 1;
                break;
            default:
                break;
            }
        }

        // If we are expecting a closing brace
        if(readKey)
        {
            throw new RuntimeException(
                    "Twig: Invalid format: \"" + formatString + "\": Missing closing brace at " + startMark);
        }

        // Get any remaining literal
        if(startMark != rawfmt.length)
        {
            format.add(new FmtLiteral(formatString.substring(startMark, rawfmt.length)));
        }

        format.trimToSize();
    }

    private FmtObj getEventObj(String key)
    {
        switch(key)
        {
        case NAME_TAG:
            return new FmtWrap(nameWrap);
        case LEVEL_TAG:
            return new FmtWrap(levelWrap);
        case TIME_TAG:
            return new FmtWrap(timeWrap);
        case MSG_TAG:
            return new FmtWrap(msgWrap);
        default:
            throw new RuntimeException("Twig: Unknown format key: \"" + key + "\"");
        }
    }

    private FmtObj getColorObj(String key)
    {
        switch(key)
        {
        case LEVEL_TAG:
            return new FmtWrap(levelColorWrap);
        case COLOR_END_TAG:
            return new FmtColor(COLOR_END);
        case COLOR_RED_TAG:
            return new FmtColor(COLOR_RED);
        case COLOR_BLU_TAG:
            return new FmtColor(COLOR_BLU);
        case COLOR_GRN_TAG:
            return new FmtColor(COLOR_GRN);
        case COLOR_YLW_TAG:
            return new FmtColor(COLOR_YLW);
        case COLOR_CYAN_TAG:
            return new FmtColor(COLOR_CYAN);
        default:
            throw new RuntimeException("Twig: Unknown color tag: \"" + key + "\"");
        }
    }
}
