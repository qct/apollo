package com.ctrip.framework.apollo.internals;

import static com.ctrip.framework.apollo.core.ConfigConsts.CONFIG_SERVICE_URL_PREFIX;

import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.core.utils.ClassLoaderUtil;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.ctrip.framework.apollo.util.ConfigUtil;
import com.ctrip.framework.foundation.internals.io.BOMInputStream;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultConfigServiceFactory implements ConfigServiceFactory {

    public static final String APOLLO_PROPERTIES_CLASSPATH = "META-INF/apollo.properties";

    private static final Logger logger = LoggerFactory.getLogger(DefaultConfigServiceFactory.class);

    private static final Map<String, List<ServiceDTO>> configServiceCache = new ConcurrentHashMap<>();

    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(APOLLO_PROPERTIES_CLASSPATH);
            if (in == null) {
                in = DefaultConfigServiceFactory.class.getResourceAsStream(APOLLO_PROPERTIES_CLASSPATH);
            }

            if (in != null) {
                try {
                    PROPERTIES.load(new InputStreamReader(new BOMInputStream(in), StandardCharsets.UTF_8));
                } finally {
                    in.close();
                }
            }
        } catch (Exception ex) {
            logger.debug("Unable to load apollo config from location [" + APOLLO_PROPERTIES_CLASSPATH + "]");
        }
    }

    private ConfigUtil configUtil = ApolloInjector.getInstance(ConfigUtil.class);

    public DefaultConfigServiceFactory() {
        initConfigServices();
    }

    private void initConfigServices() {
        Lists.newArrayList("dev", "fat", "uat", "prod", "default").forEach(env -> {
            String key = "default".equals(env) ? CONFIG_SERVICE_URL_PREFIX : CONFIG_SERVICE_URL_PREFIX + "." + env;
            // 1. Get from Java System properties.
            String url = System.getProperty(key.toLowerCase());
            if (Strings.isNullOrEmpty(url)) {
                url = System.getProperty(key.toUpperCase());
            }

            // 2. Get from OS environment variables.
            if (Strings.isNullOrEmpty(url)) {
                url = System.getenv(key.toLowerCase());
                if (Strings.isNullOrEmpty(url)) {
                    url = System.getenv(key.toUpperCase());
                }
            }

            // 3. Get from application.properties(spring boot)
            boolean isSpringBoot = deduceFromClasspath();
            if (isSpringBoot) {
                // TODO set config service url from application.properties
            }

            // 4. Get from META-INF/apollo.properties
            if (Strings.isNullOrEmpty(url)) {
                url = PROPERTIES.getProperty(key);
            }

            if (!Strings.isNullOrEmpty(url)) {
                List<ServiceDTO> services = toServices(url);
                configServiceCache.put(env, services);
                logConfigServices(services);
                logger.info("Config service initialized: env({}): {}", env, url);
            }
        });
    }

    private boolean deduceFromClasspath() {
        return ClassLoaderUtil.isClassPresent("org.springframework.boot.SpringApplication", null);
    }

    @Override
    public List<ServiceDTO> getConfigServices() {
        Env env = configUtil.getApolloEnv();
        List<ServiceDTO> services =
            env == Env.UNKNOWN ? configServiceCache.get("default") : configServiceCache.get(env.name());
        if (services == null || services.isEmpty()) {
            return Collections.emptyList();
        }
        return services;
    }

    private List<ServiceDTO> toServices(String configServiceUrl) {
        if (!configServiceUrl.contains(",")) {
            ServiceDTO service = new ServiceDTO();
            service.setHomepageUrl(configServiceUrl);
            return Collections.singletonList(service);
        }
        return Arrays.stream(configServiceUrl.split(","))
            .map(url -> {
                ServiceDTO service = new ServiceDTO();
                service.setHomepageUrl(url.trim());
                return service;
            })
            .collect(Collectors.toList());
    }

    private void logConfigServices(List<ServiceDTO> services) {
        for (ServiceDTO serviceDto : services) {
            logConfigService(serviceDto.getHomepageUrl());
        }
    }

    private void logConfigService(String serviceUrl) {
        Tracer.logEvent("Apollo.Config.Services", serviceUrl);
    }
}
