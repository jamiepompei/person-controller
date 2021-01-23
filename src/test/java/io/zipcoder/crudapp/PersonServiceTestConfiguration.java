package io.zipcoder.crudapp;

import io.zipcoder.crudapp.service.PersonService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class PersonServiceTestConfiguration {
    @Bean
    @Primary
    public PersonService personService(){
        return Mockito.mock(PersonService.class);
    }
}
