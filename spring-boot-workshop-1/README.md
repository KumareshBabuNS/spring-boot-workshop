Spring Boot Lab 1
=================

Im ersten Lab wird eine rudimentäre Spring Boot Web Anwendung implementiert, 
die lediglich eine Begrüßung im Browser anzeigt.
 
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
* DevTools
* Actuator
* HATEOAS

2.Starten des Projekts
----------------------

Nach Erstellung der Spring Boot Anwendung liegen im Projekt nun eine *pom.xml* Datei, eine Starter-Klasse unter *src/main/java* sowie ein 
Spring-Integrationstest unter *src/test/java*.  
Mit der Starter-Klasse lässt sich die Anwendung bereits jetzt gleich starten. Standardmäßig wird die Webanwendung mit Http auf Port 8080 konfiguriert.  
Somit kann über *http://localhost:8080* die Anwendung aufgerufen werden.  

3.Controller mit RESTful Service
--------------------------------

Allerdings wird im Browser aktuell noch eine *Whitelabel Error Page* angezeigt.
Wir haben ja bisher noch keinen Inhalt für die Webanwendung festgelegt.

Dies wollen wir jetzt nach holen. In einem neuen Subpackage *controller* wollen wir einen neue Klasse *HelloController* 
mit nachstehendem Code erstellen:

    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RestController;
    
    @RestController
    public class HelloController {

      @RequestMapping(path = "/", method = RequestMethod.GET)
      public String message() {
        return "hello world";
      }
    }
    
Nach erneutem Compile des Projekts muss dank der bei der Erstellung ausgewählten *DevTools* das Projekt nicht manuell neu gestartet werden sondern
es wird ein automatischer Reload der von uns erstellten Klassen durchgeführt und anschliessend ist die Anwendung mit dem aktuellen Stand bereit.  
Nach Refresh im Browser sollte nun ein *hello world* im Browser erscheinen.

4.Parametrisierter RESTful Service
----------------------------------

Jetzt wollen wir in der *HelloController* Klasse die Begrüßung mit einem konfigurierbaren Parameter erweitern:

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class HelloController {

      @Value ("${hello.greeting}")
      private String greeting;

      @RequestMapping(path = "/", method = RequestMethod.GET)
      public String message() {
        return "hello " + greeting;
      }
    }

Konfiguriert wird eine Spring Boot Anwendung generell mittels einer vordefinierten Property-Datei mit dem Namen *application.properties*, die
bereits bei Projekterstellung als leere Datei unter *src/main/resources* abgelegt wurde.

Hier können wir nun die neue Property *hello.greeting* einfügen:

    hello.greeting=new world

Nun sollte nach erneutem Rebuild und Restart im Browser der Text *hello new world* angezeigt werden.     

5.Code-Completion für eigene Properties
---------------------------------------

Analog zur Code-Completion und Dokumentation der vordefinierten Spring Boot Properties kann eine solche 
auch für eigene Properties erzeugt werden. In IntelliJ kann dies durch einen Quickhelper *Define configuration key* direkt in der 
*application.properties* Datei ausgelöst werden.
Dadurch wird eine Datei namens *additional-spring-configuration-metadata.json* im Verzeichnis
*src/main/resources/META-INF* erzeugt.  
Nachstehend ist ein Beispiel für unsere Property *hello.greeting* dargestellt:

    {
      "properties": [
        {
        "name": "hello.greeting",
        "type": "java.lang.String",
        "description": "Greeting for first spring boot app."
        }
    ],
    "hints": [
    {
      "name": "hello.greeting",
      "values": [
        {
          "description": "informal.",
          "value": "my bro"
        },
        {
          "description": "formal.",
          "value": "my dear"
        }
      ]
    }
    ]
    }
    
Um diese Datei beim Compile automatisch zu verarbeiten muss eine zusätzliche Dependency für Maven
hinzugefügt werden:
    
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-configuration-processor</artifactId>
      <optional>true</optional>
    </dependency>

6.Installation von Spring Boot Anwendungen
------------------------------------------

Mittels des Spring Boot Maven Plugins werden Spring Boot Anwendungen in ausführbare Jar- bzw. War-Dateien
verpackt.
Diese können dann mittels 

    java -jar myApp.jar
     
oder

    java -jar myApp.war
    
gestartet werden.
    
Seit Spring Boot Version 1.3 können diese auch direkt als Unix/Linux-Services konfiguriert und 
gestartet werden.
Dazu muss im Maven Plugin die folgende Konfiguration ergänzt werden:
    
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
            <executable>true</executable>
        </configuration>
    </plugin>
    
Nun kann unter Unix/Linux die Anwendung auch direkt als *Executable* ausgeführt werden.  
Darüber hinaus kann diese auch als init.d or systemd Dienst konfiguriert werden.  
Um die Anwendung als init.d Dienst zu installieren reicht folgende Zeile aus:
    
    sudo ln -s ./myApp.jar /etc/init.d/myApp    
    
Nun kann wie gewohnt die Anwendung z.B. über 

    /etc/init.d/myApp start

gestartet werden.  
Entsprechend können auch die übrigen Kommandos *stop*, *status* und *restart* verwendet werden.        
        