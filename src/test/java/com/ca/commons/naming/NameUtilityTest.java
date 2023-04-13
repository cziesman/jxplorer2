/**
 *
 */

package com.ca.commons.naming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;

public class NameUtilityTest {

    private static final String myLdapDN = "\\e6\\90\\ad\\e9\\85\\8d\\e5\\90\\b8\\e6\\94\\b6\\e5\\a4\\96\\e8\\b5\\84";

    private static final String myUnicode = "\u642d\u914d\u5438\u6536\u5916\u8d44";

    private static final String finalSpaceTest = "bloop\\ ";

    private static final String finalSpaceTestOutcome = "bloop ";

    private static final String badFinalSpace = "bloop\\";

    private static final String badFinalSpaceOutcome = "bloop ";

    private static final String specialCharRDNVal = "jon\\,fred\\+erick (\\\"\\<http:\\\\\\\\www.blarg.com\\>\\\")";

    @Test
    public void testCodec() throws NamingException {

        String unicode1 = NameUtility.unescape(myLdapDN);
        Assertions.assertEquals(unicode1, myUnicode);

        String temp = NameUtility.unescape(specialCharRDNVal);
        String backAgain = NameUtility.escape(temp);

        Assertions.assertEquals(specialCharRDNVal, backAgain);
    }

    @Test
    public void testFinalSpace() throws NamingException {

        String temp = NameUtility.unescape(finalSpaceTest);
        Assertions.assertEquals(finalSpaceTestOutcome, temp);

        temp = NameUtility.escape(temp);
        Assertions.assertEquals(finalSpaceTest, temp);

        temp = NameUtility.unescape(badFinalSpace);
        Assertions.assertEquals(badFinalSpaceOutcome, temp);
    }

}
