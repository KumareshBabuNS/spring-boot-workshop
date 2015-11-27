package info.novatec.spring.boot;

import info.novatec.spring.boot.config.ApplicationProperties;
import info.novatec.spring.boot.jpa.Person;
import info.novatec.spring.boot.jpa.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

@EnableConfigurationProperties(ApplicationProperties.class)
@SpringBootApplication
public class SpringBootWorkshop3Application implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger ( SpringBootWorkshop3Application.class );

    @Autowired
    private PersonService personService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWorkshop3Application.class, args);
    }

    @Override
    public void run ( String... args ) throws Exception {

        personService.save(new Person ( "Hans", "Mustermann" ));
        personService.save(new Person ( "Peter", "Müller" ));
        personService.save(new Person ( "Sabine", "Maier" ));
        personService.save(new Person ( "Claudia", "Bäcker" ));

        LOGGER.info ("Created {} persons in database", personService.count () );

        List<Person> persons = personService.findAll ();
        LOGGER.info ("Found persons {}", persons);

        persons = personService.findAllByFirstNameAndLastName ( "Hans", "Mustermann" );
        LOGGER.info ("Found person by first and last name {}", persons);

    }
}
