package com.ca.commons.naming;

import com.ca.directory.jxplorer.JXConfig;
import com.pegacat.testprovider.TestLdapContextData;
import com.pegacat.testprovider.TestProviderContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import java.util.Properties;

/**
 * (c) Chris Betts; Pegacat Software (http://pegacat.com)
 */
public class DXOpsTest {

    protected TestProviderContext ctx;

    protected DXOps dxops;

    @BeforeEach
    protected void setUp()
            throws Exception {
        // initialise a fake directory with built in test data - a bit of stuffing around to fake enough part of
        // JXplorer to make this work...
        JXConfig.setupProperties(new Properties());
        ctx = new TestLdapContextData().setupTestContext();
        dxops = new DXOps(ctx);
/*
        SchemaOps schemaOps = new SchemaOps(ctx);
        ctx.addToEnvironment("java.naming.ldap.attributes.binary", schemaOps.getNewBinaryAttributes());
        DXAttribute.setDefaultSchema(schemaOps);
*/
    }

    @Test
    public void testNameChange()
            throws Exception {

        DXEntry oldEntry = new DXEntry("cn=Fiona Jensen,ou=Marketing,dc=airius,dc=com",
                new Attribute[]{new DXAttribute("objectclass", new String[]{"top", "person", "organizationalPerson"}),
                        new DXAttribute("cn", "Fiona Jensen"),
                        new BasicAttribute("sn", "Jensen"),
                        new BasicAttribute("telephonenumber", "+1 408 555 1212"),
                        new BasicAttribute("uid", "fiona")});

        DXEntry newEntryChangedCN = new DXEntry("cn=Fred Jensen,ou=Marketing,dc=airius,dc=com",
                new Attribute[]{new DXAttribute("objectclass", new String[]{"top", "person", "organizationalPerson"}),
                        new DXAttribute("cn", "Fred Jensen"),
                        new BasicAttribute("sn", "Jensen"),
                        new BasicAttribute("telephonenumber", "+1 408 555 1212"),
                        new BasicAttribute("uid", "fiona")});

        Assertions.assertTrue(dxops.doWeReplaceTheRDN(oldEntry, newEntryChangedCN));

        DXEntry newEntryExtraCN = new DXEntry("cn=Fred Jensen,ou=Marketing,dc=airius,dc=com",
                new Attribute[]{new DXAttribute("objectclass", new String[]{"top", "person", "organizationalPerson"}),
                        new DXAttribute("cn", new String[]{"Fred Jensen", "Fiona Jensen"}),
                        new BasicAttribute("sn", "Jensen"),
                        new BasicAttribute("telephonenumber", "+1 408 555 1212"),
                        new BasicAttribute("uid", "fiona")});

        Assertions.assertFalse(dxops.doWeReplaceTheRDN(oldEntry, newEntryExtraCN));

        DXEntry newEntryNewNamingAtt = new DXEntry("sn=Jensen,ou=Marketing,dc=airius,dc=com",
                new Attribute[]{new DXAttribute("objectclass", new String[]{"top", "person", "organizationalPerson"}),
                        new DXAttribute("cn", new String[]{"Fred Jensen", "Fiona Jensen"}),
                        new BasicAttribute("sn", "Jensen"),
                        new BasicAttribute("telephonenumber", "+1 408 555 1212"),
                        new BasicAttribute("uid", "fiona")});

        Assertions.assertFalse(dxops.doWeReplaceTheRDN(oldEntry, newEntryNewNamingAtt));
    }

}
