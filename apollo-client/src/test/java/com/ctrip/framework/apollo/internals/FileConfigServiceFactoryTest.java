package com.ctrip.framework.apollo.internals;

import static org.junit.Assert.assertEquals;

import com.ctrip.framework.apollo.ConfigServiceFactory;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import java.util.List;
import org.junit.Test;

/**
 * default description.
 *
 * @author qct
 * @since 2.0.0
 */
public class FileConfigServiceFactoryTest {

    @Test
    public void shouldReturnRightResult() {
        ConfigServiceFactory factory = new FileConfigServiceFactory();
        List<ServiceDTO> configServices = factory.getConfigServices();
        assertEquals(1, configServices.size());
        assertEquals("http://test-url:8080", configServices.get(0).getHomepageUrl());
    }
}
