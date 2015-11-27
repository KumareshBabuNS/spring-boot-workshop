package info.novatec.spring.boot;

import info.novatec.spring.boot.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(ApplicationProperties.class)
@SpringBootApplication
public class SpringBootWorkshop2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWorkshop2Application.class, args);
    }
}
