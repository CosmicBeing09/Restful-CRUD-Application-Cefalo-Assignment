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


# Basic Features
 * Story Creation
    * Save as a draft
    * Set a date to publish the story
* Story Updation
  * Unpulbish story
  * Save as draft
  * Set a date to publish further
* Story deletion
* Get published story
* Get a particular story
* Get story of a particular user
* Create Tag
* Add tags to story
* Get post by a particular tag
* Add authors to a story
* Get the stories having author access
* Add comment
* Delete comment
* Get most commented story
* Get trending topics (tags)
* Search story
* Get the size of published stories
* Application level caching ...



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
                     
    publishDate*      string
                     format : YYYY-MM-dd
                     nullable : false
    
    isDrafted*        Boolean
                     nullable: false
    
    existingTags      Array
                     nullable: true
    
    newTags          Array
                     nullable: true
    
    authorsId        Array
                     nullable: true
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
Authorization : Bearer "YOUR TOKEN"
``` 
 
 ### Request Body: 
 
 ``` 
{
	"title" : "Test Title",
	"body" : "Test Body",
	"publishDate" : "2020-04-27",
	"isDrafted" : false,
	"existingTags" :  [{
		      "id"   : 1,
		      "name" : "Tag 1"
	},{
		      "id"  : 2,
		      "name" : "Tag 2"
	}],
	"newTags" : [{
		      "name" : "Tag 1"
	},{
		      "name" : "Tag 2"
	}],
	"authorsId" : ["jessica124"] 
}
```
 
 
 ### Response Body (201 Created) :

```
{
    Post Created
}
``` 


### PUT  `/post`
 
  ### Request body Scheme: 
 
 
 
    {
    version*         number
                     nullable : false
    
    title*		     string
                     max: 250
                     nullable: false

    body*		     string
                     max: 10000
                     nullable: false 
                     
    publishDate*      string
                     format : YYYY-MM-dd
                     nullable : false
    
    isDrafted*        Boolean
                     nullable: false
    }
 
  ### Response Status

Status Code | Message
------------ | -------------
201 | Created
400 | Bad Request
401 | unauthorized
412 | pre-condition failed
409 | conflict

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
If-Match : "VERSION NUMBER"
``` 
 
 ### Request Body: 
 
 ``` 
{
	"version" : 1,
	"id" : 5,
	"title" : "Title Updated",
	"body" : "Body Updated",
	"isDrafted" : false,
	"publishDate" : "2020-02-27"
}
```
 
 
 ### Response Body (409 Conflict) :

```
{
    Post already updated! Get or Reload the post again and try again!
}
``` 


### DELETE  `/post/{postId}`
 * postId : (type : number)
 
  ### Response Status

Status Code | Message
------------ | -------------
400 | Bad Request
401 | Unauthorized
404 | Not Found

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 
 
 
 ### Response Body (200 OK) :

```
{
    Deleted
}
``` 

### GET  `/posts`
 
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request
401 | Unauthorized

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 

 ### Request Params: 
 
 ``` 
pageNo : NUMBER
pageSize : NUMBER
``` 

 
 
 ### Response Body (200 OK) :

```
[
    {
        "id": 4,
        "title": "Title 1",
        "body": "Body 1",
        "date": "2020-06-05T14:33:18.906+0000",
        "publishDate": "2020-04-27T00:00:00.000+0000",
        "user": {
            "userId": "user123",
            "name": "User123",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "isPublished": true,
        "isDrafted": false,
        "tags": [
            {
                "id": 1,
                "name": "Tag 2"
            },
            {
                "id": 2,
                "name": "Tag 1"
            }
        ],
        "comments": [],
        "noOfViews": 0,
        "version": 0,
        "lastEditedAt": "2020-06-05T14:33:18.891+0000",
        "lastEditedBy": {
            "userId": "user124",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "authors": []
    },
    {
        "id": 3,
        "title": "Title 2",
        "body": "Body 2",
        "date": "2020-06-05T14:33:16.014+0000",
        "publishDate": "2020-04-27T00:00:00.000+0000",
        "user": {
            "userId": "user124",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "isPublished": true,
        "isDrafted": false,
        "tags": [
            {
                "id": 1,
                "name": "Tag 2"
            },
            {
                "id": 2,
                "name": "Tag 1"
            }
        ],
        "comments": [],
        "noOfViews": 0,
        "version": 0,
        "lastEditedAt": "2020-06-05T14:33:15.978+0000",
        "lastEditedBy": {
            "userId": "user123",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "authors": ["user123","user124"]
    }
]
``` 


