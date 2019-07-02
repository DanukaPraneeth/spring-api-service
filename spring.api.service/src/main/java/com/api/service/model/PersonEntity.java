package com.api.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "person")
public class PersonEntity {

    @Id
    @GeneratedValue
    private int id;

    @JsonProperty("firstname")
    private String firstName;

    @JsonProperty("lastname")
    private String lastName;

    private String age;

    @JsonProperty("favouritecolour")
    private String favouriteColor;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<HobbyEntity> hobby;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getAge() {
        return age;
    }


    public void setAge(String age) {
        this.age = age;
    }


    public String getFavouriteColor() {
        return favouriteColor;
    }


    public void setFavouriteColor(String favouriteColor) {
        this.favouriteColor = favouriteColor;
    }


    public List<HobbyEntity> getHobby() {
        return hobby;
    }


    public void setHobby(List<HobbyEntity> hobby) {
        this.hobby = hobby;
    }
}
