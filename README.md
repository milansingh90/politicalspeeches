###### **Political Information**

This service helps to perform analysis on the csv files. It exposes an endpoint which takes url as input and provide meaningful insights from the data.

**Running the application**

_On Localhost:_ Please run the following commands when running the application locally:
```
Install Java 11, mySql, maven
mvn package
mvn spring-boot:run

```
_Using Minikube exposed as a service on kubernetes:_ Please run the following commands:

````
brew install minikube (If not already installed)
minikube start

Commands to be executed in the application folder

eval $(minikube docker-env) (To run it in Minikube session)
Docker build -t PoliticalSpeeches . (Building the docker image of the app)
Kubectl create -f deployment.yml (Using the deployment file to create kubernetes deployment)
kubectl expose deployment PoliticalSpeeches-deployment --type=NodePort (exposing the deployment as a service in kubernetes)
minikube service PoliticalSpeeches-deployment --url (Get the url where minikube exposes the service. Use it to hit the URL)

````
*The application can be accessed over Swagger UI as well once it is started.
The url to access is HOST-URL/swagger-ui.html*



**Functionality**

The application currently exposes the below endpoints:

GET **/analysis/csv** - Get endpoint to fetch the analytics information.

```
curl --request GET http://localhost:8080/analysis/csv?url=http://localhost:8080/fileHosting/politicalSpeechTest.csv

```

and provides information about 
```
1. most Speeches by a Speaker in 2013
2. Speaker who spoke most on the topic "Internal Security"
3. Speaker who spoke the least number of words

```

File Hosting Controller is used here to host the csv files locally for testing purpose. 
