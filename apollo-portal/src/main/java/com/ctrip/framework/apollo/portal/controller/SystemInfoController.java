package com.ctrip.framework.apollo.portal.controller;

import com.ctrip.framework.apollo.Apollo;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.portal.component.AdminAndConfigServiceLocator;
import com.ctrip.framework.apollo.portal.component.PortalSettings;
import com.ctrip.framework.apollo.portal.component.RestTemplateFactory;
import com.ctrip.framework.apollo.portal.entity.vo.EnvironmentInfo;
import com.ctrip.framework.apollo.portal.entity.vo.SystemInfo;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/system-info")
public class SystemInfoController {

    private static final Logger logger = LoggerFactory.getLogger(SystemInfoController.class);
    private final PortalSettings portalSettings;
    private final AdminAndConfigServiceLocator serviceLocator;
    private RestTemplate restTemplate;

    public SystemInfoController(final PortalSettings portalSettings, final RestTemplateFactory restTemplateFactory,
        AdminAndConfigServiceLocator serviceLocator) {
        this.portalSettings = portalSettings;
        this.restTemplate = restTemplateFactory.getObject();
        this.serviceLocator = serviceLocator;
    }

    @PreAuthorize(value = "@permissionValidator.isSuperAdmin()")
    @GetMapping
    public SystemInfo getSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();

        String version = Apollo.VERSION;
        if (isValidVersion(version)) {
            systemInfo.setVersion(version);
        }

        List<Env> allEnvList = portalSettings.getAllEnvs();

        for (Env env : allEnvList) {
            EnvironmentInfo environmentInfo = new EnvironmentInfo();
            environmentInfo.setEnv(env);
            environmentInfo.setActive(portalSettings.isEnvActive(env));

            try {
                environmentInfo.setConfigServices(serviceLocator.getConfigService(env).toArray(new ServiceDTO[0]));
                environmentInfo.setAdminServices(serviceLocator.getAdminService(env).toArray(new ServiceDTO[0]));
            } catch (Throwable ex) {
                String errorMessage = "Loading config/admin services from meta server failed!";
                logger.error(errorMessage, ex);
                environmentInfo.setErrorMessage(errorMessage + " Exception: " + ex.getMessage());
            }
            systemInfo.addEnvironment(environmentInfo);
        }
        return systemInfo;
    }

    @PreAuthorize(value = "@permissionValidator.isSuperAdmin()")
    @GetMapping(value = "/health")
    public Health checkHealth(@RequestParam String host) {
        return restTemplate.getForObject(host + "/health", Health.class);
    }

    private boolean isValidVersion(String version) {
        return !version.equals("java-null");
    }
}
