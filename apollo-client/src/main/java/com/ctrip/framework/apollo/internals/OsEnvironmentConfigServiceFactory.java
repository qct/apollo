package com.ctrip.framework.apollo.internals;

import static com.ctrip.framework.apollo.core.ConfigConsts.CONFIG_SERVICE_URL_PREFIX;

import com.ctrip.framework.apollo.ConfigServiceFactory;
import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.util.ConfigUtil;
import com.google.common.base.Strings;
import java.util.Collections;
import java.util.List;

/**
 * Get config service from OS environment variables.
 *
 * @author qct
 * @since 2.0.0
 */
public class OsEnvironmentConfigServiceFactory implements ConfigServiceFactory {

    private static final int CONFIG_SERVICE_FACTORY_ORDER = -2;

    private ConfigUtil configUtil = ApolloInjector.getInstance(ConfigUtil.class);

    @Override
    public List<ServiceDTO> getConfigServices() {
        Env env = configUtil.getApolloEnv();
        String url;
        if (Env.UNKNOWN == env) {
            url = getPropertyIgnoreCase(CONFIG_SERVICE_URL_PREFIX);
        } else {
            url = getPropertyIgnoreCase(CONFIG_SERVICE_URL_PREFIX + "." + env);
            if (Strings.isNullOrEmpty(url)) {
                url = getPropertyIgnoreCase(CONFIG_SERVICE_URL_PREFIX);
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

    private String getPropertyIgnoreCase(String key) {
        String url = System.getenv(key.toUpperCase());
        if (!Strings.isNullOrEmpty(url)) {
            return url;
        }
        return System.getenv(key.toLowerCase());
    }
}
