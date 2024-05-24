package sit.int204.resurrections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import sit.int204.resurrections.configs.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class ResurrectionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResurrectionsApplication.class, args);
    }

}
