package sample;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * default description.
 *
 * @author qct
 * @since 2.0.0
 */
@SpringBootApplication
public class ApolloSpringBootSampleApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApolloSpringBootSampleApplication.class);

    public static void main(String[] args) {
        System.out.println("This is apollo with spring boot sample");
        SpringApplication.run(ApolloSpringBootSampleApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        Config config = ConfigService.getAppConfig();
        while (true) {
            logger.debug("Load from apollo, config size: {}", config.getPropertyNames());
            Thread.sleep(5000L);
        }
    }
}
