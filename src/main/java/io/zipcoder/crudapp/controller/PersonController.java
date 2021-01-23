package io.zipcoder.crudapp.controller;


import io.zipcoder.crudapp.entity.Person;
import io.zipcoder.crudapp.repository.PersonRepository;
import io.zipcoder.crudapp.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final Logger LOG = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

//====================== Create Person ====================================
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createPerson(@RequestBody Person person, UriComponentsBuilder ucBuilder){
        LOG.info("creating a person: {}", person);

        if(personService.exists(person)){
            LOG.info("a person with name " + person.getFirstName() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

    personService.create(person);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/person/{id}").buildAndExpand(person.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);

    }

    //=================== Get a Person by ID =============================
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Person> getPerson(@PathVariable("id") Long id){
        LOG.info("getting person with id: {}", id);
        Person person = personService.findById(id);

        if(person == null){
            LOG.info("person with id {} not found", id);
            return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }

    //==================== Get All People =============================
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Person>> getPersonList(){
        LOG.info("getting all people");
        List<Person> personList = personService.getAll();
        if(personList == null || personList.isEmpty()){
            LOG.info("no people found");
            return new ResponseEntity<List<Person>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Person>>(personList, HttpStatus.OK);
    }

    //===================== Update Existing Person by ID ========================
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person person){
        LOG.info("updating person: {person}", person);
        Person currentPerson = personService.findById(id);

        if(currentPerson == null){
            return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
        }

        currentPerson.setId(person.getId());
        currentPerson.setFirstName(person.getFirstName());
        currentPerson.setLastName(person.getLastName());

        personService.update(currentPerson);

        return new ResponseEntity<Person>(currentPerson, HttpStatus.OK);
    }

    //====================== Delete User ==================================
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> DeletePerson(@PathVariable ("id") Long id){
        LOG.info("deleting person with id: {}", id);
        Person person = personService.findById(id);

        if (person == null){
            LOG.info("Unable to delete. Person with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        personService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
