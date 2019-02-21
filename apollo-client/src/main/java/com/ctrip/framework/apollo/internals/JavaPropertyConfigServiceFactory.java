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
 * Get config service from Java System properties.
 *
 * @author qct
 * @since 2.0.0
 */
public class JavaPropertyConfigServiceFactory implements ConfigServiceFactory {

    private static final int CONFIG_SERVICE_FACTORY_ORDER = -4;

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

    private String getPropertyIgnoreCase(String key) {
        return System.getProperty(key.toUpperCase(), System.getProperty(key.toLowerCase()));
    }

    @Override
    public int order() {
        return CONFIG_SERVICE_FACTORY_ORDER;
    }
}
