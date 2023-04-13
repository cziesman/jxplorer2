package com.ca.commons.jndi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for creating LDAP filters from JAXB objects.
 * It passes a DSML XML request to the LdapFilter class
 * and compares the result with the expected LDAP filter.
 *
 * @author Chris.
 */

public class JNDIOpsTest {

    @Test

    /**
     * Test examples from RFC 2252
     */
    public void testURLEncoding() {

        Assertions.assertEquals("ldap://localhost:19389", JNDIOps.makeServerURL("ldap://localhost:19389", null));
        Assertions.assertEquals("ldap://localhost:19389", JNDIOps.makeServerURL("ldap://localhost:19389", ""));
        Assertions.assertEquals("ldap:///o=University%20of%20Michigan,c=US", JNDIOps.makeServerURL("ldap://", "o=University of Michigan,c=US"));
        Assertions.assertEquals("ldap:///o=University%20of%20Michigan,c=US", JNDIOps.makeServerURL("ldap:///", "o=University of Michigan,c=US"));
        Assertions.assertEquals("ldap://ldap.itd.umich.edu/o=University%20of%20Michigan,c=US", JNDIOps.makeServerURL("ldap://ldap.itd.umich.edu", "o=University of Michigan,c=US"));
        Assertions.assertEquals("ldap://ldap.itd.umich.edu/o=University%20of%20Michigan,c=US", JNDIOps.makeServerURL("ldap://ldap.itd.umich.edu/", "o=University of Michigan,c=US"));
        Assertions.assertEquals("ldap://ldap.question.com/o=Question%3f,c=US", JNDIOps.makeServerURL("ldap://ldap.question.com", "o=Question?,c=US"));
        Assertions.assertEquals("ldap:///o=%20%3c%3e%3f%23%25%7b%7d%7c%5c%5e%7e%5b%5d%27", JNDIOps.makeServerURL("ldap:///", "o= <>\"#%{}|\\^~[]'"));
    }

}