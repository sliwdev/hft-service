## HFT Service Assessment Task

### Running the app

To create native image and run it in a Docker container:
1) Have Docker daemon running on your host.

2) Execute
```
./gradlew clean dockerBuildNative
```
(might take a few minutes)
or
```
./gradlew clean dockerBuild
```
3) Execute
```
docker run -p 8080:8080 -d hft-service
```

You can also run the app without a container by executing:
```
./gradlew clean run
```

### Running the tests

Execute
```
./gradlew clean test
```

### Comments
Max batch request size is 10MB.

I could add more detailed and parameterized tests and Swagger.

I could also make it 1BRC style to tweak performance (at the expense of readability) but that would take much longer 😅

Example usage:
```
curl -X POST http://localhost:8080/add_batch \
     -H "Content-Type: application/json" \
     -d '{
           "symbol": "A",
           "values": [1.23, 4.56, 7.89, 10.11]
         }'
```
```
curl -X GET "http://localhost:8080/stats?symbol=A&k=1"
```

Enjoy :)

