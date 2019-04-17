package ssjava.cholmod.demo;

import org.junit.jupiter.api.Test;


import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class CholmodDemoTest
{
    private static class TestComponent
    {
        final String matrix;
        final String results;

        private TestComponent(String matrix, String results)
        {
            this.matrix = matrix;
            this.results = results;
        }

        public String getMatrix()
        {
            return matrix;
        }

        public String getResults()
        {
            return results;
        }
    }

    private static final TestComponent[] TEST_COMPONENTS =
    {
            new TestComponent("abb313.mtx", "abb313_mtx_results.txt"),
            new TestComponent("bcsstk01.tri", "bcsstk01_tri_results.txt"),
            new TestComponent("bcsstk02.tri", "bcsstk02_tri_results.txt"),
            new TestComponent("can___24.mtx", "can___24_mtx_results.txt"),
            new TestComponent("c.mtx", "c_mtx_results.txt"),
            new TestComponent("d.tri", "d_tri_results.txt"),
            new TestComponent("lp_afiro.tri", "lp_afiro_tri_results.txt"),
            new TestComponent("one.tri", "one_tri_results.txt"),
            new TestComponent("up.tri", "up_tri_results.txt"),
            new TestComponent("pts5ldd03.mtx", "pts5ldd03_results.txt")
    };

    private static final Set<String> FIELDS;
    static
    {
        FIELDS = new HashSet<String>();
        FIELDS.add("norm (A,inf)");
        FIELDS.add("norm (A,1)");
        FIELDS.add("bnorm");
        FIELDS.add("Analyze");
        FIELDS.add("Factorizing");
        FIELDS.add("Ordering");
        FIELDS.add("ints in L");
        FIELDS.add("factor flops");
        FIELDS.add("nnz(A)");
        FIELDS.add("nnz(A*A')");
        FIELDS.add("flops / nnz(L)");
        FIELDS.add("nnz(L) / nnz(A)");
        FIELDS.add("residual");
        FIELDS.add("rcond");
        FIELDS.add("peak memory usage");
    }
    private static final Pattern SPACES = Pattern.compile("\\s+");

    void testErrorNoFile() throws IOException
    {
        boolean caught = false;
        try
        {
            new CholmodDemo().rundemo(System.out, new String[] {UUID.randomUUID().toString()});
        }
        catch(RuntimeException ex)
        {
            assertEquals(ex.toString(), "java.lang.RuntimeException: Received Error Status");
            caught = true;
        }
        finally
        {
            assertTrue(caught);
        }
    }

    @Test
    void rundemo() throws IOException
    {
        testErrorNoFile();

        for (TestComponent component : TEST_COMPONENTS)
        {
            Map<String, Object> expected = loadResourceValues("/" + component.getResults());
            Map<String, Object> actual = testRunDemo("/" + component.getMatrix());
            compare(expected, actual);
        }
        if (skipped.size() > 0)
        {
            System.out.println("Skipped lines:");
            for (String s : skipped)
            {
                System.out.format("\t%s\n", s);
            }
        }
    }

    private void compare(Map<String, Object> expected, Map<String, Object> actual)
    {
        for (Map.Entry<String, Object> me : actual.entrySet())
        {
            String key = me.getKey();
            Object expval = me.getValue();
            Object actval = me.getValue();
            switch(key)
            {
                case "Analyze":
                {
                    String[] texp = SPACES.split((CharSequence) expval);
                    String[] tact = SPACES.split((CharSequence) actval);
                    assertEquals(Double.valueOf(texp[1]), Double.valueOf(tact[1]));
                    assertEquals(Double.valueOf(texp[3]), Double.valueOf(tact[3]));
                    break;
                }
                default:
//                    System.err.format("%s: %s <-> %s\n", key, expval, actval);
                    assertEquals(expval, actval);
            }
        }
    }

    private Map<String, Object> testRunDemo(String resourceName) throws IOException
    {
        URL url = getClass().getResource(resourceName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new CholmodDemo().rundemo(bos,url.getFile());
        bos.close();
        return loadValues(new ByteArrayInputStream(bos.toByteArray()));
    }

    private Map<String, Object> loadResourceValues(String resourceName) throws IOException
    {
        return loadValues(getClass().getResourceAsStream(resourceName));
    }

    private final Set<String> skipped = new HashSet<>();

    private Map<String,Object> loadValues(InputStream in) throws IOException
    {
        HashMap<String,Object> rv = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String l = reader.readLine();
        while(l != null)
        {
            l = l.trim();
            if (l.length() > 0)
            {
                KeyValuePair keyValuePair = parseLine(l);
                if(keyValuePair != null)
                {
                    String tkey = keyValuePair.getKey();
                    if (tkey.equals("residual"))
                    {
                        String tval= (String) keyValuePair.getValue();
                        if (tval.endsWith("after iterative refinement"))
                        {
                            tkey = "residual_post_refinement";
                        }
                    }
                    rv.put(tkey, keyValuePair.getValue());
                }
                else
                {
                    boolean ignore = false;
                    for(String s : IGNORELINES)
                    {
                        if (l.contains(s))
                        {
                            ignore=true;
                            break;
                        }
                    }
                    if(!ignore) skipped.add(l);
                }
            }
            l = reader.readLine();
        }
        return rv;
    }

    private static final String[] IGNORELINES =
    {
            "--------",
            "CHOLMOD sparse",
            "CHOLMOD dense",
            "CHOLMOD factor",
            "version",
            "cputime"
    };

    private static class KeyValuePair
    {
        final String key;
        final Object value;

        private KeyValuePair(String key, Object value)
        {
            this.key = key;
            this.value = value;
        }

        public String getKey()
        {
            return key;
        }

        public Object getValue()
        {
            return value;
        }

        @Override
        public String toString()
        {
            return key + "=" + value.toString();
        }
    }

    private KeyValuePair parseLine(String line)
    {
        for(String key : FIELDS)
        {
            KeyValuePair rv = parseForSep(line, key);
            if(rv != null) return rv;
        }
        return null;
    }

    private static final Pattern DELIM = Pattern.compile("^[\\s=:]+(.*)$");
    private KeyValuePair parseForSep(String line, String key)
    {
        KeyValuePair rv = null;
        if(line.startsWith(key))
        {
            Matcher m = DELIM.matcher(line.substring(key.length()));
            if(m.matches())
            {
                String sval = m.group(1);
                Object value;
                try
                {
                    value = Double.valueOf(sval);
                }
                catch(NumberFormatException nfe)
                {
                    value = sval;
                }
                rv = new KeyValuePair(key, value);
            }
        }
        return rv;
    }

}