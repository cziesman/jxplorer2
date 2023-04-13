/**
 * Author: Chris Betts
 * Date: 5/06/2002 / 16:59:37
 */
package com.ca.commons.naming;

import com.ca.commons.jndi.BasicOps;
import com.ca.commons.jndi.ConnectionData;
import com.ca.commons.jndi.SchemaOps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.ldap.LdapContext;

public class DXAttributesTest {

    private static final BasicAttribute attribute1 = new BasicAttribute("att1", "value1");

    private static final BasicAttribute attribute2 = new BasicAttribute("att2", "value2");

    private static final BasicAttribute attribute3 = new BasicAttribute("att3", "value3");

    private static final BasicAttribute attribute4 = new BasicAttribute("att4", "value4");

    private static final BasicAttribute attribute5 = new BasicAttribute("att5", "value5");
    //private static final BasicAttribute empty1 = new BasicAttribute("att5", null);
    //private static final BasicAttribute empty2 = new BasicAttribute("att5");
    //private static final BasicAttribute empty3 = new BasicAttribute("att5", "");

    @Test
    public void testEmptyConstructor() {

        DXAttributes bloop = new DXAttributes();
        Assertions.assertEquals(bloop.size(), 0);
    }

    @Test
    public void testEquality() {

        try {
            DXAttributes empty = new DXAttributes();

            DXAttributes atts1 = new DXAttributes();
            atts1.put(attribute1);
            atts1.put(attribute2);
            atts1.put(attribute3);
            atts1.put(attribute4);

            DXAttributes atts2 = new DXAttributes();
            atts2.put(attribute4);
            atts2.put(attribute3);
            atts2.put(attribute1);
            atts2.put(attribute2);

            Assertions.assertEquals(atts1, atts2);

            atts2.remove("att2");
            Assertions.assertTrue(!atts1.equals(atts2));

            atts2.put(attribute5);
            Assertions.assertTrue(!atts1.equals(atts2));

            Assertions.assertTrue(!empty.equals(null));

            atts2.remove("att1");
            atts2.remove("att3");
            atts2.remove("att4");
            atts2.remove("att5");
            Assertions.assertEquals(empty, atts2);

            Assertions.assertTrue(DXAttributes.attributesEqual(null, null));
            Assertions.assertTrue(!DXAttributes.attributesEqual(null, new DXAttributes()));
            Assertions.assertTrue(!DXAttributes.attributesEqual(new DXAttributes(), null));
        } catch (Exception e) {
            System.out.println("unexpected exception in DXAttributesTest: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Tests the expand all attributes.  DXAttribute
     * has a set default schema - this uses that
     * in the test b/c
     * @throws Exception should not throw an
     * exception.
     */
    //TODO - make this a system test.  Currently it wont be run - to run remove the DONT from the method name.
    public void DONTtestExpandAllAttributes1()
            throws Exception {

        ConnectionData cData = new ConnectionData();
        cData.setURL("ldap://betch01:19389");
        LdapContext ctx = BasicOps.openContext(cData);
        //LdapContext ctx = BasicOps.openContext("ldap://betch01:19389");
        DXAttributes a = new DXAttributes(ctx.getAttributes("ou=Manufacturing,o=DEMOCORP,c=AU"));
        SchemaOps schemaOps = new SchemaOps(ctx);
        ctx.addToEnvironment("java.naming.ldap.attributes.binary", schemaOps.getNewBinaryAttributes());
//        DXAttributes.setDefaultSchema(schemaOps);
//        DXAttribute.setDefaultSchema(schemaOps);
        a.expandAllAttributes();
    }

    /**
     * Tests the expand all attributes.  DXAttribute
     * has a set default schema - this uses that
     * in the test b/c
     * @throws Exception should not throw an
     * exception.
     */
    //TODO - make this a system test.  Currently it wont be run - to run remove the DONT from the method name.
    public void DONTtestExpandAllAttributes2()
            throws Exception {

        ConnectionData cData = new ConnectionData();
        cData.setURL("ldap://betch01:19389");
        LdapContext ctx = BasicOps.openContext(cData);
        //LdapContext ctx = BasicOps.openContext("ldap://betch01:19389");
        DXAttributes a = new DXAttributes(ctx.getAttributes("ou=Manufacturing,o=DEMOCORP,c=AU"));
        SchemaOps schemaOps = new SchemaOps(ctx);
        ctx.addToEnvironment("java.naming.ldap.attributes.binary", schemaOps.getNewBinaryAttributes());
        DXAttributes.setDefaultSchema(schemaOps);
//        DXAttribute.setDefaultSchema(schemaOps);
        a.expandAllAttributes();
    }

    /**
     * Tests the expand all attributes.  DXAttribute
     * has a set default schema - this uses that
     * in the test b/c
     * @throws Exception should not throw an
     * exception.
     */
    //TODO - make this a system test.  Currently it wont be run - to run remove the DONT from the method name.
    public void DONTtestExpandAllAttributes3()
            throws Exception {

        ConnectionData cData = new ConnectionData();
        cData.setURL("ldap://betch01:19389");
        LdapContext ctx = BasicOps.openContext(cData);

        //LdapContext ctx = BasicOps.openContext("ldap://betch01:19389");
        DXAttributes a = new DXAttributes(ctx.getAttributes("ou=Manufacturing,o=DEMOCORP,c=AU"));
        SchemaOps schemaOps = new SchemaOps(ctx);
        ctx.addToEnvironment("java.naming.ldap.attributes.binary", schemaOps.getNewBinaryAttributes());
        DXAttribute.setDefaultSchema(schemaOps);
        a.expandAllAttributes();
    }

    /**
     * Tests the expand all attributes.  DXAttribute
     * has a set default schema - this uses that
     * in the test b/c
     * @throws Exception should not throw an
     * exception.
     */
    //TODO - make this a system test.  Currently it wont be run - to run remove the DONT from the method name.
    public void DONTtestExpandAllAttributes4()
            throws Exception {

        ConnectionData cData = new ConnectionData();
        cData.setURL("ldap://betch01:19389");
        LdapContext ctx = BasicOps.openContext(cData);
        //LdapContext ctx = BasicOps.openContext("ldap://betch01:19389");
        DXAttributes a = new DXAttributes(ctx.getAttributes("ou=Manufacturing,o=DEMOCORP,c=AU"));
        SchemaOps schemaOps = new SchemaOps(ctx);
        ctx.addToEnvironment("java.naming.ldap.attributes.binary", schemaOps.getNewBinaryAttributes());
        DXAttributes.setDefaultSchema(schemaOps);
        DXAttribute.setDefaultSchema(schemaOps);
        a.expandAllAttributes();
    }

    @Test
    public void testGetDeletionSet() throws NamingException {

        RDN newRDN = new RDN("cn=Test");
        Attributes oldSet = new DXAttributes();
        oldSet.put(new BasicAttribute("att1", "value1"));
        oldSet.put(new BasicAttribute("att2", "value2"));
        oldSet.put(new BasicAttribute("att3", "value3;binary"));
        oldSet.put(new BasicAttribute("att4", "value4"));
        oldSet.put(new BasicAttribute("att5", "value5"));

        Attributes newSet = new DXAttributes();
        newSet.put(new BasicAttribute("att1", "value1"));
        newSet.put(new BasicAttribute("att2", "value2"));
        newSet.put(new BasicAttribute("att3", "value3"));
        newSet.put(new BasicAttribute("att4", "value4"));

        DXAttributes deletionSet = DXAttributes.getDeletionSet(newRDN, oldSet, newSet);
        Assertions.assertEquals(1, deletionSet.size());

        Attribute a = deletionSet.get("att5");
        Assertions.assertNotNull(a);
    }

}
