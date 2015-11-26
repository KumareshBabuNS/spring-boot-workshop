Spring Boot Lab 3
=================

Im dritten Lab wird die nun gesicherte Spring Boot Web Anwendung aus Lab 2 um Persistenz auf Basis von JPA 2.1 erweitert.
Darüber hinaus werden die Entitäten auch über REST verfügbar gemacht.
 
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
		
2.Starten des Projekts
----------------------

