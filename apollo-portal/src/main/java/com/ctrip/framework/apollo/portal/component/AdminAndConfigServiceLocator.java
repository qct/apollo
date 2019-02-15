package com.ctrip.framework.apollo.portal.component;

import com.ctrip.framework.apollo.common.config.ApolloProperties;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class AdminAndConfigServiceLocator {

    private static final Logger logger = LoggerFactory.getLogger(AdminAndConfigServiceLocator.class);
    private final PortalSettings portalSettings;
    private final ApolloProperties apolloProperties;
    private Map<Env, List<ServiceDTO>> adminServiceCache = new ConcurrentHashMap<>();
    private Map<Env, List<ServiceDTO>> configServiceCache = new ConcurrentHashMap<>();

    public AdminAndConfigServiceLocator(final PortalSettings portalSettings, ApolloProperties apolloProperties) {
        this.portalSettings = portalSettings;
        this.apolloProperties = apolloProperties;
    }

    @PostConstruct
    public void init() {
        for (Env env : portalSettings.getAllEnvs()) {
            refreshServiceCache(env);
        }
    }

    public List<ServiceDTO> getAdminService(Env env) {
        return shuffle(adminServiceCache.get(env));
    }

    public List<ServiceDTO> getConfigService(Env env) {
        return shuffle(configServiceCache.get(env));
    }

    private List<ServiceDTO> shuffle(List<ServiceDTO> services) {
        if (CollectionUtils.isEmpty(services)) {
            return Collections.emptyList();
        }
        List<ServiceDTO> randomConfigServices = Lists.newArrayList(services);
        Collections.shuffle(randomConfigServices);
        return randomConfigServices;
    }

    private void refreshServiceCache(Env env) {
        try {
            List<ServiceDTO> adminServices = refreshAdminService(env);
            if (!adminServices.isEmpty()) {
                adminServiceCache.put(env, adminServices);
            }

            List<ServiceDTO> configServices = refreshConfigService(env);
            if (!configServices.isEmpty()) {
                configServiceCache.put(env, configServices);
            }
        } catch (Throwable e) {
            logger.error(String.format("Get admin server address failed. env: %s", env), e);
            Tracer.logError(String.format("Get admin server address failed. env: %s", env), e);
        }
    }

    private List<ServiceDTO> refreshConfigService(Env env) {
        List<String> configServiceUrl = apolloProperties.getConfigServiceUrl(env);
        return toServiceDTO(configServiceUrl);
    }

    private List<ServiceDTO> refreshAdminService(Env env) {
        List<String> adminServiceUrl = apolloProperties.getAdminServiceUrl(env);
        return toServiceDTO(adminServiceUrl);
    }

    private List<ServiceDTO> toServiceDTO(List<String> serviceUrl) {
        return serviceUrl.stream()
            .map(s -> {
                ServiceDTO serviceDTO = new ServiceDTO();
                serviceDTO.setHomepageUrl(s);
                return serviceDTO;
            })
            .collect(Collectors.toList());
    }
}
