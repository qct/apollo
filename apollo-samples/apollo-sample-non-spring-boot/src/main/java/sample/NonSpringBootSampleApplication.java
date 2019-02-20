package sample;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * default description.
 *
 * @author qct
 * @since 2.0.0
 */
public class NonSpringBootSampleApplication {

    private static final Logger logger = LoggerFactory.getLogger(NonSpringBootSampleApplication.class);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("This is non spring boot sample");
        Config config = ConfigService.getAppConfig();
        while (true) {
            logger.debug("Load from apollo, config size: {}", config.getPropertyNames());
            Thread.sleep(5000L);
        }
    }
}
