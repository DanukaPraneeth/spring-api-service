package com.api.service.dao;

import com.api.service.model.HobbyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface HobbyRepository extends JpaRepository<HobbyEntity, Integer> {

    @Transactional
    @Modifying
    @Query("delete from HobbyEntity where PERSON_ID = ?1")
    void deleteByHobby (Integer id);

    void findById(int id);

}
