# Restful-CRUD-Application-Cefalo-Assignment

# Required Tools:

  - JDK 8
  - Maven / Gradle


# Steps to run the application:

1. Clone the repository.
    
   > git clone https://github.com/CosmicBeing09/Restful-CRUD-Application-Cefalo-Assignment.git


2. Setup maven or gradle.

   To install Maven on windows:

     * Visit the Apache Maven site, download the Maven Binary zip file of the latest version, and unzip the file to the folder that you want to use Maven in.

     * Add both M2_HOME and MAVEN_HOME variables to the Windows environment variables (using system properties), and point them to your Maven folder.

     * Update the PATH variable by appending the Maven bin folder – %M2_HOME%\bin. This way, Maven’s commands can be run anywhere.

    To check whether or not the above steps have been completed successfully, run the following command in the command prompt:
    
    > mvn -version
 
    
3. Go to **Restful-CRUD-Application-Cefalo-Assignment-master\Crud** folder from command prompt and run this command:
   > mvn install
   
4. Go to **Restful-CRUD-Application-Cefalo-Assignment-master\Crud\target**  folder and hit this command to run the application
   > java -jar Crud-0.0.1-SNAPSHOT.jar
   
   Or, you can use this **maven** command to run the application
   > mvn spring-boot:run
   
   Or, you can also use this **gradle** command to tun the application if you want to use gradle
   > gradle bootRun





# Brief description on the endpoints 

## User 

### POST  `/user/register`

### Request body Scheme: 
 
 
 
    {
    userId*		    string
                    nullable: false
    
    name*		    string
                    nullable: false
    
    
    password*		 string
                     nullable: false   
    }

 
### Response Status

Status Code | Message
------------ | -------------
201 | Created
400 | Bad Request
 
 ### Example
 
 ### Request Headers: 
``` 
Content-Type : application/json
Accept : application/json
``` 
 
### Request Body: 
``` 
 {
	"userId" : "raihan123",
	"name" : "raihan",
	"password" : "rai123"
}
```

 ### Response Body (400 Bad Request) :

```
{
     Username already taken!
}
``` 
 
 ### POST `/user/authenticate`
 
 ### Request body Scheme: 
 
 
 
    {
    userId*		    string
                    nullable: false

    password*		 string
                     nullable: false   
    }
 
 
 ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request

### Example
 
 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
``` 
 
### Request Body: 
 
 ``` 
 {
	"userId" : "raihan123",
	"password" : "rai123"
}
```
 
 
### Response Body (200 OK) :

```
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0YWhzaW4xMjMiLCJleHAiOjE1ODQzNTUyOTgsImlhdCI6MTU4NDMzNzI5OH0.GZCi0AJxGEWxZ5nZfPLGooWlmY-7vMYwdS8NPbp-2AwKfjzrhkhhD_f4ASp3d_UUgpxHkHi_gcXjb0WTrUpHSQ"
}
``` 
 
 # Story
 
 ### POST  `/post/{userId}`
 * userId : (type : string)
 
  ### Request body Scheme: 
 
 
 
    {
    title*		    string
                    max: 250
                    nullable: false

    body*		     string
                     max: 10000
                     nullable: false   
    }
 
  ### Response Status

Status Code | Message
------------ | -------------
201 | Created
400 | Bad Request
401 | unauthorized

### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
``` 
 
### Request Body: 
 
 ``` 
 {
	"title" : "rUt ultrices ultrices enim. Curabitur sit amet mauris",
	"body" : "Suspendisse in justo eu magna luctus suscipit. Sed lectus. Integer euismod lacus luctus magna. Quisque cursus"
}
```
 
 
### Response Body (201 OK) :

```
{
    Post Created
}
``` 
 
 
