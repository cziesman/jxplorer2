package com.pegacat.testprovider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;
import javax.naming.ldap.LdapName;

/**
 * Runs through sanity tests for the fundamental
 * entry data object used in the tree model that backs
 * the test provider.
 */
public class DataTreeTest {

    String nameString[] = {"",                                          // 0
            "c=au",                                      // 1
            "o=pegacat,c=au",                            // 2
            "ou=research,o=pegacat,c=au",                // 3
            "cn=Fred,ou=research,o=pegacat,c=au",        // 4
            "cn=Eric,ou=research,o=pegacat,c=au",        // 5
            "cn=john,ou=research,o=apache,c=us"};        // 6

    LdapName name[] = new LdapName[nameString.length];

    TreeEntry entry[] = new TreeEntry[nameString.length];

    @BeforeEach
    protected void setUp() throws NamingException {

        for (int i = 0; i < nameString.length; i++) {
            name[i] = new LdapName(nameString[i]);
            entry[i] = new TreeEntry(name[i]);
        }
    }

    @Test
    public void testTreeBuilding() throws NamingException {

        DataTree testTree = new DataTree();

        Assertions.assertTrue(testTree.size() == 0);

        testTree.addEntry(entry[0]);      // add root node
        Assertions.assertTrue(testTree.size() == 1);

        for (int i = 1; i < 6; i++)           // add next 5 *in order*, parents first
            testTree.addEntry(entry[i]);

        Assertions.assertTrue(testTree.size() == 6);

        testTree.addEntry(entry[6]);      // add parent-less entry...

        testTree.dump();

        Assertions.assertTrue(testTree.size() == 10);
    }

    @Test
    public void testTreeRootDelete() {

        DataTree testTree = new DataTree();
        testTree.addEntry(entry[0]);
        testTree.deleteEntry("");
        Assertions.assertTrue(testTree.size() == 0);
    }

    @Test
    public void testParentReferences() {

        DataTree testTree = new DataTree();
        for (int i = 0; i < 7; i++)           // add next 5 *in order*, parents first
            testTree.addEntry(entry[i]);

        Assertions.assertTrue(entry[3].getChildren().size() == 2);

        testTree.deleteEntry(nameString[4]);

        Assertions.assertTrue(entry[3].getChildren().size() == 1);

        testTree.deleteEntry(nameString[5]);

        Assertions.assertTrue(entry[3].getChildren().size() == 0);

        testTree.addEntry(entry[5]);

        Assertions.assertTrue(entry[3].getChildren().size() == 1);
    }

    @Test
    public void testTreePruning() {

        DataTree testTree = new DataTree();
        for (int i = 0; i < 7; i++)           // add next 5 *in order*, parents first
            testTree.addEntry(entry[i]);

        Assertions.assertTrue(testTree.size() == 10);

        testTree.deleteEntry(nameString[5]);  // delete leaf node
        Assertions.assertTrue(testTree.size() == 9);

        testTree.deleteEntry(nameString[3]); // delete internal node
        Assertions.assertTrue(testTree.size() == 7);

        testTree.deleteEntry(nameString[6]); // delete leaf node
        Assertions.assertTrue(testTree.size() == 6);

        testTree.deleteEntry("c=us");       // delete synthetic place holder node with questions
        Assertions.assertTrue(testTree.size() == 3);

        testTree.deleteEntry("");           // delete root node
        Assertions.assertTrue(testTree.size() == 0);
    }

    @Test
    public void testTreeAddAndDelete() {

        DataTree testTree = new DataTree();
        for (int i = 0; i < 7; i++)           // add next 5 *in order*, parents first
            testTree.addEntry(entry[i]);

        Assertions.assertTrue(testTree.size() == 10);

        testTree.deleteEntry(nameString[5]);  // delete leaf node
        Assertions.assertTrue(testTree.size() == 9);

        testTree.deleteEntry(nameString[3]); // delete internal node
        Assertions.assertTrue(testTree.size() == 7);

        testTree.deleteEntry(nameString[6]); // delete leaf node
        Assertions.assertTrue(testTree.size() == 6);

        testTree.deleteEntry("c=us");       // delete synthetic place holder node with questions
        Assertions.assertTrue(testTree.size() == 3);

        testTree.addEntry(entry[6]);      // add parent-less entry...
        Assertions.assertTrue(testTree.size() == 7);

        testTree.addEntry(entry[5]);      // add parent-less entry...
        Assertions.assertTrue(testTree.size() == 9);

        testTree.addEntry(entry[4]);      // add parent-less entry...
        Assertions.assertTrue(testTree.size() == 10);

        testTree.deleteEntry("c=au");       // delete synthetic place holder node with questions
        Assertions.assertTrue(testTree.size() == 5);
    }

    @Test
    public void testTreeReplace() throws NamingException {

        DataTree testTree = new DataTree();
        for (int i = 0; i < 7; i++)           // add next 5 *in order*, parents first
            testTree.addEntry(entry[i]);

        Assertions.assertTrue(testTree.size() == 10);

        String existingName = "ou=research,o=pegacat,c=au";
        TreeEntry testEntry = testTree.get(existingName);
        Assertions.assertTrue(testEntry.children.size() == 2);

        TreeEntry replacement = new TreeEntry(new LdapName(existingName));
        replacement.put("tag", "marker");
        testTree.addEntry(replacement);

        Assertions.assertTrue(testTree.size() == 10);

        TreeEntry readItBack = testTree.get(existingName);

        Assertions.assertTrue(readItBack.children.size() == 2);
        Assertions.assertTrue("marker".equals(readItBack.get("tag").get()));
    }

}
