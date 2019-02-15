package com.ctrip.framework.apollo.internals;

import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import java.util.List;

/**
 * Only one place to locate & provide config service.
 *
 * @author Damon.Q
 * @since 1.0
 */
public interface ConfigServiceFactory {

    /**
     * Get the config service info.
     *
     * @return the services dto
     */
    List<ServiceDTO> getConfigServices();
}
