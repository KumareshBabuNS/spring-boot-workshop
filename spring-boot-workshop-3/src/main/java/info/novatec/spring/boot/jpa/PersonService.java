package info.novatec.spring.boot.jpa;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Boundary to manage Person entities.
 */
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
