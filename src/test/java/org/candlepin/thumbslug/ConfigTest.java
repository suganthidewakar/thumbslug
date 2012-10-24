/**
 * Copyright (c) 2011 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */

package org.candlepin.thumbslug;

import java.util.Properties;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * ConfigTest
 */
public class ConfigTest {

    @Test(expected = RuntimeException.class)
    public void testRuntimeExceptionOnBadConfigLookup() {
        Config config = new Config();
        config.getBoolean("foofoofoofoo");
    }

    @Test
    public void testConfigGetString() {
        Config config = new Config();
        String result = config.getProperty("log.access");
        assertEquals(result, "/var/log/thumbslug/access.log");
    }

    @Test
    public void testConfigGetLogging() {
        Config config = new Config();
        Properties props = config.getLoggingConfig();
        // No logging in default config
        assertTrue(props.isEmpty());
    }

    @Test
    public void TestConfigGetNamespaceProperties() {
        Config config = new Config();
        Properties props = config.getNamespaceProperties("");
        assertFalse(props.isEmpty());

        Properties noProps = config.getNamespaceProperties("this.does.not.exist");
        assertTrue(noProps.isEmpty());

        Properties sslProps = config.getNamespaceProperties("ssl.client");
        assertEquals(2, sslProps.entrySet().size());
    }


}
