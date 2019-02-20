package com.ctrip.framework.apollo;

import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The only place to locate & provide config service.
 *
 * @author qct
 * @since 2.0.0
 */
public interface ConfigServiceFactory {

    /**
     * Get the config service info.
     *
     * @return the services dto
     */
    List<ServiceDTO> getConfigServices();

    /**
     * Parse url string to config service list.
     *
     * @param configServiceUrl url to be parsed
     * @return config service list
     */
    default List<ServiceDTO> toServices(String configServiceUrl) {
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

    default int order() {
        return Integer.MAX_VALUE;
    }
}
