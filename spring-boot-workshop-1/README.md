Spring Boot Lab 1
=================

Im ersten Lab wird eine rudimentäre Spring Boot Web Anwendung implementiert, 
die lediglich eine Begrüßung im Browser anzeigt.
 
Projekterstellung
-----------------
 
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

Starten des Projekts
--------------------

Nach Erstellung der Spring Boot Anwendung liegen im Projekt nun eine *pom.xml* Datei, eine Starter-Klasse unter *src/main/java* sowie ein 
Spring-Integrationstest unter *src/test/java*.  
Mit der Starter-Klasse lässt sich die Anwendung bereits jetzt gleich starten. Standardmäßig wird die Webanwendung mit Http auf Port 8080 konfiguriert.  
Somit kann über *http://localhost:8080* die Anwendung aufgerufen werden.  

Controller mit RESTful Service
------------------------------

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

Parametrisierter RESTful Service
--------------------------------

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
