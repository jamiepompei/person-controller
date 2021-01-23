package io.zipcoder.crudapp.service;

import io.zipcoder.crudapp.entity.Person;
import io.zipcoder.crudapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService{

    @Autowired
    PersonRepository personRepository;

    @Override
    public List<Person> getAll() {
     Iterable<Person> people = personRepository.findAll();
     List<Person> personList = new ArrayList<>();
        people.forEach(personList::add);
        return personList;
    }

    @Override
    public Person findById(Long id) {
        return personRepository.findOne(id);
    }

    @Override
    public Person findByName(String firstName, String lastName) {
        Iterable<Person> people = personRepository.findAll();
        List<Person> personList = new ArrayList<>();
        people.forEach(personList::add);
        for (Person person:personList) {
            if(person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)){
                return person;
        }
    }
    return null;
    }

    @Override
    public void create(Person person) {
        personRepository.save(person);
    }

    @Override
    public void update(Person person) {
        Person personInDatabse = personRepository.findOne(person.getId());
        personInDatabse.setFirstName(person.getFirstName());
        personInDatabse.setLastName(person.getLastName());
        personInDatabse = personRepository.save(personInDatabse);
    }

    @Override
    public void delete(Long id) {
        Person personToDelete = personRepository.findOne(id);
        personRepository.delete(personToDelete);
    }

    @Override
    public boolean exists(Person person) {
        boolean doesExist = findByName(person.getFirstName(), person.getLastName()) != null;
        return doesExist;
    }
}
