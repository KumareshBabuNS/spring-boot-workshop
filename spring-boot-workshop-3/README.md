Spring Boot Lab 3
=================

Im dritten Lab wird die nun gesicherte Spring Boot Web Anwendung aus Lab 2 um Persistenz auf Basis von JPA 2.1 erweitert.
Gleichzeitig wird die Autokonfiguration für eine DB-Migration mit Flyway gezeigt.
Schließlich werden die Entitäten dann auch über REST verfügbar gemacht (inkl. HAL Browser).
 
1.Projekterstellung
-------------------
 
Die Erstellung einer Spring Boot Anwendung wird mit Hilfe des [Spring Initializr](http://start.spring.io/) durchgeführt.  
Alternativ kann bei Benutzung einer der folgenden Java IDEs auch über den dortigen Spring Initializr Wizard ein neues Projekt erstellt werden.

* Spring Toolsuite ab Version 3.7 (Eclipse basiert)
* IntelliJ ab Version 14.1

Nach Eingabe der üblichen Maven Informationen und der Auswahl der Spring Boot Version (wir nutzen die letzte verfügbare) 
können dann die gewünschten Features für die Anwendung ausgewählt werden.  
Für unser Lab wollen wir die folgenden Features auswählen 
(entweder durch freie Eingabe im *Search for dependencies* Eingabefeld oder durch Umschalten auf die Vollversion durch den entsprechenden 
Link *Switch to the full version*:

* Web
* Security
* DevTools
* Actuator
* HATEOAS
* JPA
* H2
* Rest Repositories
* Rest Repositories HAL Browser

1.1.Erweiterung des Projekts aus Lab 2
--------------------------------------

Wird auf Basis des Projektes aus Lab 2 weiter gemacht, dann sind lediglich die folgenden Dependencies im Maven *pom.xml* ergänzt werden:

    <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-rest</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.data</groupId>
		<artifactId>spring-data-rest-hal-browser</artifactId>
	</dependency>
    <dependency>
		<groupId>com.h2database</groupId>
		<artifactId>h2</artifactId>
		<scope>runtime</scope>
	</dependency>
	<dependency>
    	<groupId>org.apache.commons</groupId>
    	<artifactId>commons-lang3</artifactId>
    	<version>3.4</version>
    </dependency>

Die letzte Dependency (commons-lang3) wird benötigt um einfach die üblichen Methoden wie toString(), equals()
usw. zu implementieren.  
Eine Alternative wäre auch [Lombok](https://projectlombok.org/), welches sämtlichen 
Boilerplate Code wie Konstruktoren, toString(), hashcode(), Getter und Setter automatisch beim Compile erzeugt.

2.Persistenzlayer implementieren
--------------------------------

In einem neuen Subpackage *jpa* wird nun zunächst eine JPA Entity *Person* erstellt.

    import org.springframework.data.jpa.domain.AbstractPersistable;
    import javax.persistence.Entity;

    @Entity
    public class Person extends AbstractPersistable<Long> {

        private String firstName;
        private String lastName;

        public Person () {
            super();
        }

        public Person ( String firstName, String lastName ) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName () {
            return firstName;
        }

        public String getLastName () {
            return lastName;
        }

        @Override
        public String toString () {
            return new org.apache.commons.lang3.builder.ToStringBuilder ( this )
                .append ( "firstName", firstName )
                .append ( "lastName", lastName )
                .toString ();
        }
    }

Anschließend können wir mit Hilfe von Spring Data JPA sehr einfach automatisch eine Datenzugriffsklasse 
(*Repository*) für die *Person* Entity im selben Subpackage erstellen.

    import org.springframework.data.jpa.repository.JpaRepository;

    public interface PersonRepository extends JpaRepository<Person,Long> {
    }

Damit haben wir automatisch alle sonst händisch zu implementierenden CRUD-Methoden wie *save()*,
*find()*, *delete* usw. zur Verfügung.

Es ist mit Spring Data JPA auch sehr einfach, weitere benutzerdefinierte Findermethoden über Namenskonventionen
ohne eigenen Code hinzuzufügen:

    import org.springframework.data.jpa.repository.JpaRepository;
    import java.util.List;

    public interface PersonRepository extends JpaRepository<Person,Long> {

        List<Person> findAllByLastName(String lastName);

        List<Person> findAllByFirstNameAndLastName(String firstName, String lastName);
    }
		
Um das *Entity-Control-Boundary* Pattern umzusetzen (Control sparen wir uns hier) fehlt jetzt noch das *Boundary*
in Form des Interfaces *PersonService* im selben Subpackage.
		
    import org.springframework.transaction.annotation.Transactional;
    import java.util.List;

    public interface PersonService {

        @Transactional
        Person save ( Person entity );

        @Transactional(readOnly = true)
        Person findOne ( Long aLong );

        @Transactional(readOnly = true)
        boolean exists ( Long aLong );

        @Transactional(readOnly = true)
        long count ();

        @Transactional
        void delete ( Long aLong );

        @Transactional
        void deleteAll ();

        @Transactional(readOnly = true)
        List<Person> findAll ();

        @Transactional(readOnly = true)
        List<Person> findAllByLastName ( String lastName );

        @Transactional(readOnly = true)
        List<Person> findAllByFirstNameAndLastName ( String firstName, String lastName );
    }
		
Die zugehörige Implementierung delegiert dabei nur noch an das *Repository*.
		
	import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import java.util.List;
        
    @Service("personService")
    public class PersonServiceImpl implements PersonService {
        
        @Autowired
        private PersonRepository personRepository;
        
        @Override
        public Person save ( Person entity ) {
            return personRepository.save ( entity );
        }
        
        @Override
        public Person findOne ( Long aLong ) {
            return personRepository.findOne ( aLong );
        }
        
        @Override
        public boolean exists ( Long aLong ) {
            return personRepository.exists ( aLong );
        }
        
        @Override
        public long count () {
            return personRepository.count ();
        }
        
        @Override
        public void delete ( Long aLong ) {
            personRepository.delete ( aLong );
        }
        
        @Override
        public void deleteAll () {
            personRepository.deleteAll ();
        }
        
        @Override
        public List<Person> findAll () {
            return personRepository.findAll ();
        }
        
        @Override
        public List<Person> findAllByLastName ( String lastName ) {
            return personRepository.findAllByLastName ( lastName );
        }
        
        @Override
        public List<Person> findAllByFirstNameAndLastName ( String firstName, String lastName ) {
            return personRepository.findAllByFirstNameAndLastName ( firstName, lastName );
        }
    }

Um nun beim Starten der Anwendung zu Testzwecken bereits einige Personen zu speichern kann die Starterklasse
um einen *CommandLineRunner* erweitert werden.

    import info.novatec.spring.boot.jpa.Person;
    import info.novatec.spring.boot.jpa.PersonService;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.boot.CommandLineRunner;
    import java.util.List;
    ...

    public class SpringBootWorkshop3Application implements CommandLineRunner {

        private static final Logger LOGGER = LoggerFactory.getLogger ( SpringBootWorkshop3Application.class );

        @Autowired
        private PersonService personService;
        
        ...
    
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
		
3.Starten des Projekts
----------------------

Mit der Starter-Klasse kann nun die Anwendung wie in Lab 1 und Lab 2 gestartet werden. 
Und kann dann über *http://localhost:8080* aufgerufen werden.

3.1.Abfragen gespeicherter Daten mit der H2 Konsole
---------------------------------------------------

Seit Spring Boot Version 1.3 wird bei Verwendung der H2 Datenbank automatisch gleich die H2 Konsole mitkonfiguriert.
Diese kann über die URL [http://localhost:8080/h2-console](http://localhost:8080/h2-console) aufgerufen werden.

Damit dies mit unserer Security-Konfiguration aus Lab 2 funktioniert müssen wir in der Klasse *WebSecurityConfig*
zwei Zeilen ändern bzw. hinzufügen. 

Die Zeile 

    headers().frameOptions().deny()
     
muss geändert werden in 

    headers().frameOptions().disable()
       
Hinter die existierende Zeile mit *.antMatchers ("/login")* muss die folgende Zeile hinzugefügt werden 

    .antMatchers ( "/h2-console/**" ).permitAll ()

Im Endergebnis sollte die Methode dann so aussehen:

    @Override
    protected void configure ( HttpSecurity http ) throws Exception {
        http
            .csrf ().disable ()
            .headers ().frameOptions ().disable ()
            .and ()
            .authorizeRequests ()
            .antMatchers ( "/login" ).permitAll ()
            .antMatchers ( "/h2-console/**" ).permitAll ()
            .anyRequest ().fullyAuthenticated ()
            .and ()
            .formLogin ().loginPage ( "/login" ).loginProcessingUrl ( "/j_spring_security_check" ).defaultSuccessUrl ( "/" );
    }

Nachdem die H2 Konsole geöffnet wurde sollte darauf geachtet werden dass folgende Einträge ausgewählt bzw. vorbelegt sind:

* Der ausgewählte DB-Typ sollte dieser sein: Generic H2 (Embedded)
* Die Driver-Class sollte so aussehen: org.h2.Driver
* Die JDBC-Url sollte so aussehen: jdbc:h2:mem:testdb

Dann kann mittels *Connect* die Verbindung zur H2 Datenbank hergestellt werden. Dort sollte die *Person* Tabelle
vorhanden sein. Hier kann man nun den Inhalt der Tabelle abfragen. 

4.DB-Migration mit Flyway
-------------------------

Um statt der Hibernate-Autogenerierung eine automatisierte DB-Migration mit Hilfe von Flyway aufzusetzen sind nur 
ein paar einfache Schritte erforderlich.

Zunächst muss Flyway als Dependency hinzugefügt werden:

    <dependency>
		<groupId>org.flywaydb</groupId>
		<artifactId>flyway-core</artifactId>
		<version>3.2.1</version>
	</dependency>

Anschließend muss unter *src/main/resources* einn neues Unterverzeichnis *db/migration* erstell werden.
Dort hinein erstellen wir unser SQL-Artefakt *V1_0_0__Test.sql* für Flyway um unsere Person Tabelle nun als DB-Migration zu erzeugen.

    CREATE TABLE PERSON (
        ID BIGINT AUTO_INCREMENT PRIMARY KEY,
        LAST_NAME VARCHAR(30),
        FIRST_NAME VARCHAR(30)
    );

Anschließend müssen wir nur noch die Autogenerierung von Hibernate deaktivieren (in eine Validierung). Dies 
kann wieder über eine vordefinierte Property in den *application.properties* umgesetzt werden.

    spring.jpa.hibernate.ddl-auto=validate

Wird die Anwendung nun neu gestartet sollte Flyway die Datenbank entsprechend migrieren.
Außerdem steht nun auch ein weiterer Actuator-Endpoint für [Flyway](http://localhost:8080/flyway) zur Verfügung.
 
 