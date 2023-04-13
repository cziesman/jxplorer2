/**
 * Created by IntelliJ IDEA.
 * User: betch01
 * Date: Dec 3, 2002
 * Time: 12:09:51 PM
 * To change this template use Options | File Templates.
 */
package com.ca.commons.jndi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.Enumeration;

public class SchemaOpsTest {

    private static boolean testWithDirectory = false;

    private static SchemaOps virtualOps = null;

    private static SchemaOps directorySchemaOps = null;

    @Test
    public void testGetOID() {

        Assertions.assertEquals("0.9.2342.19200300.100.4.4", virtualOps.getOID(syntaxValue1));
        Assertions.assertEquals("2.5.4.0", virtualOps.getOID(attributeTypes[0]));
        Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.31", virtualOps.getOID("( 1.3.6.1.4.1.1466.115.121.1.31 ) "));
        Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.32", virtualOps.getOID("(1.3.6.1.4.1.1466.115.121.1.32  "));
        Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.33", virtualOps.getOID("(1.3.6.1.4.1.1466.115.121.1.33)"));
        Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.34", virtualOps.getOID("1.3.6.1.4.1.1466.115.121.1.34"));
    }

    private static String syntaxValue1 = "( 0.9.2342.19200300.100.4.4 NAME 'newPilotPerson' SUP ( person ) " +
            "STRUCTURAL MAY ( uid $ mail $ drink $ roomNumber $ userClass $ homePhone $ homePostalAddress " +
            " $ secretary $ personalTitle $ preferredDeliveryMethod $ businessCategory $ janetMailbox " +
            " $ otherMailbox $ mobile $ pager $ organizationalStatus $ mailPreferenceOption $ personalSignature ) ) ";

    private static String syntaxValue2 = "( 0.9.2342.19200300.100.4.4 NAME ( 'newPilotPerson' 'fred' 'neuerFleigerMensh' ) SUP ( person ) " +
            "STRUCTURAL MAY ( uid $ mail $ drink $ roomNumber $ userClass $ homePhone $ homePostalAddress " +
            " $ secretary $ personalTitle $ preferredDeliveryMethod $ businessCategory $ janetMailbox " +
            " $ otherMailbox $ mobile $ pager $ organizationalStatus $ mailPreferenceOption $ personalSignature ) ) ";

    /*
     *  Test case where there are no spaces between things...
     */
    private static String syntaxValue3 = "(9.9.9 NAME 'test' SUP (top) STRUCTURAL MAY " +
            "(uid$mail$drink$roomNumber$userClass$homePhone$ " +
            "homePostalAddress $ secretary $ personalTitle $ preferredDeliveryMethod $ " +
            "businessCategory $ janetMailbox $ otherMailbox $ mobile $ pager $ " +
            "organizationalStatus $ mailPreferenceOption $ personalSignature) )";

    private static String[] syntaxValue3Mays =
            {"uid", "mail", "drink", "roomNumber", "userClass", "homePhone",
                    "homePostalAddress", "secretary", "personalTitle", "preferredDeliveryMethod",
                    "businessCategory", "janetMailbox", "otherMailbox", "mobile", "pager",
                    "organizationalStatus", "mailPreferenceOption", "personalSignature"};

    private static String[] attributeTypes = {
            "( 2.5.4.0 NAME ( 'objectClass' 'oc' 'objectClass' ) SYNTAX 1.3.6.1.4.1.1466.115.121.1.38 )",
            "( 2.5.4.1 NAME ( 'aliasedObjectName' 'aliasedObjectName' ) SYNTAX 1.3.6.1.4.1.1466.115.121.1.12 )",
            "( 2.5.4.2 NAME 'knowledgeInformation' SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )",
            "( 2.5.4.3 NAME ( 'cn' 'commonName' ) SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )",
            "( 2.5.4.4 NAME ( 'sn' 'surname' ) SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )",
            "( 2.5.4.5 NAME 'serialNumber' SYNTAX 1.3.6.1.4.1.1466.115.121.1.44 )",
            "( 2.5.4.6 NAME ( 'c' 'countryName' ) SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )",
            "( 2.5.4.7 NAME ( 'l' 'localityName' ) SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )",
            "( 2.5.4.8 NAME ( 'st' 'stateOrProvinceName' ) SYNTAX",
            "( 1.3.6.1.4.1.453.7.3.2.4 NAME 'mhsBadAddressSearchPoint' SYNTAX 1.3.6.1.4.1.1466.115.121.1.12 )",
            "( 1.3.6.1.4.1.453.7.3.2.5 NAME 'mhsBadAddressSearchAttributes' SYNTAX 1.3.6.1.4.1.1466.115.121.1.5 )",
            "( 1.3.6.1.4.1.453.7.3.2.6 NAME 'mhsBodyPartConversionService' SYNTAX 1.3.6.1.4.1.1466.115.121.1.5 )",
            "( 1.3.6.1.4.1.3327.6.1000.1.13 NAME 'cert_authCertSerialNumber' SYNTAX 1.3.6.1.4.1.1466.115.121.1.40 )",
            "( 1.3.6.1.4.1.3327.77.4.1.2 NAME 'uNSPSCTitle' SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 SINGLE-VALUE )"};

    // note sorted order, no redundancies
    private static String[] sortedAttributeNames = {"aliasedObjectName", "c", "cert_authCertSerialNumber",
            "cn", "commonName", "countryName", "knowledgeInformation",
            "l", "localityName", "mhsBadAddressSearchAttributes", "mhsBadAddressSearchPoint",
            "mhsBodyPartConversionService", "objectClass", "oc",
            "serialNumber", "sn", "st",
            "stateOrProvinceName", "surname", "uNSPSCTitle"};
/*
    private static String[] sortedAttributeNames = {"aliasedObjectName", "aliasedObjectName", "c", "cert_authCertSerialNumber",
                                                    "cn","commonName","countryName","knowledgeInformation",
                                                    "l","localityName","mhsBadAddressSearchAttributes","mhsBadAddressSearchPoint",
                                                    "mhsBodyPartConversionService","objectClass","objectClass","oc",
                                                    "serialNumber","sn","st",
                                                    "stateOrProvinceName","surname"};
*/

    private static String binaryStrings = "mhsBadAddressSearchAttributes mhsBodyPartConversionService cert_authCertSerialNumber ";

    private static String[] ldapSyntaxes = {
            "( 1.3.6.1.4.1.1466.115.121.1.4 DESC 'Audio' )",
            "( 1.3.6.1.4.1.1466.115.121.1.5 DESC 'Binary' )",
            "( 1.3.6.1.4.1.1466.115.121.1.7 DESC 'Boolean' )",
            "( 1.3.6.1.4.1.1466.115.121.1.8 DESC 'Certificate' )",
            "( 1.3.6.1.4.1.1466.115.121.1.9 DESC 'Certificate List' )"};

    private static String[] nameForms = {
            "( 1.3.6.1.4.1.3327.7.1 NAME 'country-top-NF' OC country MUST ( c ) )",
            "( 1.3.6.1.4.1.3327.7.2 NAME 'o-top-NF' OC organization MUST ( o ) )",
            "( 1.3.6.1.4.1.3327.7.3 NAME 'o-country-NF' OC organization MUST ( o ) MAY ( dnQualifier ) )"};

    private static String[] objectClasses = {
            "( 2.5.6.0 NAME 'top' ABSTRACT MUST ( objectClass ) )",
            "( 2.5.6.1 NAME 'alias' SUP ( top ) STRUCTURAL MUST ( aliasedObjectName ) )",
            "( 2.5.6.2 NAME 'country' SUP ( top ) STRUCTURAL MUST ( c ) MAY ( description $ searchGuide ) )",
            "( 2.5.6.3 NAME 'locality' SUP ( top ) STRUCTURAL MAY ( description $ searchGuide $ l $ st $ street $ seeAlso ) )",
            "( 1.1.1.1.1.1 NAME 'xxxPerson' DESC 'Person im EEA GDS-System' AUXILIARY MAY ( eeaBadgeNumber $ eeaPersonalHash ) X-NDS_NOT_CONTAINER '1' )"};

    private static String[] objectClassesNames = {"top", "alias", "country", "locality", "xxxPerson"}; // note unsorted order

    private static String[] sortedObjectClassesNames = {"alias", "country", "locality", "top", "xxxPerson"}; // note sorted order

    private static String[] topLevelNames;

    private static BasicAttributes virtualSchema;

    private static String[] syntaxNames2 = new String[]{"newPilotPerson", "fred", "neuerFleigerMensh"};

    @Test
    private BasicAttribute getAttribute(String[] values, String name) {

        BasicAttribute retAtt = new BasicAttribute(name);
        for (int i = 0; i < values.length; i++)
            retAtt.add(values[i]);
        return retAtt;
    }

    @BeforeEach
    protected void setUp() {
        // virtualSchema is used to run basic tests in the absence of a live directory
        System.out.println("running stand alone tests");
        virtualSchema = new BasicAttributes();
        topLevelNames = new String[]{"attributeTypes", "ldapSyntaxes", "nameForms", "objectClasses"};
        virtualSchema.put(getAttribute(attributeTypes, topLevelNames[0]));
        virtualSchema.put(getAttribute(ldapSyntaxes, topLevelNames[1]));
        virtualSchema.put(getAttribute(nameForms, topLevelNames[2]));
        virtualSchema.put(getAttribute(objectClasses, topLevelNames[3]));
        virtualOps = new SchemaOps(virtualSchema);
    }

    private boolean initDirectory() throws NamingException {

        if (testWithDirectory) {
            System.out.println("running directory link tests");
            LdapContext ctx = null;

            ConnectionData cdata = new ConnectionData();
            cdata.setURL("ldap://betch01:19389");
            ctx = BasicOps.openContext(cdata);

            Assertions.assertNotNull(ctx);

            directorySchemaOps = new SchemaOps(ctx);

            return true;
        } else
            return false;
    }

    private boolean checkFixtures() {

        if (virtualOps == null) {
            System.out.println("skipping test - no schema ops object");
            return false;
        }

        return true;
    }

    public void testStuff() throws NamingException {

        if (!checkFixtures())
            return;

        //virtualOps.debugPrint("");
    }

    @Test

    public void testValueParser()
            throws NamingException {

        BasicAttributes atts = virtualOps.getAttributesFromSchemaValue(syntaxValue1);
        Assertions.assertNotNull(atts.get("OID"));
        Assertions.assertNotNull(atts.get("NAME"));
        Assertions.assertNotNull(atts.get("SUP"));
        Assertions.assertNotNull(atts.get("STRUCTURAL"));
        Assertions.assertNotNull(atts.get("MAY"));

        Attributes bloop = virtualOps.getAttributesFromSchemaValue(syntaxValue3);
        debugPrintAttribute("WIERD BRACKET THING", bloop);

        Enumeration mayValues = bloop.get("MAY").getAll();

        for (int i = 0; mayValues.hasMoreElements(); i++)
            Assertions.assertEquals(syntaxValue3Mays[i], mayValues.nextElement().toString());
    }

    @Test

    public void testNameParser()
            throws NamingException {

        Assertions.assertEquals("newPilotPerson", virtualOps.getNames(syntaxValue1)[0]);

        String[] names = virtualOps.getNames(syntaxValue2);
        Assertions.assertEquals(names.length, syntaxNames2.length);
        for (int i = 0; i < syntaxNames2.length; i++) {
            Assertions.assertEquals(names[i], syntaxNames2[i]);
        }

        //syntaxValue3Mays
        Assertions.assertEquals("newPilotPerson", virtualOps.getFirstName(syntaxValue1));

        Assertions.assertEquals("newPilotPerson", virtualOps.getFirstName(syntaxValue2));
    }

    @Test

    public void testObjectClasses()
            throws NamingException {

        ArrayList list = virtualOps.getKnownObjectClasses();
        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.size() == sortedObjectClassesNames.length);
        for (int i = 0; i < list.size(); i++)
            Assertions.assertTrue(list.get(i).equals(sortedObjectClassesNames[i]));
    }