### GET  `/post/{postId}`
 
 * postId : (type : number)
 
 
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request
401 | Unauthorized

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 

 
 ### Response Body (200 OK) :

```
    {
        "id": 4,
        "title": "Title 1",
        "body": "Body 1",
        "date": "2020-06-05T14:33:18.906+0000",
        "publishDate": "2020-04-27T00:00:00.000+0000",
        "user": {
            "userId": "user123",
            "name": "User123",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "isPublished": true,
        "isDrafted": false,
        "tags": [
            {
                "id": 1,
                "name": "Tag 2"
            },
            {
                "id": 2,
                "name": "Tag 1"
            }
        ],
        "comments": [],
        "noOfViews": 0,
        "version": 0,
        "lastEditedAt": "2020-06-05T14:33:18.891+0000",
        "lastEditedBy": {
            "userId": "user124",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "authors": ["user125","user126"]
    }
```

### GET  `/user-posts/{userId}`
 
 * userId : (type : string)
 
 
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request
401 | Unauthorized

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 

 
 ### Response Body (200 OK) :

```
[
    {
        "id": 4,
        "title": "Title 1",
        "body": "Body 1",
        "date": "2020-06-05T14:33:18.906+0000",
        "publishDate": "2020-04-27T00:00:00.000+0000",
        "user": {
            "userId": "user123",
            "name": "User123",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "isPublished": true,
        "isDrafted": false,
        "tags": [
            {
                "id": 1,
                "name": "Tag 2"
            },
            {
                "id": 2,
                "name": "Tag 1"
            }
        ],
        "comments": [],
        "noOfViews": 0,
        "version": 0,
        "lastEditedAt": "2020-06-05T14:33:18.891+0000",
        "lastEditedBy": {
            "userId": "user124",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "authors": []
    },
    {
        "id": 3,
        "title": "Title 2",
        "body": "Body 2",
        "date": "2020-06-05T14:33:16.014+0000",
        "publishDate": "2020-04-27T00:00:00.000+0000",
        "user": {
            "userId": "user123",
            "name": "User123",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "isPublished": true,
        "isDrafted": false,
        "tags": [
            {
                "id": 1,
                "name": "Tag 2"
            },
            {
                "id": 2,
                "name": "Tag 1"
            }
        ],
        "comments": [],
        "noOfViews": 0,
        "version": 0,
        "lastEditedAt": "2020-06-05T14:33:15.978+0000",
        "lastEditedBy": {
            "userId": "user123",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "authors": ["user123","user124"]
    }
]
```

### GET  `/posts/editor-post/{userId}`

  * userId : (type : string)
`
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request
401 | Unauthorized

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 

 ### Request Params: 
 
 ``` 
