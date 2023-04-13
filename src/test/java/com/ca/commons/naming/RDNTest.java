package com.ca.commons.naming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;

/**
 * A class to exercise and test the RDN class
 */

public class RDNTest {

    static final String SMALL_RDN = "l=a";

    static final String TRIPLE_RDN = "cn=fred+l=a+sn=x";

    static final String NASTY_RDN = "cn=x\\=y\\+z+l=a\\ +sn=x";

    static final String SMALL_MULTIVALUED_RDN = "o=o+l=l";

    @Test
    public void testSmallRDN() {

        RDN stringRDN = new RDN(SMALL_RDN);
        Assertions.assertEquals(stringRDN.toString(), SMALL_RDN);
        Assertions.assertEquals(stringRDN.getAttID(), "l");
        Assertions.assertEquals(stringRDN.getRawVal(), "a");

        System.out.println("RDN " + stringRDN.toString() + " : " + stringRDN.getAttID() + " " + stringRDN.getRawVal());
    }

    @Test
    public void testTripleRDN() {

        RDN stringRDN = new RDN(TRIPLE_RDN);
        Assertions.assertEquals(stringRDN.toString(), TRIPLE_RDN);
        Assertions.assertEquals(stringRDN.getAttID(0), "cn");
        Assertions.assertEquals(stringRDN.getRawVal(0), "fred");
        Assertions.assertEquals(stringRDN.getAttID(1), "l");
        Assertions.assertEquals(stringRDN.getRawVal(1), "a");
        Assertions.assertEquals(stringRDN.getAttID(2), "sn");
        Assertions.assertEquals(stringRDN.getRawVal(2), "x");
    }

    @Test
    public void testNastyRDN() {

        RDN stringRDN = new RDN(NASTY_RDN);
        Assertions.assertEquals(stringRDN.toString(), NASTY_RDN);
        Assertions.assertEquals(stringRDN.getAttID(0), "cn");
        Assertions.assertEquals(stringRDN.getRawVal(0), "x=y+z");
        Assertions.assertEquals(stringRDN.getAttID(1), "l");
        Assertions.assertEquals(stringRDN.getRawVal(1), "a ");
        Assertions.assertEquals(stringRDN.getAttID(2), "sn");
        Assertions.assertEquals(stringRDN.getRawVal(2), "x");
    }

    @Test
    public void testAddEscaped()
            throws InvalidNameException {

        RDN rdn = new RDN("o=o");
        rdn.addEscaped("l=l");
        Assertions.assertEquals(rdn.toString(), SMALL_MULTIVALUED_RDN);
    }

    @Test
    public void testAddEscapedFail1() {

        Assertions.assertThrows(InvalidNameException.class, () -> {
            RDN rdn = new RDN("o=o");
            rdn.addEscaped("l=");
        });
    }

    @Test
    public void testAddEscapedFail2() {

        Assertions.assertThrows(InvalidNameException.class, () -> {
            RDN rdn = new RDN("o=o");
            rdn.addEscaped("=l");
        });
    }

}
