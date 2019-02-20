package com.ctrip.framework.apollo.internals;

import static org.junit.Assert.assertEquals;

import com.ctrip.framework.apollo.ConfigServiceFactory;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import java.util.List;
import org.junit.After;
import org.junit.Test;

/**
 * default description.
 *
 * @author qct
 * @since 2.0.0
 */
public class JavaPropertyConfigServiceFactoryTest {

    @After
    public void tearDown() {
        System.clearProperty("apollo.config-service-url");
    }

    @Test
    public void shouldReturnRightResult() {
        System.setProperty("apollo.config-service-url", "http://some-url");
        ConfigServiceFactory factory = new JavaPropertyConfigServiceFactory();
        List<ServiceDTO> configServices = factory.getConfigServices();
        assertEquals(1, configServices.size());
        assertEquals("http://some-url", configServices.get(0).getHomepageUrl());
    }

    @Test
    public void shouldReturnEmptyResult() {
        System.clearProperty("apollo.config-service-url");
        ConfigServiceFactory factory = new JavaPropertyConfigServiceFactory();
        List<ServiceDTO> configServices = factory.getConfigServices();
        assertEquals(0, configServices.size());
    }
}