pageNo : NUMBER
pageSize : NUMBER
``` 

 
 
 ### Response Body (200 OK) :

```
[
    {
        "id": 4,
        "title": "Title 1",
        "body": "Body 1",
        "date": "2020-06-05T14:33:18.906+0000",
        "publishDate": "2020-04-27T00:00:00.000+0000",
        "user": {
            "userId": "user123",
            "name": "User123",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "isPublished": true,
        "isDrafted": false,
        "tags": [
            {
                "id": 1,
                "name": "Tag 2"
            },
            {
                "id": 2,
                "name": "Tag 1"
            }
        ],
        "comments": [],
        "noOfViews": 0,
        "version": 0,
        "lastEditedAt": "2020-06-05T14:33:18.891+0000",
        "lastEditedBy": {
            "userId": "user124",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "authors": ["user124","user125"]
    },
    {
        "id": 3,
        "title": "Title 2",
        "body": "Body 2",
        "date": "2020-06-05T14:33:16.014+0000",
        "publishDate": "2020-04-27T00:00:00.000+0000",
        "user": {
            "userId": "user124",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "isPublished": true,
        "isDrafted": false,
        "tags": [
            {
                "id": 1,
                "name": "Tag 2"
            },
            {
                "id": 2,
                "name": "Tag 1"
            }
        ],
        "comments": [],
        "noOfViews": 0,
        "version": 0,
        "lastEditedAt": "2020-06-05T14:33:15.978+0000",
        "lastEditedBy": {
            "userId": "user123",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "authors": ["user123","user124"]
    }
]
```

### GET  `/posts/search`

`
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request
401 | Unauthorized

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 

 ### Request Params: 
 
 ``` 
pageNo : NUMBER
pageSize : NUMBER
test : String
``` 

 
 
 ### Response Body (200 OK) :

```
[
    {
        "id": 3,
        "title": "Title 2",
        "body": "Body 2",
        "date": "2020-06-05T14:33:16.014+0000",
        "publishDate": "2020-04-27T00:00:00.000+0000",
        "user": {
            "userId": "user124",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "isPublished": true,
        "isDrafted": false,
        "tags": [
            {
                "id": 1,
                "name": "Tag 2"
            },
            {
                "id": 2,
                "name": "Tag 1"
            }
        ],
        "comments": [],
        "noOfViews": 0,
        "version": 0,
        "lastEditedAt": "2020-06-05T14:33:15.978+0000",
        "lastEditedBy": {
            "userId": "user123",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "authors": ["user123","user124"]
    }
]
```

### PUT  `/post/view/{postId}`
 * postId : (type : number)
`
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
``` 
 
 
 ### Response Body (200 OK) :

```
25
```

### GET  `/posts/mostCommented`

`
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
``` 

 ### Request Params: 
 
 ``` 
count : NUMBER
``` 

 
 
 ### Response Body (200 OK) :

```
[
    {
        "id": 3,
        "title": "Title 2",
        "body": "Body 2",
        "date": "2020-06-05T14:33:16.014+0000",
        "publishDate": "2020-04-27T00:00:00.000+0000",
        "user": {
            "userId": "user124",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "isPublished": true,
        "isDrafted": false,
        "tags": [
            {
                "id": 1,
                "name": "Tag 2"
            },
            {
                "id": 2,
                "name": "Tag 1"
            }
        ],
        "comments": [
        {
            "id": 5,
            "text": "Comment 1",
            "user": {
                "userId": "user123",
                "name": "User123",
                "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
            }
        },
        {
            "id": 6,
            "text": "Comment 2",
            "user": {
                "userId": "user124",
                "name": "User124",
                "password": "$2a$10$1SI5IWKg4sSfefemC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
            }
        },
        {
            "id": 7,
            "text": "Comment 3",
            "user": {
                "userId": "user123",
                "name": "User123",
                "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
            }
        }
    ],
        "noOfViews": 22,
        "version": 0,
        "lastEditedAt": "2020-06-05T14:33:15.978+0000",
        "lastEditedBy": {
            "userId": "user123",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "authors": ["user123","user124"]
    }
]
```

### GET  `/posts/size`
`
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
``` 
 
 
 ### Response Body (200 OK) :

```
25
```

### GET  `/posts/flush-cache`
`
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request
401 | Unauthorized

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 
 
 
 ### Response Body (200 OK) :

```
Hard Reloaded
```

# Comment
 
 ### POST  `/post/comment`
 
  ### Request body Scheme: 
    {
    string
    max: 1000
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
Content-Type : text/plain
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 
 ### Request Params: 
 
 ``` 
userId : String
postId : NUMBER
``` 

 ### Request Body: 
 
 ``` 
{
  Test Comment
}
```
 
 
 ### Response Body (200 OK) :

```
{
    Comment added
}
``` 

### DELETE  `/post/comment`
 
  ### Response Status

Status Code | Message
------------ | -------------
400 | Bad Request
401 | Unauthorized
404 | Not Found

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 
 
  ### Request Params: 
 
 ``` 
postId : NUMBER
commentId : NUMBER
``` 
 
 ### Response Body (200 OK) :

```
{
    Comment Deleted
}
``` 

# Tag
 
 ### GET  `/posts/tags`
 
  
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request
401 | Unauthorized

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 

 
 ### Response Body (200 OK) :

```
[
    {
        "id": 1,
        "name": "Tag 2"
    },
    {
        "id": 2,
        "name": "Tag 1"
    }
]
``` 

### GET  `/posts/tag/{name}`
  * name : (type : string)
  
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
``` 

  ### Request Params: 
 
 ``` 
pageNo : NUMBER
pageSize : NUMBER
``` 
 
 ### Response Body (200 OK) :

```
[
    {
        "id": 3,
        "title": "Title 2",
        "body": "Body 2",
        "date": "2020-06-05T14:33:16.014+0000",
        "publishDate": "2020-04-27T00:00:00.000+0000",
        "user": {
            "userId": "user124",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "isPublished": true,
        "isDrafted": false,
        "tags": [
            {
                "id": 1,
                "name": "Tag 2"
            },
            {
                "id": 2,
                "name": "Tag 1"
            }
        ],
        "comments": [
        {
            "id": 5,
            "text": "Comment 1",
            "user": {
                "userId": "user123",
                "name": "User123",
                "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
            }
        },
        {
            "id": 6,
            "text": "Comment 2",
            "user": {
                "userId": "user124",
                "name": "User124",
                "password": "$2a$10$1SI5IWKg4sSfefemC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
            }
        },
        {
            "id": 7,
            "text": "Comment 3",
            "user": {
                "userId": "user123",
                "name": "User123",
                "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
            }
        }
    ],
        "noOfViews": 22,
        "version": 0,
        "lastEditedAt": "2020-06-05T14:33:15.978+0000",
        "lastEditedBy": {
            "userId": "user123",
            "name": "User124",
            "password": "$2a$10$1SI5IWKg4sSmC4K35bslLOcR5i0MuPvDKH5oPTp/SzdqLoLTIluCO"
        },
        "authors": ["user123","user124"]
    }
]
``` 

### GET  `/posts/tag/trending`
 
  
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
``` 
  ### Request Params: 
 
 ``` 
num : NUMBER
```
 
 ### Response Body (200 OK) :

```
[
    {
        "id": 1,
        "name": "Tag 2"
    },
    {
        "id": 2,
        "name": "Tag 1"
    }
]
```

### DELETE  `/posts/tag/{tagId}`
 * tagId : (type : number)
 
  ### Response Status

Status Code | Message
------------ | -------------
400 | Bad Request
401 | Unauthorized
404 | Not Found

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 
 
  ### Request Params: 
 
 ``` 
userId : NUMBER
``` 
 
 ### Response Body (200 OK) :

```
{
  Deleted
}
``` 

 
 ### DELETE  `/posts/tag`
 
  ### Response Status

Status Code | Message
------------ | -------------
400 | Bad Request
401 | Unauthorized
404 | Not Found

 ### Request Headers: 
 
 ``` 
Content-Type : application/json
Accept : application/json
Authorization : Bearer "YOUR TOKEN"
``` 
 
  ### Request Params: 
 
 ``` 
postId : NUMBER
tagId : NUMBER
``` 
 
 ### Response Body (200 OK) :

```
{
  Deleted
}
``` 



# File 
 
 ### POST  `/uploadFile`
 
  ### Request body Scheme (form-data): 
   file : YOUR_FILE
  
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request
401 | Unauthorized

 ### Request Headers: 
 
 ``` 
Authorization : Bearer "YOUR TOKEN"
``` 

 ### Request Body (form-data): 
 
 ``` 
file : YOUR FILE
```
 
 
 ### Response Body (200 OK) :

```
{
    "url": "http://localhost:8080/downloadFile/image.jpg"
}
```  


### POST  `/uploadMultipleFiles`
 
  ### Request body Scheme (form-data): 
   files : YOUR_FILES
  
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request
401 | Unauthorized

 ### Request Headers: 
 
 ``` 
Authorization : Bearer "YOUR TOKEN"
``` 

 ### Request Body (form-data): 
 
 ``` 
files : YOUR FILES
```
 
 
 ### Response Body (200 OK) :

```
[
    {
        "url": "http://localhost:8080/downloadFile/Image1.jpg"
    },
    {
        "url": "http://localhost:8080/downloadFile/Image2.PNG"
    },
    {
        "url": "http://localhost:8080/downloadFile/Image3.jpg"
    }
]
```


### GET  `/downloadFile/{fileName:.+}`
 
 * fileNmae.EXTENSION : (type : string)
 
  ### Request body Scheme (form-data): 
   files : YOUR_FILES
  
  ### Response Status

Status Code | Message
------------ | -------------
200 | OK
400 | Bad Request


 ### Response Body (200 OK) :

```
IMAGE
```


