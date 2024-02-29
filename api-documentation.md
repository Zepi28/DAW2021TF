# DAW API documentation

## Table of contents
- ~~Description~~
- [Authentication](#authenticating-rest-api-calls)
- [Project](#obtain-all-projects)
- [Issues](#List-issues-of-a-specific-project)
- ~~Users~~

The base part of the URI path for the DAW API is `/./api`

This API is available for authenticated users.

--- 

## Authenticating REST API calls

To authenticate each REST API method call you will need to provide the username and password of an active user account. The client sends HTTP requests with the Authorization header that contains the word *Basic* followed by a space and a base64-encoded string *username:password*. The credentials should be included as an additional HTTP header of the REST API request, according to the following format:

```
Authorization: Basic <credentials>
```

**Note:** Because base64 is easily decoded, Basic authentication should only be used together with other security mechanisms such as HTTPS/SSL.

([return to table of contents](#table-of-contents))

---

## Obtain all projects

```http
GET /projects
```

- Request:
  - Body: none
- Response:
  - Status: 200 OK

    ```json
      {
        "projects": [
          {
            "name": "project1",
            "description": "description of project 1"
          },
          {
            "name": "project2",
            "description": "description of project 2"
          },
          ...
        ]
      }
    ```
	
([return to table of contents](#table-of-contents))

---

## Obtain a specific project

```http
GET /projects/:name
```

- Request:
  - Path parameters:
    - name - The project identifier
  - Body: none
- Response:
  - Status: 200 OK
    
    ```json
        {      
            "name": "project identifier",
            "description": "project description"
        }
    ```

  - Errors:
    - 400 and 404 (see Common Error Handling section)
	
([return to table of contents](#table-of-contents))

---

## Create a Project

```http
POST /projects/new
```

- Request:
  - Content-Type: application/json
- Response:

```json
  {
    "name": "project1",
    "description": "description of project 1"
  },  

```

- Response:
  - Status 201 CREATED
  - Headers:
    - Location: `./api/projects/2`
 
    ```json
      {
        "status" : "Project created",
        "uri": `./api/projects/2`
      }
    ```
	
([return to table of contents](#table-of-contents))

---

## List issues of a specific project

```http
GET /projects/:name/issues
```

- Request:
  - Path parameters:
    - name - The project identifier
- Response:
  - Status 200 OK

    ```json
        {
            [{
                "id": "Issue 1 identifier",
                "name": "Issue 1 name",
                "description": "Issue 1 description",
                "state": Issue 1 state,
                "labels":[
                {
                    "name": "label 1 name"
                },
                {
                    "name": "label 2 name"
                }
                ...
                ]
            },
            {
                "id": "Issue 2 identifier",
                "name": "Issue 2 name",
                "description": "Issue 2 description",
                "state": Issue 2 state,
                "labels":[
                {
                    "name": "label 1 name"
                },
                {
                    "name": "label 2 name"
                },
                {
                    "name": "label 3 name"
                }
                ...
                ]
            }
            ...
            ]
        }
    ```

  - Errors:
    - 400 and 404 (see Common Error Handling section)

([return to table of contents](#table-of-contents))

---

## List comments of a specific issue

```http
GET /projects/:name/issues/:id
```

- Request:
  - Path parameters:
    - name - Project identifier
    - id - Issue identifier
- Response:
  - Success:
    - Status: 200 OK
   
    ```json
        {
            {
                "id": "Issue 1 identifier",
                "name": "Issue 1 name",
                "description": "Issue 1 description",
                "state": Issue 1 state
                "comments":[
                {
                    "description": "Comment 1 example",
                    "date": Date of comment 1 creation
                },
                {
                    "description": "Comment 2 example",
                    "date": Date of comment 2 creation
                }
                ...
                ]
            }
        }
    ```

  - Errors:
    - 400 and 404 (see Common Error Handling section)

([return to table of contents](#table-of-contents))

---

## Create an Issue

```http
PUT /projects/:name/issues/new
```

- Request:
  - Content-Type: application/json
  - Body:

```json
  {
    "id": "Issue id",
    "i_name": "Name of the Issue",
    "p_name": "Project name",           // TODO: Check if project name is really necessary to go in req body
    "i_description": "Issue description",
    "creationDate": "Issue date creation",
    "state": "Issue state"
  }

```

- Response:
  - Success:
    - Status: 201 CREATED
    - Headers:
      - Location: `./api/projects/:name/issues/2`
 
    ```json
      {
        "status" : "Issue created",
        "uri": `./api/projects/:name/issues/2`
      }
    ```
	
([return to table of contents](#table-of-contents))

---

## Common Error Handling

This section describes the error handling that is done in every endpoint that produces these errors. This is presented in a separate section to avoid repeating these descriptions wherever it applies.

Every error response has an `application/json` body with the content described for each error.

### 400 - Bad request

Every time the request contains a URI with and invalid query string or a body with invalid Json content for that specific request, the response has a 400 status code with the following sample body:

- Body:

  ```json
      {
        "error": "The request query string is invalid",
        "uri": "/./api/:endPoint/?InvalidQueryString",
      }
  ```
([return to table of contents](#table-of-contents))

---

### 404 - Not found

Every time the request contains a URI for a resource not managed by the API, the response has a 404 status code with the following sample body.

- Body:

  ```json
      {
        "timestamp": "2021-04-26T00:47:00.490+00:00",
	"status": 404,
	"error": "Not Found",
	"message": "Message associated with error"
        "path": "Requested path where the error occurred"
      }
  ```
([return to table of contents](#table-of-contents))
