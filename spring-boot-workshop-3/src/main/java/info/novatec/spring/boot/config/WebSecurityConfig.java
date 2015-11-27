package info.novatec.spring.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity(debug = true)
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
            .headers ().frameOptions ().disable ()
            .and ()
            .authorizeRequests ()
            .antMatchers ( "/login" ).permitAll ()
            .antMatchers ( "/h2-console/**" ).permitAll ()
            .anyRequest ().fullyAuthenticated ()
            .and ()
            .formLogin ().loginPage ( "/login" ).loginProcessingUrl ( "/j_spring_security_check" ).defaultSuccessUrl ( "/" );
    }

}
