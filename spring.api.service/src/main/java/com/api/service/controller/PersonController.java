package com.api.service.controller;

import com.api.service.bean.PersonBean;
import com.api.service.model.Person;
import com.api.service.service.PersonService;
import com.api.service.util.BadRequestException;
import com.api.service.util.CustomErrorResponse;
import com.api.service.util.PersonNotFoundException;
import com.api.service.util.PersonServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class PersonController {


    public static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;


    // -------------------Retrieve All Users---------------------------------------------
    @GetMapping("/v1/search/persons/all")
    public ResponseEntity getAllPersons() {

        PersonBean ps = personService.getAllPerson();

        if (ps == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ps, HttpStatus.OK);

    }


    //-------------------Retrieve Single User--------------------------------------------------------
    @GetMapping("/v1/search/persons/{name}")
    public ResponseEntity<?> getPersonByName(@PathVariable("name") String name) {

        PersonBean user = personService.getPersonByName(name, null);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }



    @GetMapping("/v1/search/persons")
    public ResponseEntity<?> getPersonByFullName(@RequestParam(value = "first_name", required = true) String firstName, @RequestParam(value = "last_name", required = true) String lastName) {

        PersonBean user = personService.getPersonByName(firstName, lastName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }



    // -------------------Create a User-------------------------------------------
    @PostMapping("/v1/add/persons")
    public ResponseEntity addPerson(@RequestBody PersonBean personBean) {
        personService.addPerson(personBean);
        return new ResponseEntity<String>("New Persons added to the system successfully....", HttpStatus.CREATED);
    }




    // ------------------- Update a User ------------------------------------------------

    @PutMapping("/v1/change/persons")
    public ResponseEntity updatePerson(@RequestParam(value = "first_name", required = true) String firstName, @RequestParam(value = "last_name", required = true) String lastName, @RequestBody PersonBean personBean) {

        if (personBean.getPerson().length > 1) {
            CustomErrorResponse error = new CustomErrorResponse("Bad Request", "Too many person entity details for update request", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }

        Person latestPersonInfo = personBean.getPerson()[0];
        personService.updatePerson(firstName, lastName, latestPersonInfo);

        return new ResponseEntity<>("Person details updated Successfully....", HttpStatus.OK);
    }



    // ------------------- Delete a User-----------------------------------------

    @DeleteMapping("/v1/delete/persons")
    public ResponseEntity<?> deleteUser(@RequestParam(value = "first_name", required = true) String firstName, @RequestParam(value = "last_name", required = true) String lastName) {

        personService.deletePerson(firstName, lastName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    // ------------------- Delete All Users-----------------------------

    @DeleteMapping("/v1/delete/persons/all")
    public ResponseEntity<?> deleteAllUsers() {

        personService.deleteAllUsers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    @ExceptionHandler({ PersonNotFoundException.class })
    public ResponseEntity<CustomErrorResponse> handleNotFoundException(Exception ex) {

        CustomErrorResponse error = new CustomErrorResponse("Record Not Found", ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({ PersonServiceException.class })
    public ResponseEntity<CustomErrorResponse> handleServiceException(Exception ex) {

        CustomErrorResponse error = new CustomErrorResponse("Internal Server Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<CustomErrorResponse> badRequestException(Exception ex) {

        CustomErrorResponse error = new CustomErrorResponse("Bad Request. Validation Failed", ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

}

