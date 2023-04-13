package com.ca.commons.naming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.directory.BasicAttribute;

/**
 * Tests Ldif Modify Attributes print out correctly as per RFC 2849
 * <p>
 * (e.g. the 'add|delete|replace' elements below)
 *
 * <pre>
 * dn: cn=Paula Jensen, ou=Product Development, dc=airius, dc=com
 * changetype: modify
 * add: postaladdress
 * postaladdress: 123 Anystreet $ Sunnyvale, CA $ 94086
 * -
 * delete: description
 * -
 * replace: telephonenumber
 * telephonenumber: +1 408 555 1234
 * telephonenumber: +1 408 555 5678
 * -
 * delete: facsimiletelephonenumber
 * facsimiletelephonenumber: +1 408 555 9876
 * -
 * </pre>
 * <p>
 * (c) Chris Betts; Pegacat Software (http://pegacat.com)
 */
public class LdifModifyAttributeTest {

    @Test
    public void testAddAttribute() {

        LdifModifyAttribute add = new LdifModifyAttribute(new BasicAttribute("postaladdress", "123 Anystreet $ Sunnyvale, CA $ 94086"), LdifModifyType.add);

        Assertions.assertEquals(
                "add: postaladdress\n" +
                        "postaladdress: 123 Anystreet $ Sunnyvale, CA $ 94086\n" +
                        "-\n",
                add.toString());
    }

    @Test
    public void testDeleteAttribute() {

        LdifModifyAttribute delete = new LdifModifyAttribute(new BasicAttribute("facsimiletelephonenumber", "+1 408 555 9876"), LdifModifyType.delete);

        Assertions.assertEquals(
                "delete: facsimiletelephonenumber\n" +
                        "facsimiletelephonenumber: +1 408 555 9876\n" +
                        "-\n",
                delete.toString());

        delete = new LdifModifyAttribute(new BasicAttribute("description"), LdifModifyType.delete);

        Assertions.assertEquals(
                "delete: description\n" +
                        "-\n",
                delete.toString());
    }

    @Test
    public void testReplaceAttribute() {

        LdifModifyAttribute replace = new LdifModifyAttribute(new DXAttribute("telephonenumber", new String[]{"+1 408 555 1234", "+1 408 555 5678"}), LdifModifyType.replace);

        Assertions.assertEquals(
                "replace: telephonenumber\n" +
                        "telephonenumber: +1 408 555 1234\n" +
                        "telephonenumber: +1 408 555 5678\n" +
                        "-\n",
                replace.toString());
    }

}

