/*
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 *
 * User: betch01
 * Date: 15/05/2002 / 15:45:25
 */
package com.ca.commons.naming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A class to exercise and test the DN class
 *
 * @author Chris Betts
 */
public class DNTest {

    public static final String RDN3Att = "cn";

    public static final String RDN2Att = "ou";

    public static final String RDN1Att = "o";

    public static final String RDN0Att = "c";

    public static final String RDN3Val = "fred";

    public static final String RDN2Val = "\\+research";

    public static final String RDN1Val = "x\\=y\\+\\\"z\\\"";

    public static final String RDN0Val = "af";

    public static final String RDN3 = RDN3Att + '=' + RDN3Val + "+sn=bloggs";

    public static final String RDN2 = RDN2Att + '=' + RDN2Val;

    public static final String RDN1 = RDN1Att + '=' + RDN1Val;

    public static final String RDN0 = RDN0Att + '=' + RDN0Val;

    public static final String NODE = "cn=foo\\'tle+sn=snort";

    // should come out as: "cn=fred+sn=bloggs,ou=\\+research,o=x\\=y\\+\\'z\\',c=af";
    public static String bigComplicatedDN = RDN3 + ',' + RDN2 + ',' + RDN1 + ',' + RDN0;

    public static String anotherComplicatedDN = NODE + ',' + RDN2 + ',' + RDN1 + ',' + RDN0;

    public static final String DERDN5Att = "cn";

    public static final String DERDN4Att = "ou";

    public static final String DERDN3Att = "ou";

    public static final String DERDN2Att = "ou";

    public static final String DERDN1Att = "o";

    public static final String DERDN0Att = "c";

    public static final String DERDN5Val = "por-p201";

    public static final String DERDN4Val = "P 2.01 Koordinierung\\, Controlling\\, Strategische Personalplanung (P 2.01)";

    public static final String DERDN3Val = "P 2 Personalbetreuung\\, Stellenwirtschaft";

    public static final String DERDN2Val = "Personal- und Organisationsreferat";

    public static final String DERDN1Val = "Landeshauptstadt Munchen";

    public static final String DERDN0Val = "DE";

    public static final String DERDN5 = DERDN5Att + '=' + DERDN5Val;

    public static final String DERDN4 = DERDN4Att + '=' + DERDN4Val;

    public static final String DERDN3 = DERDN3Att + '=' + DERDN3Val;

    public static final String DERDN2 = DERDN2Att + '=' + DERDN2Val;

    public static final String DERDN1 = DERDN1Att + '=' + DERDN1Val;

    public static final String DERDN0 = DERDN0Att + '=' + DERDN0Val;

    public static String DEbigComplicatedDN = DERDN5 + ',' + DERDN4 + ',' + DERDN3 + ',' + DERDN2 + ',' + DERDN1 + ',' + DERDN0;

    public static final String strangeDN = "cn=\\\"Craig \\\\nLink\\\",ou=Administration,ou=Corporate,o= DEMOCORP,c=AU";

    public static final String strangeRDN1 = "cn=\"Craig \\nLink\"";

    public static final String strangeRDN2 = "ou=Administration";

    public static final String strangeRDN3 = "ou=Corporate";

    public static final String strangeRDN4 = "o= DEMOCORP";

    public static final String strangeRDN5 = "c=AU";

    @Test
    public void testEmptyConstructor() {

        DN bloop = new DN();
        Assertions.assertEquals(bloop.size(), 0);
    }

    @Test
    public void testCopyConstructor() {

        DN copyMe = new DN(bigComplicatedDN);
        DN copy = new DN(copyMe);
        DN bloop = new DN(copy);
        Assertions.assertEquals(copyMe, bloop);
    }

    @Test
    public void testStringConstructor() {

        DN stringDN = new DN(bigComplicatedDN);
        Assertions.assertEquals(stringDN.toString(), bigComplicatedDN);
    }

    @Test
    public void testDEStringConstructor() {

        DN stringDN = new DN(DEbigComplicatedDN);
        Assertions.assertEquals(stringDN.toString(), DEbigComplicatedDN);
        System.out.println(stringDN);
    }

    // this is actually a fair bit of work to test, since it
    // requires creating a JNDI ldap Name, which is a bitch.
/*    public void testNameConstructor()
    {
        assertTrue(true); // placeholder
        //DN(Name name)
    }
*/

    // Kaff - pretty much the same test as the string constructor.
    @Test
    public void testToString() {

        DN stringDN = new DN(bigComplicatedDN);
        Assertions.assertEquals(stringDN.toString(), bigComplicatedDN);
    }

    @Test
    public void testGetDN() {

        testToString();  // deprecated method chains to toString()
    }

    @Test
    public void testMakeStrangeDN()
            throws NamingException {

        DN testDN = new DN();
        testDN.add(strangeRDN5);
        testDN.add(strangeRDN4);
        testDN.add(strangeRDN3);
        testDN.add(strangeRDN2);

        RDN testRDN = new RDN();
        testRDN.addRaw(strangeRDN1);
        testDN.add(testRDN);
        Assertions.assertEquals(testDN.toString(), strangeDN);
    }

