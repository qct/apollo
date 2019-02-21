package com.ctrip.framework.apollo.internals;

import static com.ctrip.framework.apollo.core.ConfigConsts.CONFIG_SERVICE_URL_PREFIX;

import com.ctrip.framework.apollo.ConfigServiceFactory;
import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.util.ConfigUtil;
import com.google.common.base.Strings;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/**
 * Get config service from Spring Boot Environment.
 *
 * @author qct
 * @since 2.0.0
 */
public class SpringConfigServiceFactory implements ConfigServiceFactory, ApplicationContextInitializer, Ordered {

    private static final int APPLICATION_CONTEXT_INITIALIZER_ORDER = 1;

    private static final int CONFIG_SERVICE_FACTORY_ORDER = -2;

    private ConfigUtil configUtil = ApolloInjector.getInstance(ConfigUtil.class);

    private Environment environment;

    //TODO default to 4 envs(dev, fat, uat, prod)
    @Override
    public List<ServiceDTO> getConfigServices() {
        Env env = configUtil.getApolloEnv();
        String url;
        if (Env.UNKNOWN == env) {
            url = this.environment.getProperty(CONFIG_SERVICE_URL_PREFIX);
        } else {
            url = this.environment.getProperty(CONFIG_SERVICE_URL_PREFIX + "." + env);
            if (Strings.isNullOrEmpty(url)) {
                url = this.environment.getProperty(CONFIG_SERVICE_URL_PREFIX);
            }
        }

        if (Strings.isNullOrEmpty(url)) {
            return Collections.emptyList();
        }
        return toServices(url);
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        this.environment = configurableApplicationContext.getEnvironment();
        CompositeConfigServiceFactory instance = ApolloInjector.getInstance(CompositeConfigServiceFactory.class);
        if (instance != null) {
            List<ConfigServiceFactory> factories = instance.getFactories();
            boolean exists = factories.stream().anyMatch(x -> x instanceof SpringConfigServiceFactory);
            if (exists) {
                return;
            }
            factories.add(this);
            factories.sort(Comparator.comparingInt(ConfigServiceFactory::order));
        }
    }

    @Override
    public int getOrder() {
        return APPLICATION_CONTEXT_INITIALIZER_ORDER;
    }

    @Override
    public int order() {
        return CONFIG_SERVICE_FACTORY_ORDER;
    }
}
