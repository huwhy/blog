package cn.huwhy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public void testColumn() {
        String columnSql = "insert into temp(a, b c) values (?,?,?)";
        int fromIndex = columnSql.indexOf("(");
        int endIndex = columnSql.indexOf(")");
        System.out.println(columnSql.substring(fromIndex, endIndex));
    }
}
