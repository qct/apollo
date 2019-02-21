package com.ctrip.framework.apollo.internals;

import static com.ctrip.framework.apollo.core.ConfigConsts.CONFIG_SERVICE_URL_PREFIX;

import com.ctrip.framework.apollo.ConfigServiceFactory;
import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.util.ConfigUtil;
import com.ctrip.framework.foundation.internals.io.BOMInputStream;
import com.google.common.base.Strings;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get config service from META-INF/apollo.properties.
 *
 * @author qct
 * @since 2.0.0
 */
public class FileConfigServiceFactory implements ConfigServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileConfigServiceFactory.class);

    private static final int CONFIG_SERVICE_FACTORY_ORDER = -1;

    private static final String APOLLO_PROPERTIES_CLASSPATH = "META-INF/apollo.properties";

    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(APOLLO_PROPERTIES_CLASSPATH);
            if (in == null) {
                in = FileConfigServiceFactory.class.getResourceAsStream(APOLLO_PROPERTIES_CLASSPATH);
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

    @Override
    public List<ServiceDTO> getConfigServices() {
        Env env = configUtil.getApolloEnv();
        String url;
        if (Env.UNKNOWN == env) {
            url = PROPERTIES.getProperty(CONFIG_SERVICE_URL_PREFIX);
        } else {
            url = PROPERTIES.getProperty(CONFIG_SERVICE_URL_PREFIX + "." + env);
            if (Strings.isNullOrEmpty(url)) {
                url = PROPERTIES.getProperty(CONFIG_SERVICE_URL_PREFIX);
            }
        }

        if (Strings.isNullOrEmpty(url)) {
            return Collections.emptyList();
        }
        return toServices(url);
    }

    @Override
    public int order() {
        return CONFIG_SERVICE_FACTORY_ORDER;
    }
}
