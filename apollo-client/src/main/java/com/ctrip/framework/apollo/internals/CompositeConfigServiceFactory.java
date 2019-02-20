package com.ctrip.framework.apollo.internals;

import com.ctrip.framework.apollo.ConfigServiceFactory;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Default config service factory, load config service in order below.
 * <ol>
 * <li>Get config service from Java System properties.</li>
 * <li>Get config service from OS environment variables.</li>
 * <li>Get config service from Spring Boot Environment.</li>
 * <li>Get config service from META-INF/apollo.properties.</li>
 * </ol>
 *
 * @author qct
 * @since 2.0.0
 */
public class CompositeConfigServiceFactory implements ConfigServiceFactory {

    private final List<ConfigServiceFactory> factories = new ArrayList<>();

    CompositeConfigServiceFactory() {
        factories.add(new JavaPropertyConfigServiceFactory());
        factories.add(new OsEnvironmentConfigServiceFactory());
        factories.add(new FileConfigServiceFactory());
    }

    public List<ConfigServiceFactory> getFactories() {
        return factories;
    }

    @Override
    public List<ServiceDTO> getConfigServices() {
        for (ConfigServiceFactory factory : this.factories) {
            List<ServiceDTO> configServices = factory.getConfigServices();
            if (configServices != null && !configServices.isEmpty()) {
                return configServices;
            }
        }
        return null;
    }
}
