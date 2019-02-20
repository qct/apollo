package com.ctrip.framework.apollo.internals;

import static org.junit.Assert.assertEquals;

import com.ctrip.framework.apollo.ConfigServiceFactory;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import java.util.List;
import org.junit.After;
import org.junit.Test;

/**
 * Tests for {@link CompositeConfigServiceFactory}.
 *
 * @author qct
 */
public class CompositeConfigServiceFactoryTest {

    @After
    public void tearDown() {
        System.clearProperty("apollo.config-service-url");
    }

    @Test
    public void systemPropertyWins() {
        System.setProperty("apollo.config-service-url", "http://some-url");
        ConfigServiceFactory configServiceFactory = new CompositeConfigServiceFactory();
        List<ServiceDTO> configServices = configServiceFactory.getConfigServices();
        assertEquals(1, configServices.size());
        assertEquals("http://some-url", configServices.get(0).getHomepageUrl());
    }

    @Test
    public void propertiesFileWins() {
        ConfigServiceFactory configServiceFactory = new CompositeConfigServiceFactory();
        List<ServiceDTO> configServices = configServiceFactory.getConfigServices();
        assertEquals(1, configServices.size());
        assertEquals("http://test-url:8080", configServices.get(0).getHomepageUrl());
    }
}