    @Test

    public void testAttributeNames()
            throws NamingException {

        ArrayList list = virtualOps.attributeNames();
        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.size() == sortedAttributeNames.length);
        for (int i = 0; i < list.size(); i++)
            Assertions.assertTrue(list.get(i).equals(sortedAttributeNames[i]));
    }

    @Test

    public void testGetNewBinaryAttributes() {

        String bloop = virtualOps.getNewBinaryAttributes();
        Assertions.assertEquals(bloop, binaryStrings);
    }

    @Test

    public void testGetAttributeSyntax() {

        Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.38", virtualOps.getAttributeSyntax("objectClass"));
        Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.38", virtualOps.getAttributeSyntax("oc"));
        Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.12", virtualOps.getAttributeSyntax("aliasedObjectName"));
        Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.15", virtualOps.getAttributeSyntax("knowledgeInformation"));
    }

    @Test

    public void testDirGetAttributeSyntax() throws NamingException {

        if (initDirectory()) {
            Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.38", directorySchemaOps.getAttributeSyntax("objectClass"));
            Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.38", directorySchemaOps.getAttributeSyntax("oc"));
            Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.12", directorySchemaOps.getAttributeSyntax("aliasedObjectName"));
            Assertions.assertEquals("1.3.6.1.4.1.1466.115.121.1.15", directorySchemaOps.getAttributeSyntax("knowledgeInformation"));
        }
    }

    @Test

    public void testDirSchemaLookup() throws NamingException {

        if (initDirectory()) {
            Assertions.assertEquals("c", directorySchemaOps.schemaLookup("schema=countryName,schema=attributeTypes", "NAME"));
            Assertions.assertEquals("Certificate", directorySchemaOps.schemaLookup("schema=Certificate,schema=ldapSyntaxes", "DESC"));
        }
    }

    @Test

    public void testSchemaLookup() {

        Assertions.assertEquals("c", virtualOps.schemaLookup("schema=countryName,schema=attributeTypes", "NAME"));
        Assertions.assertEquals("Certificate", virtualOps.schemaLookup("schema=Certificate,schema=ldapSyntaxes", "DESC"));
        Assertions.assertEquals("objectClass", virtualOps.schemaLookup("AttributeDefinition/objectClass", "NAME"));
        Assertions.assertEquals("objectClass", virtualOps.schemaLookup("attributeTypes/objectClass", "NAME"));
    }

