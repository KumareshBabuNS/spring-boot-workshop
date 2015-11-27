package info.novatec.spring.boot.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementation for {@link PersonService}.
 */
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
