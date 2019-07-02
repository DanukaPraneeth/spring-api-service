package com.api.service.dao;

import com.api.service.model.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {

    List<PersonEntity> findAllByFirstNameAndLastName(String firstName, String lastName);

    @Query("select c from PersonEntity c where c.firstName = ?1 or c.lastName = ?1")
     List<PersonEntity> findAllByName(String name1);


}