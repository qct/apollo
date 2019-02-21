package com.ctrip.framework.apollo.internals;

import static org.junit.Assert.assertEquals;

import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.spring.AbstractSpringIntegrationTest;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * default description.
 *
 * @author Damon.Q
 * @since 1.0
 */
public class SpringConfigServiceFactoryTest extends AbstractSpringIntegrationTest {

    private ConfigurableApplicationContext context;

    @After
    public void cleanUp() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void shouldReturnRightResult() {
        SpringApplication application = new SpringApplication(Config.class);
        this.context = application.run("--apollo.config-service-url=http://some-url.com");
        SpringConfigServiceFactory factory = new SpringConfigServiceFactory();
        factory.initialize(this.context);
        List<ServiceDTO> configServices = factory.getConfigServices();
        assertEquals(1, configServices.size());
        assertEquals("http://some-url.com", configServices.get(0).getHomepageUrl());
    }

    @Test
    public void shouldReturnEmptyResult() {
        SpringApplication application = new SpringApplication(Config.class);
        this.context = application.run();
        SpringConfigServiceFactory factory = new SpringConfigServiceFactory();
        factory.initialize(this.context);
        List<ServiceDTO> configServices = factory.getConfigServices();
        assertEquals(0, configServices.size());
    }

    @Configuration
    protected static class Config {

    }
}
