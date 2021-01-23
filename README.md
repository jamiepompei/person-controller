# Person Controller CRUD Application

This repository provides a starter project for a CRUD application using Spring Boot, RestControllers, and H2 for an in memory database with tests written with JUnit4 and Mockito.



<strong>Endpoints:</strong>

- `POST /people` - create a new person
  - Response: `201 Created`
- `GET /people` - get the list of all people
  - Response: `200 OK` 
- `GET /people/{id}` - Get the person with id number `{id}`
  - Response: `200 OK` if found, else `404 Not Found`
- `PUT /people/{id}` - Update the person with id number `{id}`
  - Response: `200 OK` if updated, `201 Created` if a new entity was created
- `DELETE /people/{id}` - delete the person with id number `{id}`
  - Response: `204 No Content`


