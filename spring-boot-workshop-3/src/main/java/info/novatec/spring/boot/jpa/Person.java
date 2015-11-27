package info.novatec.spring.boot.jpa;

import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;

/**
 * Person entity.
 */
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
