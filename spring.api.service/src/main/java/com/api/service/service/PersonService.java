package com.api.service.service;

import com.api.service.bean.PersonBean;
import com.api.service.controller.PersonController;
import com.api.service.dao.HobbyRepository;
import com.api.service.dao.PersonRepository;
import com.api.service.model.HobbyEntity;
import com.api.service.model.Person;
import com.api.service.model.PersonEntity;
import com.api.service.util.BadRequestException;
import com.api.service.util.PersonNotFoundException;
import com.api.service.util.PersonServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private HobbyRepository hobbyRepository;

    public static final Logger logger = LoggerFactory.getLogger(PersonService.class);


    public PersonBean getPersonByName(String firstName, String lastName) throws PersonNotFoundException{

        List<PersonEntity> entityList;
        PersonBean personBean = new PersonBean();

        try {
            if (lastName == null) {
                entityList = personRepo.findAllByName(firstName);
            } else
                entityList = personRepo.findAllByFirstNameAndLastName(firstName, lastName);

            if (entityList.size() == 0){
                logger.error("Persons details not found in the system for " + firstName);
                throw new PersonNotFoundException("Persons details not found in the system for " + firstName);
            }

            Person[] personList = new Person[entityList.size()];
            int i = 0;
            for (PersonEntity personEntity : entityList) {
                Person ps = new Person();
                ps.setFirst_name(personEntity.getFirstName());
                ps.setLast_name(personEntity.getLastName());
                ps.setAge(personEntity.getAge());
                ps.setFavourite_colour(personEntity.getFavouriteColor());
                String[] hobbies = new String[personEntity.getHobby().size()];
                int x = 0;
                for (HobbyEntity hobby : personEntity.getHobby()) {
                    hobbies[x] = hobby.getHobby();
                    x++;
                }
                ps.setHobby(hobbies);
                personList[i] = ps;
                i++;
            }
            personBean.setPerson(personList);
            logger.info("Persons details retrieved successfully for person " + firstName);

        }
        catch (PersonNotFoundException ex){
            throw ex;
        }
        catch (Exception ex){
            throw new PersonServiceException("Error while retrieving Person details from the system");
        }

        return personBean;
    }

    public PersonBean getAllPerson(){

        try{
            List<PersonEntity> entityList = personRepo.findAll();
            Person[] personList = new Person[entityList.size()];
            int i = 0;
            for (PersonEntity personEntity : entityList) {
                Person ps = new Person();
                ps.setFirst_name(personEntity.getFirstName());
                ps.setLast_name(personEntity.getLastName());
                ps.setAge(personEntity.getAge());
                ps.setFavourite_colour(personEntity.getFavouriteColor());
                String[] hobbies = new String[personEntity.getHobby().size()];
                int x = 0;
                for (HobbyEntity hobby : personEntity.getHobby()) {
                    hobbies[x] = hobby.getHobby();
                    x++;
                }
                ps.setHobby(hobbies);
                personList[i] = ps;
                i++;
            }
            PersonBean personBean = new PersonBean();
            personBean.setPerson(personList);

            logger.info("All Persons details retrieved successfully");
            return personBean;

        }catch (Exception ex){
            logger.error("Error while retrieving All Persons details from the system");
            throw new PersonServiceException("Error while retrieving All Persons details from the system");
        }

    }


    public boolean addPerson(PersonBean personBean){
        try{
            for(Person person :personBean.getPerson()) {
                PersonEntity entity=new PersonEntity();

                if( isPersonExist(person)){
                    String error = "Request failed for users after " + person.getFirst_name() + " " + person.getLast_name() + ". User Already in the system";
                    logger.error(error);
                    throw new BadRequestException(error);
                }

                entity.setFirstName(person.getFirst_name());
                entity.setLastName(person.getLast_name());
                entity.setAge(person.getAge());
                entity.setFavouriteColor(person.getFavourite_colour());

                List<HobbyEntity> hobbies=new ArrayList<HobbyEntity>();

                for (String hobby : person.getHobby()) {
                    HobbyEntity hobbyBean=new HobbyEntity();
                    hobbyBean.setPerson(entity);
                    hobbyBean.setHobby(hobby);
                    hobbies.add(hobbyBean);
                }

                entity.setHobby(hobbies);
                personRepo.save(entity);
                logger.info("New persons added to the system successfully");
            }
        }catch (BadRequestException ex){
            throw ex;

        }catch (Exception ex){
            logger.error("Error while adding new Person details to the system");
            throw new PersonServiceException("Error while adding new Person details to the system");
        }

        return true;
    }


    public void updatePerson(String firstName, String lastName, Person latestPersonInfo){

        try{
            List<PersonEntity> existingPersonList = personRepo.findAllByFirstNameAndLastName(firstName, lastName);

            if(existingPersonList.size() == 0){
                String error = "No Person details found in the system for " + firstName + " " + lastName;
                logger.error(error);
                throw new BadRequestException(error);
            }

            PersonEntity existingPerson = existingPersonList.get(0);

            existingPerson.setId(existingPerson.getId());
            existingPerson.setFirstName(latestPersonInfo.getFirst_name());
            existingPerson.setLastName(latestPersonInfo.getLast_name());
            existingPerson.setAge(latestPersonInfo.getAge());
            existingPerson.setFavouriteColor(latestPersonInfo.getFavourite_colour());

            List<HobbyEntity> hobbies=new ArrayList<HobbyEntity>();

            for (String hobby : latestPersonInfo.getHobby()) {
                HobbyEntity hobbyBean=new HobbyEntity();
                //hobbyBean.setId(x);
                hobbyBean.setPerson(existingPerson);
                hobbyBean.setHobby(hobby);
                hobbies.add(hobbyBean);
            }

            hobbyRepository.deleteByHobby(existingPerson.getId());
            hobbyRepository.flush();
            existingPerson.setHobby(hobbies);
            personRepo.save(existingPerson);
            logger.info("New details updated succeefully for person " + firstName + " " + lastName);
        }
        catch (BadRequestException ex){
            throw ex;
        }
        catch (Exception ex){
            logger.error("Error while updating Person details to the system");
            throw new PersonServiceException("Error while updating Person details to the system");
        }

    }


    public void deletePerson(String firstName, String lastName){

        try{
            List<PersonEntity> existingPersonList = personRepo.findAllByFirstNameAndLastName(firstName, lastName);
            if(existingPersonList.size() == 0){
                String error = "No Person details found in the system for " + firstName + " " + lastName;
                logger.error(error);
                throw new BadRequestException(error);
            }

            PersonEntity existingPerson = existingPersonList.get(0);
            personRepo.deleteById(existingPerson.getId());
        }
        catch (BadRequestException ex){
            throw  ex;
        }
        catch (Exception ex){
            logger.error("Error while deleting Person details from the system");
            throw new PersonServiceException("Error while deleting Person details from the system");
        }
    }


    public void deleteAllUsers(){
        try{
            personRepo.deleteAll();
        }
        catch (Exception ex){
            logger.error("Database Error while deleting Person details from the system");
            throw new PersonServiceException("Database Error while deleting Person details from the system");
        }
    }


    public boolean isPersonExist(Person person){

        try{
            List<PersonEntity> existingUserList = personRepo.findAllByFirstNameAndLastName(person.getFirst_name(), person.getLast_name());

            if(existingUserList.size() != 0)
                return true;        }
        catch (Exception ex){
            logger.error("Database Error while retrieving Person details from the system");
            throw new PersonServiceException("Database Error in the system");
        }

        return false;
    }


}
