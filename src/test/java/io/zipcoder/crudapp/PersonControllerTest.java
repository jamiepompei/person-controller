package io.zipcoder.crudapp;

import io.zipcoder.crudapp.controller.PersonController;
import io.zipcoder.crudapp.entity.Person;
import io.zipcoder.crudapp.filter.CORSFilter;
import io.zipcoder.crudapp.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PersonController.class)
@ActiveProfiles("test")
public class PersonControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PersonService personService;


    @InjectMocks
    private PersonController personController;

    private List<Person> personList;
    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(personController)
                .addFilters(new CORSFilter())
                .build();
    }

    //================================ Test Get All People ==================================

    @Test
    public void shouldFindAllPeopleTest() throws Exception {
        this.personList = new ArrayList<>();
        this.personList.add(new Person(1L, "Kyle", "Grace"));
        this.personList.add(new Person(2L,"Jon", "Snow"));
        this.personList.add(new Person(3L, "Jessie", "Pinkman"));

        when(personService.getAll()).thenReturn(personList);

        mockMvc.perform(get("/person"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.size()", is(personList.size())))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].firstName", is("Kyle")))
            .andExpect(jsonPath("$[0].lastName", is("Grace")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].firstName", is("Jon")))
            .andExpect(jsonPath("$[1].lastName", is("Snow")))
            .andExpect(jsonPath("$[2].id", is(3)))
            .andExpect(jsonPath("$[2].firstName", is("Jessie")))
            .andExpect(jsonPath("$[2].lastName", is("Pinkman")));

            verify(personService, times(1)).getAll();
            verifyNoMoreInteractions(personService);
    }

    // ==================================== Test Get Person By Id ====================================

    @Test
    public void testGetPersonById() throws Exception {
        Person person = new Person(10L, "Kyle", "Grace");

        when(personService.findById(10L)).thenReturn(person);

        mockMvc.perform(get("/person/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.firstName", is("Kyle")))
                .andExpect(jsonPath("$.lastName", is("Grace")));

        verify(personService, times(1)).findById(10L);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void testGetPersonById404NotFound() throws Exception {
        when(personService.findById(10L)).thenReturn(null);

        mockMvc.perform(get("/person/{id}", 10))
                .andExpect(status().isNotFound());

        verify(personService, times(1)).findById(10L);
        verifyNoMoreInteractions(personService);
    }

    //====================================== Create New Person ==============================
    @Test
    public void testCreatePersonSuccess() throws Exception {
        Person person = new Person(1L, "Jon", "Snow");

        when(personService.exists(person)).thenReturn(false);
        doNothing().when(personService).create(person);

        mockMvc.perform(
                post("/person")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(person)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/person/1")));

        verify(personService, times(1)).exists(person);
        verify(personService, times(1)).create(person);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void testCreateUserFailNotFound() throws Exception {
        Person person = new Person("Person", "Exists");

        when(personService.exists(person)).thenReturn(true);

        mockMvc.perform(
                post("/person")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(person)))
                .andExpect(status().isConflict());
        verify(personService, times(1)).exists(person);
        verifyNoMoreInteractions(personService);
    }

    //======================================= Update Existing Person ============================

    @Test
    public void testUpdateUserSuccess() throws Exception {
        Person person = new Person(1L, "Jon", "Snow");

        when(personService.findById(person.getId())).thenReturn(person);
        doNothing().when(personService).update(person);

        mockMvc.perform(
                put("/person/{id}", person.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(person)))
                .andExpect(status().isOk());

        verify(personService, times(1)).findById(person.getId());
        verify(personService, times(1)).update(person);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void testUpdateUserFailNotFound() throws Exception {
        Person person = new Person(1L, "personNot", "Found");

        when(personService.findById(person.getId())).thenReturn(null);

        mockMvc.perform(
                put("/person/{id}", person.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(person)))
                .andExpect(status().isNotFound());

        verify(personService, times(1)).findById(person.getId());
        verifyNoMoreInteractions(personService);
    }

    //========================================= Delete User =======================================

    @Test
    public void testDeleteUserSuccess() throws Exception {
        Person person = new Person(1L, "Jerry Lee", "Pompei");

        when(personService.findById(person.getId())).thenReturn(person);
        doNothing().when(personService).delete(person.getId());

        mockMvc.perform(
                delete("/person/{id}", person.getId()))
                .andExpect(status().isOk());

        verify(personService, times(1)).findById(person.getId());
        verify(personService, times(1)).delete(person.getId());
        verifyNoMoreInteractions(personService);

    }

    @Test
    public void testDeleteUserFail() throws Exception {
        Person person = new Person(1L, "Person", "NotFound");

        when(personService.findById(person.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/person/{id}", person.getId()))
                .andExpect(status().isNotFound());


        verify(personService, times(1)).findById(person.getId());
        verifyNoMoreInteractions(personService);
    }

    //=================================== Test CORS header ========================================
    @Test
    public void testCORSheaders() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(header().string("Access-Control-Allow_Origin", "*"))
                .andExpect(header().string("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE"))
                .andExpect(header().string("Access-Control-Allow-Headers", "*"))
                .andExpect(header().string("Access-Control-Max-Age", "3600"));

    }


    public static String asJsonString(final Object obj){
        try{
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
