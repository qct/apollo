package com.ctrip.framework.apollo.demo.spring.springBootDemo.refresh;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.demo.spring.springBootDemo.config.SampleRedisConfig;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@ConditionalOnProperty("redis.cache.enabled")
@Component
public class SpringBootApolloRefreshConfig {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootApolloRefreshConfig.class);

  private final SampleRedisConfig sampleRedisConfig;

  public SpringBootApolloRefreshConfig(final SampleRedisConfig sampleRedisConfig) {
    this.sampleRedisConfig = sampleRedisConfig;
  }

  @ApolloConfigChangeListener({ConfigConsts.NAMESPACE_APPLICATION, "TEST1.apollo", "application.yaml"})
  public void onChange(ConfigChangeEvent changeEvent) {
    boolean redisCacheKeysChanged = false;
    for (String changedKey : changeEvent.changedKeys()) {
      if (changedKey.startsWith("redis.cache")) {
        redisCacheKeysChanged = true;
        break;
      }
    }
    if (!redisCacheKeysChanged) {
      return;
    }

    logger.info("before refresh {}", sampleRedisConfig.toString());
    logger.info("after refresh {}", sampleRedisConfig.toString());
  }
}
