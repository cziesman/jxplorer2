/**
 * Created by IntelliJ IDEA.
 * User: erstr01
 * Date: Mar 19, 2003
 * Time: 11:45:12 AM
 * To change this template use Options | File Templates.
 */
package com.ca.directory.jxplorer.viewer.tableviewer;

import com.ca.commons.naming.DN;
import com.ca.commons.naming.DXAttribute;
import com.ca.commons.naming.DXEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;

public class AttributeTableModelTest {

    /**
     * Set up a test AttributeTableModel with some standard attributes and a multi-valued RDN.
     *
     * @return a test AttributeTableModel
     */

    private AttributeTableModel getStandardTestModel() {

        DXEntry entry = new DXEntry(new DN("cn=john+sn=smith,ou=pig botherers,o=pig corp"));

        DXAttribute cn = new DXAttribute("cn", "john");
        cn.add("fred");
        cn.add("nigel");

        DXAttribute sn = new DXAttribute("sn", "smith");
        sn.add("john");
        sn.add("fred");

        DXAttribute objectClass = new DXAttribute("objectClass", "top");
        objectClass.add("person");
        objectClass.add("inetOrgPerson");

        DXAttribute favouriteDrink = new DXAttribute("favouriteDrink", "Midori");
        objectClass.add("Cointreau");
        objectClass.add("Lemon Juice");
        objectClass.add("Mmm... Japanese Slipper...");

        entry.put(cn);
        entry.put(sn);
        entry.put(objectClass);
        entry.put("eyeColour", "purple");
        entry.put("mySocks", "moldy");

        AttributeTableModel model = new AttributeTableModel();
        model.insertAttributes(entry);
        return model;
    }

    @Test
    public void testGetRDN() {

        AttributeTableModel model = getStandardTestModel();
        Assertions.assertEquals("cn=john+sn=smith", model.getRDN().toString());
    }

    @Test
    public void testRemoveNamingComponent() {

        AttributeTableModel model = getStandardTestModel();
        model.removeNamingComponent(new AttributeNameAndType("sn", true), new AttributeValue(new DXAttribute("sn", "smith"), "smith"));
        Assertions.assertEquals("cn=john", model.getRDN().toString());
        Assertions.assertEquals(model.namingTypes.length, 1);
        Assertions.assertEquals(model.namingRawValues.length, 1);
        Assertions.assertEquals(model.numberNamingValues, 1);
    }

    @Test
    public void testAddNamingComponent() {

        AttributeTableModel model = getStandardTestModel();

        int row = getSpecificAttributeValueRow(model, "eyeColour", "purple");

        Assertions.assertTrue((row != -1));
        AttributeValue val = (AttributeValue) model.attributeValues.get(row);
        AttributeNameAndType type = (AttributeNameAndType) model.attributeTypes.get(row);

        model.addNamingComponent(type, val);
        Assertions.assertEquals("cn=john+eyecolour=purple+sn=smith", model.getRDN().toString());

        Assertions.assertEquals(model.namingTypes.length, 3);
        Assertions.assertEquals(model.namingRawValues.length, 3);
        Assertions.assertEquals(model.numberNamingValues, 3);
    }

    private int getSpecificAttributeValueRow(AttributeTableModel model, String id, String value) {

        AttributeValue val = null;
        for (int i = 0; i < model.attributeValues.size(); i++) {
            val = (AttributeValue) model.attributeValues.get(i);
            //System.out.println("searching : " + val.getID().toString() + ":" + val.getStringValue() + " for " + id + ":" + value);
            if (val.getID().equalsIgnoreCase(id) && val.getStringValue().equals(value)) {
                return i;
            }
        }
        return -1;
    }

    @Test
    public void testChangeMultiValuesNamingComponentValue() {

        AttributeTableModel model = getStandardTestModel();

        // find which row is 'cn=john', and change it...
        int row = getSpecificAttributeValueRow(model, "cn", "john");

        Assertions.assertTrue((row != -1));
        model.setValueAt("erick", row, 1);
        Assertions.assertEquals("erick", model.getValueAt(row, 1).toString());
        Assertions.assertEquals("cn=erick+sn=smith", model.getRDN().toString());
    }

    public void testChangeSingleValuedNamingComponentValue() {

        AttributeTableModel model = getStandardTestModel();

        model.removeNamingComponent(new AttributeNameAndType("sn", true), new AttributeValue(new DXAttribute("sn", "smith"), "smith"));
        // find which row is 'cn=john', and change it...
        int row = -1;
        for (int i = 0; i < model.attributeValues.size(); i++) {
            AttributeValue val = (AttributeValue) model.attributeValues.get(i);
            if (val.getID().equals("cn") && val.getStringValue().equals("john")) {
                row = i;
                break;
            }
        }
        Assertions.assertTrue((row != -1));
        model.setValueAt("erick", row, 1);
        Assertions.assertEquals("erick", model.getValueAt(row, 1).toString());

        Assertions.assertEquals("cn=erick", model.getRDN().toString());
    }

    @Test
    public void testRemoveRowFromArray() throws NamingException {

        int testElement1 = 0;
        String[] test1 = new String[]{"0", "1", "2", "3", "4"};
        String[] test1b = new String[]{"1", "2", "3", "4"};
        int testElement2 = 3;
        String[] test2 = new String[]{"0", "1", "2", "3", "4"};
        String[] test2b = new String[]{"0", "1", "2", "4"};
        int testElement3 = 4;
        String[] test3 = new String[]{"0", "1", "2", "3", "4"};
        String[] test3b = new String[]{"0", "1", "2", "3"};

        // test first element removal
        Assertions.assertArrayEquals(test1b, AttributeTableModel.removeRowFromArray(test1, testElement1));

        // test mid element removal
        Assertions.assertArrayEquals(test2b, AttributeTableModel.removeRowFromArray(test2, testElement2));

        // test end element removal
        Assertions.assertArrayEquals(test3b, AttributeTableModel.removeRowFromArray(test3, testElement3));

        // test lower out-of-bounds error
        Assertions.assertArrayEquals(test1, AttributeTableModel.removeRowFromArray(test1, -1));

        // test upper    out-of-bounds error
        Assertions.assertArrayEquals(test1, AttributeTableModel.removeRowFromArray(test1, test1.length));
    }

}
