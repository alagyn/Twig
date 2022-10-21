package org.bdd.twig.backend;

import org.bdd.twig.Twig;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class TwigFactory implements ILoggerFactory
{

    public TwigFactory()
    {
    }

    @Override
    public Logger getLogger(String name)
    {
        for(String x : Twig.getBlocks())
        {
            if(name.startsWith(x))
            {
                return new TwigNoop(name);
            }
        }

        String n = "ERROR";
        switch(Twig.getNameMode())
        {
        case ClassName:
        {
            int i = name.lastIndexOf('.');
            if(i >= 0 && i < name.length() - 1)
            {
                n = name.substring(i + 1);
            }
            else
            {
                n = name;
            }

            break;
        }
        case ShortPath:
        {
            String[] path = name.split("\\.");
            if(path.length == 1)
            {
                n = name;
                break;
            }

            StringBuilder b = new StringBuilder();
            b.append(path[0].charAt(0));
            for(int i = 1; i < path.length - 1; ++i)
            {
                b.append('.').append(path[i].charAt(0));
            }

            b.append('.').append(path[path.length - 1]);
            n = b.toString();
            break;
        }
        case None:
            n = name;
            break;
        }

        return new TwigLogger(n);
    }

}
