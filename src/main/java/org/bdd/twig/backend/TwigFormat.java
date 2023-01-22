package org.bdd.twig.backend;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bdd.twig.Twig;
import org.bdd.twig.Twig.Level;

public class TwigFormat
{
    public static final String DEFAULT_FORMAT = "[{event.level}] {event.message}\n";
    public static final String DEFAULT_TIME_FORMAT = "dd-MM-yyyy kk:mm:ss";

    private static final String COLOR_BASE_TAG = "color", EVENT_BASE_TAG = "event";

    private static final String LEVEL_TAG = "level", MSG_TAG = "message", TIME_TAG = "time", NAME_TAG = "name";

    private static final String COLOR_END_TAG = "end", COLOR_RED_TAG = "red", COLOR_GRN_TAG = "green",
            COLOR_BLU_TAG = "blue", COLOR_YLW_TAG = "yellow", COLOR_CYAN_TAG = "cyan";

    private static final String COLOR_END = "\033[0m", COLOR_RED = "\033[31m", COLOR_GRN = "\033[32m",
            COLOR_BLU = "\033[34m", COLOR_YLW = "\033[33m", COLOR_CYAN = "\033[36m";

    private static int longestName = 0;

    public static void updateLongestName(int len)
    {
        longestName = Math.min(Math.max(len, longestName), Twig.getMaxNameLen());
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
        public String value = "";

        public FmtWrap()
        {
        }

        @Override
        public String get()
        {
            return value;
        }

        @Override
        public String toString()
        {
            return "FmtWrap";
        }
    }

    private FmtWrap msgWrap = new FmtWrap();
    private FmtWrap nameWrap = null;
    private FmtWrap levelWrap = null;
    private FmtWrap levelColorWrap = null;
    private FmtWrap timeWrap = null;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);

    private ArrayList<FmtObj> format;
    private String formatString;

    public TwigFormat(String format)
    {
        formatString = format;
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

    private static String alignCenter(String in)
    {
        int diff = longestName - in.length();
        if(diff <= 0)
        {
            return in;
        }

        StringBuilder out = new StringBuilder();

        int leftPad = (longestName - in.length()) / 2;
        for(int i = 0; i < leftPad; ++i)
        {
            out.append(' ');
        }

        out.append(in);

        int rightPad = leftPad + (diff % 2);
        for(int i = 0; i < rightPad; ++i)
        {
            out.append(' ');
        }

        return out.toString();
    }

    private static String alignLeft(String in)
    {
        int diff = longestName - in.length();
        if(diff <= 0)
        {
            return in;
        }

        StringBuilder out = new StringBuilder();

        out.append(in);

        for(int i = 0; i < diff; ++i)
        {
            out.append(' ');
        }

        return out.toString();
    }

    private static String alignRight(String in)
    {
        int diff = longestName - in.length();
        if(diff == 0)
        {
            return in;
        }

        StringBuilder out = new StringBuilder();

        for(int i = 0; i < diff; ++i)
        {
            out.append(' ');
        }

        return out.append(in).toString();
    }

    public String format(String loggerName, Level level, OffsetDateTime now, String msg)
    {
        msgWrap.value = msg;

        if(nameWrap != null)
        {
            switch(Twig.getNameAlign())
            {
            case Left:
                nameWrap.value = alignLeft(loggerName);
                break;
            case Right:
                nameWrap.value = alignRight(loggerName);
                break;
            case Center:
                nameWrap.value = alignCenter(loggerName);
                break;
            case None:
            default:
                nameWrap.value = loggerName;
                break;
            }

        }

        if(levelWrap != null)
        {
            levelWrap.value = Twig.getLevelStr(level);
        }

        if(levelColorWrap != null)
        {
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
        }

        if(timeWrap != null)
        {
            timeWrap.value = timeFormatter.format(now);
        }

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
            if(nameWrap == null)
            {
                nameWrap = new FmtWrap();
            }
            return nameWrap;

        case LEVEL_TAG:
            if(levelWrap == null)
            {
                levelWrap = new FmtWrap();
            }
            return levelWrap;
        case TIME_TAG:
            if(timeWrap == null)
            {
                timeWrap = new FmtWrap();
            }
            return timeWrap;
        case MSG_TAG:
            return msgWrap;
        default:
            throw new RuntimeException("Twig: Unknown format key: \"" + key + "\"");
        }
    }

    private FmtObj getColorObj(String key)
    {
        switch(key)
        {
        case LEVEL_TAG:
            if(levelColorWrap == null)
            {
                levelColorWrap = new FmtWrap();
            }
            return levelColorWrap;
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

    public String toString()
    {
        return new StringBuilder().append("TwigFormat: \"").append(formatString).append("\"").toString();
    }
}
