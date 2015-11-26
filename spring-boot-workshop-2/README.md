Spring Boot Lab 2
=================

Im zweiten Lab wird die rudimentäre Spring Boot Web Anwendung aus Lab 1 um Security erweitert. 
Hier soll gezeigt werden, wie einfach mittels Spring Boot in wenigen Minuten eine 
bereits grundsätzlich sichere Anwendung erstellt werden kann.
 
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

1.1.Erweiterung des Projekts aus Lab 1
--------------------------------------

Wird auf Basis des Projektes aus Lab 1 weiter gemacht, dann ist lediglich die folgende Dependency im Maven *pom.xml* ergänzt werden:

    <dependency>
	   <groupId>org.springframework.boot</groupId>
	   <artifactId>spring-boot-starter-security</artifactId>
	</dependency>


2.Starten des Projekts
----------------------

Nach Erstellung/Erweiterung der Spring Boot Anwendung ist bereits eine grundsätzliche Sicherheit auf Basis von Basic Authentication
automatisch durch Spring Boot konfiguriert.
  
Mit der Starter-Klasse kann nun die Anwendung wie in Lab 1 gestartet werden. 
Und kann dann über *http://localhost:8080* aufgerufen werden.
  
Zunächst wird nun direkt ein Anmeldedialog als Popup angezeigt. Standardmäßig wird als Benutzername *user* und als Passwort
ein auf der Konsole ausgegebenes zufällig generiertes Passwort verwendet.  

Dieses ist auf der Konsole wie folgt zu sehen:

    Using default security password: fe9caa23-1ea0-4508-8aa1-7519016c339f

Um nun nicht jedes Mal das immer anders lautende Passwort ermitteln zu müssen kann dies durch 
vordefinierte Properties fest vorgegeben werden:

    security.user.name=myuser
    security.user.password=mypassword
    security.user.role=USER,ADMIN

Wie hier zu sehen ist kann über das Passwort hinaus auch der Benutzername sowie die zugewisenen Rollen geändert werden.

3.Weitergehende Konfiguration
-----------------------------

Die vereinfachte Konfiguration über die vordefinierten Properties reichen im Normalfall für ernsthafte Anwendungen
nicht aus. Spring Security bietet hierfür einen komfortablen Weg über die Erweiterung der Klasse *WebSecurityConfigurerAdapter*:

    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    
    @EnableWebSecurity
    @Configuration
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Override
        protected void configure ( AuthenticationManagerBuilder auth ) throws Exception {
            auth
                .inMemoryAuthentication ()
                .withUser ( "guest" ).password ( "guest" ).roles ( "GUEST" )
                .and ()
                .withUser ( "user" ).password ( "user" ).roles ( "USER" )    
                .and ()
                .withUser ( "admin" ).password ( "admin" ).roles ( "ADMIN", "USER", "GUEST" );    
        }
    
        @Override
        protected void configure ( HttpSecurity http ) throws Exception {
            http
                .csrf ().disable ()
                .headers ().frameOptions ().deny ()
                .and ()
                .authorizeRequests ()
                .antMatchers ( "/login" ).permitAll ()
                .anyRequest ().fullyAuthenticated ()
                .and ()
                .formLogin ().loginPage ( "/login" ).loginProcessingUrl ( "/j_spring_security_check" ).defaultSuccessUrl ( "/" );;    
        }
    }

Wie zu sehen ist kann sowohl der Authentication-Provider (in diesem Fall eine In-Memory-Konfiguration)
als auch die zu schützenden Ressourcen sowie die Art der Anmedlung konfiguriert werden.    

Eine Loginseite in HTML ist nachstehend dargestellt:

    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
    </head>
    <body>
        <form name="f" method="post" action="/j_spring_security_check">
            <p>
            <label for="username">User</label>
            <input type="text" name="username" id="username"/>
            </p>
            <p>
            <label for="password">Passwort</label>
            <input type="password" name="password" id="password"/>
            </p>
            <p>
            <input type="submit" name="login" id="login" value="login">
            </p>
        </form>
    </body>
    </html>

Um diese über den Pfad "/login" verfpgbar zu machen muss nachfolgende Spring MVC Konfiguration noch hinzugefügt werden:

    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

    @Configuration
    public class MvcConfig extends WebMvcConfigurerAdapter {

        @Override
        public void addViewControllers ( ViewControllerRegistry registry ) {
            registry.addViewController ( "/login" ).setViewName ( "/login.html" );
        }
    }

