package info.novatec.spring.boot.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Repository for data access to Person entities.
 */
@RepositoryRestResource
public interface PersonRepository extends JpaRepository<Person,Long> {

    List<Person> findAllByLastName(String lastName);

    List<Person> findAllByFirstNameAndLastName(String firstName, String lastName);

}
