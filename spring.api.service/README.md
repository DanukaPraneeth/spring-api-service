# spring-api-service

This is a simple API service for inserting, updating, deleting and searching Person entities


## Steps to build the jar file and run the service

clone the git repository to your local server
    
    link - https://github.com/DanukaPraneeth/spring-api-service.git
   
Go to the local repository, change "**application.properties**" if required to change the default custom configurations and execute the below command to build the pack

``` 
mvn clean install
```

Then start the service using below command

```
java -jar target/com.spring.api.service-1.0-SNAPSHOT.jar
```

## How to use the API service

-  Please find the below details for the current version of this API service. All the limitations and sample request are given below.

- ##### It is assumed that there will not be any two perosons with same same firstname and second name

- #### HTTP basic authentication is for this API service as a measure of security. You may change the default username and password in the "**application.properties**" file before building the API service. Make sure to use the below header for each API call you make for this service.
           -H 'Authorization: Basic {base 64 encoded username:password'


- Below sample request are for the default port in local server. You may change the domain name and port depending on your requirement

- This service is using **h2 in-memory database** to store the API request information. So all the api request related data will be lost once the service is stopped

### Sample requests:

#### Get Person Details

##### * Using a part of the name

```
    curl -X GET \
       http://localhost:8080/v1/search/persons/{name} \
       -H 'Authorization: Basic YWRtaW46cm9vdEAxMjM'
```

##### * Using full name

``` 
     curl -X GET \
        'http://localhost:8080/v1/search/persons?first_name={name_1}&last_name={name_2}' \
        -H 'Authorization: Basic YWRtaW46cm9vdEAxMjM' 
```

##### * Get All person details

```
    curl -X GET \
       http://localhost:8080/v1/search/persons/all \
       -H 'Authorization: Basic YWRtaW46cm9vdEAxMjM' 
```

#### Insert New Person Record

```
    curl -X POST \
      http://localhost:8080/v1/add/persons \
      -H 'Authorization: Basic YWRtaW46cm9vdEAxMjM' \
      -H 'Content-Type: application/json' \
      -d '{
        "person" : [
            {
                "first_name" : "Tony",
                "last_name" : "Gray",
                "age" : "14",
                "favourite_colour" : "blue",
                "hobby": ["football", "hockey", "Carrom"]
            },
            {
                "first_name" : "Antony",
                "last_name" : "Silva",
                "age" : "30",
                "favourite_colour" : "green",
                 "hobby": [ "racing", "reading"]                    
            }
        ]
    }'
```

#### Update Existing Person Record

To update the details of an existing person, request should be made with his old name ( first name and last name).
 
Update operation can only be done for one person with a single request 

```
    curl -X PUT \
      'http://localhost:8080/v1/change/persons?first_name={name_1}&last_name={name_2}' \
      -H 'Authorization: Basic YWRtaW46cm9vdEAxMjM' \
      -H 'Content-Type: application/json' \
      -d '{
        "person" : [
            {
                "first_name" : "Tony",
                "last_name" : "Fernando",
                "age" : "50",
                "favourite_colour" : "blacb",
                "hobby": ["football", "hockey", "Carrom", "filming"]
            }
        ]
    }'
```

#### Delete Existing Person Record

##### * Delete a single person

```
    curl -X DELETE \
      'http://localhost:8080/v1/delete/persons?first_name={name_1}&last_name={name_2}' \
      -H 'Authorization: Basic YWRtaW46cm9vdEAxMjM'
```

##### * Delete all persons

```
     curl -X DELETE \
       http://localhost:8080/v1/delete/persons/all \
       -H 'Authorization: Basic YWRtaW46cm9vdEAxMjM'
```