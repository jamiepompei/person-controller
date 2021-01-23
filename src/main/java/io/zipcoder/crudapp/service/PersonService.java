package io.zipcoder.crudapp.service;

import io.zipcoder.crudapp.entity.Person;

import java.util.List;


public interface PersonService {

    List<Person> getAll();

    Person findById(Long id);

    Person findByName(String firstName, String lastName);

    void create(Person person);

    void update(Person person);

    void delete(Long id);

    boolean exists(Person person);


}
