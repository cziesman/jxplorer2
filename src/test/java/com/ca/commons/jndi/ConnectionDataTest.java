/**
 * Created by IntelliJ IDEA.
 * User: erstr01
 * Date: Dec 20, 2002
 * Time: 3:45:33 PM
 * To change this template use Options | File Templates.
 */
package com.ca.commons.jndi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConnectionDataTest {

    @Test
    public void testGetPort() {

        ConnectionData testConnection;
        testConnection = new ConnectionData();
        testConnection.setURL("ldap://bloop:19389");
        Assertions.assertEquals(19389, testConnection.getPort());
        testConnection.setProtocol(ConnectionData.DSML);
        testConnection.setURL("dsml://bloop:19389/myserver/stuff");
        Assertions.assertEquals(19389, testConnection.getPort());
        testConnection.setURL("dsml://fnord.ca.com:19389/myserver/stuff");
        Assertions.assertEquals(19389, testConnection.getPort());
        testConnection.setURL("dsml://fnord.ca.com:66000/myserver/stuff");
        Assertions.assertEquals(-1, testConnection.getPort());
        testConnection.setURL("dsml://fnord.ca.com:-500/myserver/stuff");
        Assertions.assertEquals(-1, testConnection.getPort());
    }

    @Test
    public void testGetHost() {

        ConnectionData testConnection, dsmlURL;
        testConnection = new ConnectionData();
        testConnection.setURL("ldap://bloop:19389");
        Assertions.assertEquals("bloop", testConnection.getHost());
        testConnection.setProtocol(ConnectionData.DSML);
        testConnection.setURL("http://bloop:19389/myserver/stuff");
        Assertions.assertEquals("bloop", testConnection.getHost());
        testConnection.setURL("http://fnord.ca.com:19389/myserver/stuff");
        Assertions.assertEquals("fnord.ca.com", testConnection.getHost());
    }

}
