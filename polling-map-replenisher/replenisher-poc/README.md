# replenisher-poc

## How to Run
`./gradlew bootRun`

## How to build and run app with custom config

````
./gradlew build

java -jar /path-to-project/build/libs/replenisher-poc-0.0.1-SNAPSHOT.jar
````

## Edit configurations application.properties
* firebase.replenisher.database.url=<firebase db url>
* Service account file: firebase.replenisher.config.path=firebase/ServiceAccount.json


## Swagger URL
 ````
 http://localhost:5000/swagger-ui.html

````