//.schemaLookup("AttributeDefinition/objectClass", "NAME");

    public void testSchemaList()
            throws NamingException {

        ArrayList nextLevel = virtualOps.listEntryNames("schema=objectClasses");
        for (int i = 0; i < nextLevel.size(); i++)
            Assertions.assertEquals(objectClassesNames[i], nextLevel.get(i));

        nextLevel = virtualOps.listEntryNames("");
        for (int i = 0; i < topLevelNames.length; i++)
            nextLevel.contains(topLevelNames[i]);

        nextLevel = virtualOps.listEntryNames("cn=schema");
        for (int i = 0; i < topLevelNames.length; i++)
            nextLevel.contains(topLevelNames[i]);
    }

    @Test

    public void testGetNameOfObjectClassAttribute() {

        Assertions.assertEquals("objectClass", virtualOps.getNameOfObjectClassAttribute());
    }

    @Test

    public void testTranslateOID() {

        Assertions.assertEquals("knowledgeInformation", virtualOps.translateOID("2.5.4.2"));
        Assertions.assertEquals("mhsBadAddressSearchPoint", virtualOps.translateOID("1.3.6.1.4.1.453.7.3.2.4"));
        Assertions.assertEquals("o-top-NF", virtualOps.translateOID("1.3.6.1.4.1.3327.7.2"));
    }

    @Test

    public void testMultipleNamedAttributes()
            throws NamingException {

        Attributes cn1 = virtualOps.getAttributesFromSchemaName("schema=commonName,schema=attributeTypes");
        Attributes cn2 = virtualOps.getAttributesFromSchemaName("schema=cn,schema=attributeTypes");

        Assertions.assertNotNull(cn1);
        Assertions.assertNotNull(cn2);
        Assertions.assertEquals(cn1.get("OID"), cn2.get("OID"));
    }

    // test parsing of:
    //"( 1.1.1.1.1.1 NAME 'xxxPerson' DESC 'Person im EEA GDS-System' AUXILIARY MAY ( eeaBadgeNumber $ eeaPersonalHash ) X-NDS_NOT_CONTAINER '1' )"};
    //and
    //"( 2.5.6.2 NAME 'country' SUP ( top ) STRUCTURAL MUST ( c ) MAY ( description $ searchGuide ) )",
    @Test

    public void testObjectClassAttributeParsing()
            throws NamingException {

        Attributes oc = virtualOps.getAttributes("schema=xxxPerson,schema=objectClasses");

        //debugPrintAttribute("XXX Person", oc);

        Assertions.assertEquals(oc.get("NAME").get(), "xxxPerson");
        Assertions.assertEquals(oc.get("DESC").get(), "Person im EEA GDS-System");

        oc = virtualOps.getAttributes("schema=country,schema=objectClasses");
        Assertions.assertEquals(oc.get("NAME").get(), "country");
        Assertions.assertEquals(oc.get("OID").get(), "2.5.6.2");
    }

    @Test

    public void testObjectClassAttributeParsingWithMixedCases()
            throws NamingException {

        Attributes oc = virtualOps.getAttributes("schema=xxxperson,schema=objectClasses");

        //debugPrintAttribute("XXX Person", oc);
        Assertions.assertNotNull(oc);
        Assertions.assertNotNull(oc.get("NAME"));

        Assertions.assertEquals(oc.get("NAME").get(), "xxxPerson");
        Assertions.assertEquals(oc.get("DESC").get(), "Person im EEA GDS-System");

        oc = virtualOps.getAttributes("schema=country,schema=objectClasses");
        Assertions.assertEquals(oc.get("NAME").get(), "country");
        Assertions.assertEquals(oc.get("OID").get(), "2.5.6.2");
    }

    public void debugPrintAttribute(String msg, Attributes atts)
            throws NamingException {

        System.out.println(msg);
        Enumeration bloop = atts.getIDs();
        while (bloop.hasMoreElements()) {
            String id = bloop.nextElement().toString();
            Enumeration vals = atts.get(id).getAll();
            while (vals.hasMoreElements())
                System.out.println("  " + id + " : " + vals.nextElement().toString());
        }
    }

    public void testGetTypeName()
            throws NamingException {

        Assertions.assertEquals("objectClasses", virtualOps.getTypeName("schema=objectClasses,cn=schema"));
        Assertions.assertEquals("attributeTypes", virtualOps.getTypeName("schema=cn,schema=attributeTypes,cn=schema"));
        Assertions.assertEquals("attributeTypes", virtualOps.getTypeName("schema=cn,schema=attributeTypes"));
    }

    public void testGetSpecificName()
            throws NamingException {

        Assertions.assertEquals("cn", virtualOps.getSpecificName("schema=cn,schema=attributeTypes,cn=schema"));
        Assertions.assertEquals("cn", virtualOps.getSpecificName("schema=cn,schema=attributeTypes"));
        Assertions.assertEquals("xxxPerson", virtualOps.getSpecificName("schema=xxxPerson,schema=objectClass"));
    }

    public void testMangleEntryName() {

        Assertions.assertEquals("schema=objectClasses", virtualOps.mangleEntryName("schema=ClassDefinition,cn=schema"));
        Assertions.assertEquals("schema=ldapSyntaxes", virtualOps.mangleEntryName("schema=SyntaxDefinition"));
        Assertions.assertEquals("schema=cn,schema=attributeTypes", virtualOps.mangleEntryName("AttributeDefinition/cn;binary"));
    }                       //   ;binary,schema=AttributeDefinition>

    @Test

    public void testIsAttributeSingleValued() {/* TE */
        Assertions.assertTrue(virtualOps.isAttributeSingleValued("uNSPSCTitle"));
        Assertions.assertTrue((!virtualOps.isAttributeSingleValued("countryName")));
    }

}

