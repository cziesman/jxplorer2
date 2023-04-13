package com.ca.commons.naming;

/**
 * This code lovingly written by:
 * User: betch01
 * Date: 22/07/2003
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Arrays;

public class DXAttributeTest {

    @Test
    public void testNonStringCheck() {

        Assertions.assertTrue(DXAttribute.isStringSyntax(null));
        Assertions.assertFalse(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.5"));
        Assertions.assertFalse(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.28"));
        Assertions.assertFalse(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.40"));
        Assertions.assertFalse(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.4"));
        Assertions.assertFalse(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.8"));
        Assertions.assertFalse(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.9"));
        Assertions.assertFalse(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.10"));

        Assertions.assertFalse(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.4{112}"));
        Assertions.assertFalse(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.8{88}"));
        Assertions.assertFalse(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.9{0}"));

        Assertions.assertFalse(DXAttribute.isStringSyntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.8"));
        Assertions.assertFalse(DXAttribute.isStringSyntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.40"));
        Assertions.assertFalse(DXAttribute.isStringSyntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.4{777}"));

        Assertions.assertTrue(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.41"));
        Assertions.assertTrue(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.42"));
        Assertions.assertTrue(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.48"));
        Assertions.assertTrue(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.49"));

        Assertions.assertTrue(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.41{99}"));
        Assertions.assertTrue(DXAttribute.isStringSyntax("1.3.6.1.4.1.1466.115.121.1.42{512}"));

        Assertions.assertTrue(DXAttribute.isStringSyntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.49"));
        Assertions.assertTrue(DXAttribute.isStringSyntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.41{99}"));
        Assertions.assertTrue(DXAttribute.isStringSyntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.42{512}"));
    }

    @Test
    public void testASN1Check() {

        Assertions.assertTrue(DXAttribute.isASN1Syntax(null) == false);
        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.5"));
        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.8"));
        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.9"));
        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.10"));

        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.5{112}"));
        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.8{88}"));
        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.9{0}"));

        Assertions.assertTrue(DXAttribute.isASN1Syntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.5"));
        Assertions.assertTrue(DXAttribute.isASN1Syntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.8"));
        Assertions.assertTrue(DXAttribute.isASN1Syntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.9{777}"));

        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.28") == false);
        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.40") == false);
        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.48") == false);
        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.49") == false);

        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.28{99}") == false);
        Assertions.assertTrue(DXAttribute.isASN1Syntax("1.3.6.1.4.1.1466.115.121.1.40{512}") == false);

        Assertions.assertTrue(DXAttribute.isASN1Syntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.49") == false);
        Assertions.assertTrue(DXAttribute.isASN1Syntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.40{99}") == false);
        Assertions.assertTrue(DXAttribute.isASN1Syntax("SYNTAX 1.3.6.1.4.1.1466.115.121.1.28{512}") == false);
    }

    @Test
    public void testBasicAttributeFunctionality() throws NamingException {

        DXAttribute newAtt = new DXAttribute("fakeName", new DXNamingEnumeration(new ArrayList(Arrays.asList(new String[]{"a", "b", "c", "d"}))));

        Assertions.assertTrue("a".equals(newAtt.get()));
        Assertions.assertTrue("d".equals(newAtt.get(3)));
    }

}