    @Test
    public void testDEGetRDNAttribute() {

        DN testDN = new DN(DEbigComplicatedDN);
        Assertions.assertEquals(DERDN0Att, testDN.getRDNAttribute(0));
        Assertions.assertEquals(DERDN1Att, testDN.getRDNAttribute(1));
        Assertions.assertEquals(DERDN2Att, testDN.getRDNAttribute(2));
        Assertions.assertEquals(DERDN3Att, testDN.getRDNAttribute(3));
        Assertions.assertEquals(DERDN4Att, testDN.getRDNAttribute(4));
        Assertions.assertEquals(DERDN5Att, testDN.getRDNAttribute(5));
        Assertions.assertEquals("", testDN.getRDNAttribute(6));
        Assertions.assertEquals("", testDN.getRDNAttribute(-1));
    }

    @Test
    public void testGetRDNAttribute() {

        DN testDN = new DN(bigComplicatedDN);
        Assertions.assertEquals(RDN0Att, testDN.getRDNAttribute(0));
        Assertions.assertEquals(RDN1Att, testDN.getRDNAttribute(1));
        Assertions.assertEquals(RDN2Att, testDN.getRDNAttribute(2));
        Assertions.assertEquals(RDN3Att, testDN.getRDNAttribute(3));
        Assertions.assertEquals("", testDN.getRDNAttribute(4));
        Assertions.assertEquals("", testDN.getRDNAttribute(-1));
    }

    @Test
    public void testGetRDNValue() throws InvalidNameException {

        DN testDN = new DN(bigComplicatedDN);

        Assertions.assertEquals(NameUtility.unescape(RDN0Val), testDN.getRDNValue(0));
        Assertions.assertEquals(NameUtility.unescape(RDN1Val), testDN.getRDNValue(1));
        Assertions.assertEquals(NameUtility.unescape(RDN2Val), testDN.getRDNValue(2));
        Assertions.assertEquals(NameUtility.unescape(RDN3Val), testDN.getRDNValue(3));

        Assertions.assertEquals("", testDN.getRDNValue(4));
        Assertions.assertEquals("", testDN.getRDNValue(-1));
    }

    @Test
    public void testSetRDN() {

        DN test = new DN("x=x,x=x,x=x,x=x");
        test.setRDN(new RDN(RDN0), 0);
        test.setRDN(new RDN(RDN1), 1);
        test.setRDN(new RDN(RDN2), 2);
        test.setRDN(new RDN(RDN3), 3);
        Assertions.assertEquals(new DN(bigComplicatedDN), test);
    }

    @Test
    public void testGetRDN() {

        DN test = new DN(bigComplicatedDN);
        Assertions.assertEquals(test.getRDN(0), new RDN(RDN0));
        Assertions.assertEquals(test.getRDN(1), new RDN(RDN1));
        Assertions.assertEquals(test.getRDN(2), new RDN(RDN2));
        Assertions.assertEquals(test.getRDN(3), new RDN(RDN3));
    }

    @Test
    public void testGetRootRDN() {

        DN test = new DN(bigComplicatedDN);
        Assertions.assertEquals(test.getRootRDN(), new RDN(RDN0));
        Assertions.assertEquals(test.getRootRDN(), test.getRDN(0));
    }

    @Test
    public void testGetLowestRDN() {

        DN test = new DN(bigComplicatedDN);
        Assertions.assertEquals(test.getLowestRDN(), new RDN(RDN3));
        Assertions.assertEquals(test.getLowestRDN(), test.getRDN(3));
    }

    @Test
    public void testStartsWith() {

        DN big = new DN(bigComplicatedDN);
        DN prefix = new DN(RDN0);

        Assertions.assertTrue(big.startsWith(prefix));

        prefix.add(new RDN(RDN1));
        Assertions.assertTrue(big.startsWith(prefix));

        prefix.add(new RDN(RDN2));
        Assertions.assertTrue(big.startsWith(prefix));
    }

    @Test
    public void testSharesParent() {

        DN big = new DN(bigComplicatedDN);
        DN sibling = new DN(anotherComplicatedDN);

        Assertions.assertTrue(big.sharesParent(sibling));
    }

    @Test
    public void testParentDN() {

        DN veryLongDN = new DN("cn=new level," + bigComplicatedDN);
        Assertions.assertEquals(veryLongDN.getParent(), new DN(bigComplicatedDN));
    }

    @Test
    public void testReverse() {

        String bigComplicatedDN = RDN3 + ',' + RDN2 + ',' + RDN1 + ',' + RDN0;
        String reverseDN = RDN0 + ',' + RDN1 + ',' + RDN2 + ',' + RDN3;

        DN testReversal = new DN(bigComplicatedDN);
        testReversal.reverse();
        Assertions.assertEquals(testReversal.toString(), reverseDN); // placeholder
    }

    @Test
    public void testClear() {

        DN big = new DN(bigComplicatedDN);
        big.clear();
        Assertions.assertTrue(big.size() == 0); // placeholder
    }

    @Test
    public void testSorting() {

        ArrayList<DN> list = new ArrayList<DN>();

        list.add(new DN("cn=fred,ou=legal,o=pegacat,c=au"));
        list.add(new DN("o=pegacat,c=au"));
        list.add(new DN("cn=nigel,ou=legal,o=pegacat,c=au"));
        list.add(new DN("cn=fred,ou=HR,o=pegacat,c=au"));
        list.add(new DN("cn=greg,ou=legal,o=ibm,c=us"));
        list.add(new DN("c=au"));
        list.add(new DN("c=us"));

        Collections.sort(list);

        for (DN dn : list)
            System.out.println("DN: " + dn.toString());

        Assertions.assertEquals(list.get(0).toString(), "c=au");
        Assertions.assertEquals(list.get(5).toString(), "c=us");
    }

}
