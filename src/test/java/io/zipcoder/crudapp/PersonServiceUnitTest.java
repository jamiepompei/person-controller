package io.zipcoder.crudapp;

import io.zipcoder.crudapp.entity.Person;
import io.zipcoder.crudapp.repository.PersonRepository;
import io.zipcoder.crudapp.service.PersonService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CRUDApplication.class)
public class PersonServiceUnitTest {

    @Mock
    private PersonRepository personRepository;

    @Autowired
    PersonService personService;

    @Test
    public void whenIdIsProvided_thenRetrievedPersonIsCorrect(){
        Long mockId = 1L;
        String expectedFirstName = "Kyle";
        String expectedLastName = "Grace";
        Long expectedId = 1L;
        Person expectedPerson = new Person(expectedId, expectedFirstName, expectedLastName);

        Mockito.when(personRepository.findOne(mockId)).thenReturn(new Person(expectedId, expectedFirstName, expectedLastName));

        Person testPerson = personService.findById(mockId);

        assertEquals(expectedPerson, testPerson);
    }

    @Test
    public void whenListIsProvided_thenRetrieveAllPeople(){
        Person kyle = new Person(1L, "Kyle", "Grace");
        Person jon = new Person(2L, "Jon", "Snow");
        Person jessie = new Person(3l, "Jessie", "Pinkman");
        Person[] people = {kyle, jon, jessie};
        List<Person> personListExpected = Arrays.asList(people);

        Mockito.when(personService.getAll()).thenReturn(personListExpected);

        List<Person> personListTest = personService.getAll();

        assertEquals(personListExpected, personListTest);

    }

    @Test
    public void verifyCreateAPersonCall(){
        Person person = mock(Person.class);
        personService.create(person);
        verify(personService, times(1)).create(person);
    }

    @Test
    public void verifyDeleteAPersonCall(){
        Person person = mock(Person.class);
        personService.delete(person.getId());

        verify(personService, times(1)).delete(person.getId());
    }

    @Test
    public void verifyUpdatePerson(){
        Person person = mock(Person.class);
        personService.update(person);

        verify(personService, times(1)).update(person);
    }
}
