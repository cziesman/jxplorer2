package com.pegacat.testprovider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.ldap.LdapName;

/**
 * Sanity test the basic TreeEntry class.
 * Tests are pretty Mickey Mouse, as the class is a simple data container that doesn't do very much
 * more than extend the BasicAttributes class it is based on...
 */
public class TreeEntryTest {

    private final String nameString[] = {"", // 0
            "c=au", // 1
            "o=pegacat,c=au", // 2
            "ou=research,o=pegacat,c=au", // 3
            "cn=Fred,ou=research,o=pegacat,c=au", // 4
            "cn=Eric,ou=research,o=pegacat,c=au", // 5
            "cn=john,ou=research,o=apache,c=us"};     // 6

    LdapName name[] = new LdapName[nameString.length];

    TreeEntry entry[] = new TreeEntry[nameString.length];

    @BeforeEach
    public void setUp()
            throws NamingException {

        for (int i = 0; i < nameString.length; i++)
            name[i] = new LdapName(nameString[i]);
    }

    @Test
    public void testClone() throws NamingException {

        TreeEntry entry = new TreeEntry(name[5], new String[]{"objectClass", "person", "cn", "Fred", "favouriteDrink", "vodka"});
        TreeEntry clone = (TreeEntry) entry.clone();

        Assertions.assertTrue(clone.size() == 3);

        entry.remove("favouriteDrink");

        Assertions.assertTrue(entry.size() == 2);
        Assertions.assertTrue(clone.size() == 3);

        Assertions.assertNotNull(clone.getName());
        Assertions.assertNotNull(entry.getName());
        Assertions.assertEquals(clone.getName(), entry.getName());

        entry.name = new LdapName("cn=blah");

        Assertions.assertNotEquals(clone.getName(), entry.getName());
    }

    @Test
    public void testNameRegistration() {

        TreeEntry entry;

        for (int i = 0; i < nameString.length; i++) {
            entry = new TreeEntry(name[i]);
            Assertions.assertNotNull(entry.getName());
            Assertions.assertEquals(entry.getName(), name[i]);
            Assertions.assertEquals(entry.getName().toString(), nameString[i]);
            Assertions.assertEquals(entry.getStringName(), nameString[i]);
        }
    }

    @Test
    public void testAttributeRegistration() throws NamingException {

        TreeEntry entry = new TreeEntry(name[5], new String[]{"objectClass", "person", "cn", "Fred", "favouriteDrink", "vodka"});

        Assertions.assertEquals(entry.get("objectClass").get(), "person");
        Assertions.assertEquals(entry.get("cn").get(), "Fred");
        Assertions.assertEquals(entry.get("favouriteDrink").get(), "vodka");
    }

    @Test
    public void testEquality() throws NamingException {

        for (int i = 0; i < nameString.length; i++) {
            name[i] = new LdapName(nameString[i]);
            entry[i] = new TreeEntry(name[i]);
        }

        TreeEntry test = new TreeEntry(new LdapName(nameString[3]));

        Assertions.assertTrue(test.equals(entry[3]));
        Assertions.assertFalse(test.equals(entry[4]));
    }

    @Test
    public void testChildRegistration() throws InvalidNameException {

        for (int i = 0; i < nameString.length; i++) {
            name[i] = new LdapName(nameString[i]);
            entry[i] = new TreeEntry(name[i]);
        }

        entry[3].addChild(entry[4]);
        entry[3].addChild(entry[5]);

        Assertions.assertTrue(entry[3].getChildren().size() == 2);

        entry[3].removeChild(entry[4]);
        Assertions.assertTrue(entry[3].getChildren().size() == 1);

        entry[3].removeChild(entry[2]); // doesn't exist

        Assertions.assertTrue(entry[3].getChildren().size() == 1);

        entry[3].removeChild(entry[5]);
        Assertions.assertTrue(entry[3].getChildren().size() == 0);
    }

}
