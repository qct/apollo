package com.ctrip.framework.apollo.common.config;

import com.ctrip.framework.apollo.core.enums.Env;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * default description.
 *
 * @author Damon.Q
 * @since 1.0
 */
@ConfigurationProperties(prefix = "apollo")
@Component
public class ApolloProperties {

    private Map<String, String> configServiceUrl;
    private Map<String, String> adminServiceUrl;

    public Map<String, String> getConfigServiceUrl() {
        return configServiceUrl;
    }

    public void setConfigServiceUrl(Map<String, String> configServiceUrl) {
        this.configServiceUrl = configServiceUrl;
    }

    public Map<String, String> getAdminServiceUrl() {
        return adminServiceUrl;
    }

    public void setAdminServiceUrl(Map<String, String> adminServiceUrl) {
        this.adminServiceUrl = adminServiceUrl;
    }

    public List<String> getAdminServiceUrl(Env env) {
        if (this.adminServiceUrl == null || this.adminServiceUrl.isEmpty()) {
            return Collections.emptyList();
        }
        return parseUrl(this.adminServiceUrl, env);
    }

    public List<String> getConfigServiceUrl(Env env) {
        if (this.configServiceUrl == null || this.configServiceUrl.isEmpty()) {
            return Collections.emptyList();
        }
        return parseUrl(this.configServiceUrl, env);
    }

    private List<String> parseUrl(Map<String, String> url, Env env) {
        return url.entrySet()
            .stream()
            .filter(stringStringEntry -> stringStringEntry.getKey().toUpperCase().equals(env.name()))
            .flatMap(e -> Stream.of(e.getValue().split(",")))
            .collect(Collectors.toList());
    }
}
